package knapsack;

import java.math.BigInteger;
import java.util.ArrayList;

import javafxstuff.Point3D;
import knapsack.parcel.Parcel;
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
public class Knapsack implements Variables {
	
	/**
	 * The shape of this {@code Knapsack} which specifies the {@code length}, {@code width} and {@code height} of this {@code Knapsack}
	 * @see Cube#length
	 * @see Cube#width
	 * @see Cube#height
	 */
	private final Cube shape;
	
	private ArrayList<Parcel> parcels;
	private SortState sorted;
	private BigInteger occupied_cubes;
	
	public Knapsack(int length, int width, int height) {
		this.shape = new Cube(length, width, height, Point3D.ZERO);
		parcels = new ArrayList<Parcel>();
		sorted = SortState.NONE;
		occupied_cubes = BigInteger.ZERO; // empty array
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
	
	public boolean remove(Parcel parcel) {
		setBits(parcel, false);
		return parcels.remove(parcel);
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
	
	/** @return {@code true} if the parcel fits, then immediately adds it to the knapsack<br>
	 *  otherwise returns {@code false}*/
	public boolean putParcel(Parcel parcel) {
		if (fitsParcel(parcel)) add(parcel);
		else return false;
		return true;
	}
	
}
