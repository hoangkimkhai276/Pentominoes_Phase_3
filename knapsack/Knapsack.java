package knapsack;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

import static knapsack.Variables.*;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafxstuff.Point3D;
import knapsack.parcel.FastParcel;
import knapsack.parcel.Parcel;
import knapsack.parcel.ParcelGroup;
import knapsack.parcel.Parcels;

enum SortState {
	/** Sorted by distance to the origin (0,0,0)
	 * @see {@linkplain Parcels#DISTANCE_SORT}*/
	DISTANCE,
	/** Sorted by value
	 * @see {@linkplain Parcels#VALUE_SORT}*/
	VALUE,
	/** Sorted by volume
	 * @see {@linkplain Parcels#VOLUME_SORT}*/
	VOLUME,
	/** not sorted in any particular way */
	NONE;
}

/**
 * The {@code Knapsack} represents a 3-dimensional rectangular container made for holding {@link Parcel} objects.<br>
 * It stores the parcels in an {@link ArrayList} and keeps track of the occupied grids in the {@code Knapsack}
 *  with a {@link BigInteger}.<br><br>
 * The {@code Knapsack} class provides methods for fitting parcels into itself.
 */
public class Knapsack {

	/**
	 * The shape of this {@code Knapsack} which specifies the {@code length}, {@code width} and {@code height} of this {@code Knapsack}
	 * @see Cube#length
	 * @see Cube#width
	 * @see Cube#height
	 */
	private final Cube shape;

	protected ArrayList<Parcel> parcels;
	protected HashSet<Integer> stored_IDs;
	protected SortState sorted;
	protected BigInteger occupied_cubes;

