package knapsack.parcel;

import java.awt.Color;

import javafxstuff.Point3D;
import knapsack.Edge3D;
import knapsack.Plane3D;
import knapsack.Size3D;

public abstract class Parcel {

	private Point3D origin;
	private final int value;
	private Color color;

	protected Parcel(Point3D origin, int value, Color color) {
		this.value = value;
		this.color = color;
		this.origin = origin;
	}

	public void moveParcel(Point3D delta) {
		this.mutateOrigin(delta);
		origin = origin.add(delta);
	}

	public Point3D getOrigin() {
		return origin;
	}
	public void setOrigin(Point3D origin) {
		this.mutateOrigin(origin.subtract(this.origin));
		this.origin = origin;
	}

	public int getValue() {
		return value;
	}
	
	public Color getColor() {
		return color;
	}
	protected void setColor(Color color) {
		this.color = color;
	}

	public abstract <T extends Parcel> T copy();

	public abstract Size3D getHitBox();

	public abstract int getVolume();

	public abstract Point3D[] getPoints();

	public abstract Edge3D[] getEdges();

	public abstract Plane3D[] getPlanes();

	/** @return the origin-points of all the unit-cube-sized grids this parcel occupies*/
	public abstract Point3D[] getOccupiedGrids();

	/** Increases/decreases the location of the origin of this parcel
	 *  @param delta - the vector which is added to the origin */
	protected abstract void mutateOrigin(Point3D delta);

	/** Rotate around the length-/x-axis at an angle of 90 degrees (right or left rotation is consistent but no direction is ensured) */
	public abstract void rotateLength();

	/** Rotate around the width-/y-axis at an angle of 90 degrees (right or left rotation is consistent but no direction is ensured) */
	public abstract void rotateWidth();

	/** Rotate around the height-/z-axis at an angle of 90 degrees (right or left rotation is consistent but no direction is ensured) */
	public abstract void rotateHeight();

	@Override
	public boolean equals(Object o) {
		if (o==this) return true;
		if (!(o instanceof Parcel)) return false;
		Parcel p = (Parcel)o;
		if (!p.origin.equals(origin)) return false;
		if (p.getValue() != getValue() || !p.getClass().equals(getClass())) return false;
		Point3D[] pgrid = p.getPoints();
		Point3D[] tgrid = getPoints();
		for (int i=0; i < pgrid.length; i++)
			if (!pgrid[i].equals(tgrid[i])) return false;
		// as soon as there is a point that isn't the same in both objects, return false
		return true;
	}

}
