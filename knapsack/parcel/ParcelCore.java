package knapsack.parcel;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;

import javafxstuff.Point3D;
import knapsack.Edge3D;
import knapsack.Plane3D;
import knapsack.Size3D;

public abstract class ParcelCore implements Parcel {

	private Point3D origin;
	private final int value;
	private Color color;

	protected ParcelCore(Point3D origin, int value, Color color) {
		this.value = value;
		this.color = color;
		this.origin = origin;
	}

	@Override
	public void moveParcel(Point3D delta) {
		this.mutateOrigin(delta);
		origin = origin.add(delta);
	}

	@Override
	public Point3D getOrigin() {
		return origin;
	}
	@Override
	public void setOrigin(Point3D origin) {
		this.mutateOrigin(origin.subtract(this.origin));
		this.origin = origin;
	}

	@Override
	public int getValue() {
		return value;
	}
	
	@Override
	public Color getColor() {
		return color;
	}
	protected void setColor(Color color) {
		this.color = color;
	}

	@Override
	public abstract <T extends Parcel> T copy();

	@Override
	public abstract Size3D getHitBox();

	@Override
	public abstract int getVolume();

	@Override
	public abstract Point3D[] getPoints();

	@Override
	public abstract Edge3D[] getEdges();

	@Override
	public abstract Plane3D[] getPlanes();

	/** @return the origin-points of all the unit-cube-sized grids this parcel occupies*/
	@Override
	public abstract Point3D[] getOccupiedGrids();

	/** Increases/decreases the location of the origin of this parcel
	 *  @param delta - the vector which is added to the origin */
	protected abstract void mutateOrigin(Point3D delta);

	/** Rotate around the length-/x-axis at an angle of 90 degrees (right or left rotation is consistent but no direction is ensured) */
	@Override
	public abstract void rotateLength();

	/** Rotate around the width-/y-axis at an angle of 90 degrees (right or left rotation is consistent but no direction is ensured) */
	@Override
	public abstract void rotateWidth();

	/** Rotate around the height-/z-axis at an angle of 90 degrees (right or left rotation is consistent but no direction is ensured) */
	@Override
	public abstract void rotateHeight();

	@Override
	public boolean equals(Object o) {
		if (o==this) return true;
		if (!(o instanceof ParcelCore)) return false;
		ParcelCore p = (ParcelCore)o;
		if (!p.origin.equals(origin)) return false;
		if (p.getValue() != getValue() || !p.getClass().equals(getClass())) return false;
		
		HashSet<Point3D> pset = new HashSet<Point3D>(Arrays.asList(p.getOccupiedGrids()));
		HashSet<Point3D> tset = new HashSet<Point3D>(Arrays.asList(getOccupiedGrids()));
		if (!pset.equals(tset)) return false;
		// as soon as there is a point that isn't in both objects' occupied-grid-space, return false
		return true;
	}

}