	public Knapsack(int length, int width, int height) {
		this.shape = new Cube(length, width, height, Point3D.ZERO);
		parcels = new ArrayList<Parcel>();
		sorted = SortState.NONE;
		occupied_cubes = BigInteger.ZERO; // empty array
		stored_IDs = new HashSet<Integer>();
	}
	public Knapsack(double length, double width, double height) {
		this((int)(2d * length), (int)(2d * width), (int)(2d * height));
	}
	public Knapsack() {
		this(DEFAULT_LENGTH, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	/** x-direction size, in 0.5 meters */
	public int getLength() {
		return shape.getLength();
	}
	/** y-direction size, in 0.5 meters */
	public int getWidth() {
		return shape.getWidth();
	}
	/** z-direction size, in 0.5 meters */
	public int getHeight() {
		return shape.getHeight();
	}
	public int getVolume() {
		return shape.getVolume();
	}

	public void add(Parcel to_add) {
		parcels.add(to_add);
		setBits(to_add, true);
		sorted = SortState.NONE;
	}
	public void add(FastParcel to_add) {
		parcels.add(to_add);
		int[] shape = to_add.getParcelShape();
		for (int i=0; i < shape.length; i++) occupied_cubes = occupied_cubes.setBit(shape[i]);
		sorted = SortState.NONE;
		stored_IDs.add(to_add.getID());
	}

	public boolean remove(Parcel parcel) {
		if (parcels.remove(parcel)) setBits(parcel, false);
		else return false;
		return true;
	}
	public boolean remove(FastParcel parcel) {
		if (parcels.remove(parcel)) {
			int[] shape = parcel.getParcelShape();
			for (int i=0; i < shape.length; i++) occupied_cubes = occupied_cubes.clearBit(shape[i]);
			stored_IDs.remove(parcel.getID());
		} else return false;
		return true;
	}

	private void setBits(Parcel p, boolean set) {
		Point3D[] points = p.getOccupiedGrids();
		for (Point3D point : points)
			setBit((int)point.getX(), (int)point.getY(), (int)point.getZ(), set);
	}
	private void setBit(int x, int y, int z, boolean set) {
		if (set) occupied_cubes = occupied_cubes.setBit(z * getWidth() * getLength() + y * getLength() + x);
		else occupied_cubes = occupied_cubes.clearBit(z * getWidth() * getLength() + y * getLength() + x);
	}
	public boolean isOccupied(int x, int y, int z) {
		return occupied_cubes.testBit(z * getWidth() * getLength() + y * getLength() + x);
	}

	public void sortParcels() {
		Parcels.sortByDistance(parcels);
		sorted = SortState.DISTANCE;
	}

	public SortState getSortState() {
		return sorted;
	}

	public boolean fitsParcel(Parcel parcel) {
		Point3D o = parcel.getOrigin();
		if (o.getX() < 0 || o.getY() < 0 || o.getZ() < 0) return false;
		Size3D s = parcel.getHitBox();
		if (s.length + o.getX() > getLength() || s.width + o.getY() > getWidth() || s.height + o.getZ() > getHeight()) return false;
		Point3D[] points = parcel.getOccupiedGrids();
		for (Point3D point : points) if (this.isOccupied((int)point.getX(), (int)point.getY(), (int)point.getZ())) return false;
		return true;
	}

	public boolean fitsParcel(FastParcel parcel) {
		int[] shape = parcel.getParcelShape();
		for (int i=0; i < shape.length; i++) if (occupied_cubes.testBit(shape[i])) return false;
		return true;
	}

	public int getValue() {
		int value = 0;
		for (Parcel parcel : parcels) value += parcel.getValue();
		return value;
	}
	public int getFilledVolume() {
		return occupied_cubes.bitCount();
	}
	public int getEmptyVolume() {
		return getVolume() - getFilledVolume();
	}
	public boolean isFilled() {
		return occupied_cubes.bitCount()==(getLength() * getWidth() * getHeight());
	}
	public BigInteger getOccupiedCubes() {
		return occupied_cubes;
	}

	/** @return {@code true} if the parcel fits, then immediately adds it to the knapsack<br>
	 *  otherwise returns {@code false}*/
	public boolean putParcel(Parcel parcel) {
		if (fitsParcel(parcel)) add(parcel);
		else return false;
		return true;
	}
	public boolean putParcel(FastParcel parcel) {
		if (fitsParcel(parcel)) add(parcel);
		else return false;
		return true;
	}

	public Box toBox(double scale) {
		Box result = new Box();
		result.setWidth(getWidth() * scale);
		result.setHeight(getHeight() * scale);
		result.setDepth(getLength() * scale);
		result.setTranslateX(result.getWidth()/2d); // moves in width (right)
		result.setTranslateY(-result.getHeight()/2d); // moves in height (down)
		result.setTranslateZ(result.getDepth()/2d); // moves in depth (backwards)
		result.setDrawMode(DrawMode.LINE);
		return result;
	}

	public ArrayList<ParcelGroup> getParcelGroups(double scale) {
		ArrayList<ParcelGroup> result = new ArrayList<ParcelGroup>();
		for (Parcel parcel : parcels) result.add(new ParcelGroup(parcel, scale));
		return result;
	}

	public String toString() {
		return "KS["+getLength()+"x"+getWidth()+"x"+getHeight()+"] with:\n"+parcels.toString();
	}

	public int to1DCoord(int x, int y, int z) {
		return z * shape.getWidth() * shape.getLength() + y * shape.getLength() + x;
	}
	public int to1DCoord(Point3D point) {
		return to1DCoord(point.getX(), point.getY(), point.getZ());
	}

	public Point3D toPoint(int coord) {
		int width = shape.getWidth();
		int length = shape.getLength();
		int z = coord / width / length;
		int y = coord / length - z * width;
		int x = coord - z * length * width - y * length;
		return new Point3D(x,y,z);
	}

	public Knapsack getEmpty() {
		Knapsack result = new Knapsack(getLength(), getWidth(), getHeight());
		result.shape.add(shape.getOrigin());
		return result;
	}
	public void copyTo(Knapsack other) {
		other.occupied_cubes = occupied_cubes.add(BigInteger.ZERO);
		other.parcels.clear();
		other.stored_IDs.clear();
		for (Parcel p : parcels) other.parcels.add(p);
		for (Integer ID : stored_IDs) other.stored_IDs.add(ID);
		other.sorted = sorted;
	}
	public Knapsack copy() {
		Knapsack knapsack = getEmpty();
		copyTo(knapsack);
		return knapsack;
	}
	
	public boolean compareContents(HashSet<Integer> contents) {
		return stored_IDs.equals(contents);
	}
	@SuppressWarnings("unchecked")
	public HashSet<Integer> getContents() {
		return (HashSet<Integer>) stored_IDs.clone();
	}
	
}
