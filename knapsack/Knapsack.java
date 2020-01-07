package knapsack;

import java.math.BigInteger;
import java.util.ArrayList;

import javafxstuff.Point3D;
import knapsack.parcel.Parcel;
import knapsack.parcel.Parcels;

enum SortState {
	DISTANCE, VALUE, VOLUME, NONE;
}

public class Knapsack implements Variables {

	/** x-direction size, in 0.5 meters */
	private final int length;
	/** y-direction size, in 0.5 meters */
	private final int width;
	/** z-direction size, in 0.5 meters */
	private final int height;
	
	private ArrayList<Parcel> parcels;
	private SortState sorted;
	private BigInteger occupied_cubes;
	
	public Knapsack(int length, int width, int height) {
		this.length = length;
		this.width = width;
		this.height = height;
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
		return length;
	}
	/** y-direction size, in 0.5 meters */
	public int getWidth() {
		return width;
	}
	/** z-direction size, in 0.5 meters */
	public int getHeight() {
		return height;
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
		if (set) occupied_cubes = occupied_cubes.setBit(z * width * length + y * length + x);
		else occupied_cubes = occupied_cubes.clearBit(z * width * length + y * length + x);
	}
	public boolean isOccupied(int x, int y, int z) {
		return occupied_cubes.testBit(z * width * length + y * length + x);
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
		if (s.length + o.getX() > length || s.width + o.getY() > width || s.height + o.getZ() > height) return false;
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
