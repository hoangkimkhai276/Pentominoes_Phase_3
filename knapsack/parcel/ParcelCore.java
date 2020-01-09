package knapsack.parcel;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;

import javafx.scene.shape.Box;

import javafxstuff.Point3D;
import knapsack.Cube;

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
	public Box[] toBoxes(double scale) {
		Cube[] cubes = toCubes();
		Box[] boxes = new Box[cubes.length];
		Point3D origin = getOrigin();
		for (int i=0; i < boxes.length; i++) {
			boxes[i] = cubes[i].toBox();
			boxes[i].setWidth(scale);
			boxes[i].setHeight(scale);
			boxes[i].setDepth(scale);
			if (cubes[i].getOrigin().equals(origin)) continue;
			boxes[i].setTranslateX(scale * (origin.getY() - cubes[i].getOrigin().getY()));
			boxes[i].setTranslateY(scale * (origin.getZ() - cubes[i].getOrigin().getZ()));
			boxes[i].setTranslateZ(scale * (origin.getX() - cubes[i].getOrigin().getX()));
		}
		return boxes;
	}
	
	protected abstract Cube[] toCubes();

	/** Increases/decreases the location of the origin of this parcel
	 *  @param delta - the vector which is added to the origin */
	protected abstract void mutateOrigin(Point3D delta);

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
