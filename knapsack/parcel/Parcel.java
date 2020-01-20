package knapsack.parcel;

import java.awt.Color;

import javafx.scene.shape.Box;

import javafxstuff.Point3D;
import knapsack.Edge3D;
import knapsack.Plane3D;
import knapsack.Size3D;

public interface Parcel {

	void moveParcel(Point3D delta);

	Point3D getOrigin();

	void setOrigin(Point3D origin);
	
	default void setOrigin(int x, int y, int z) {
		setOrigin(new Point3D(x, y, z));
	}
	
	default boolean isSameType(Parcel other) {
		return getName().equals(other.getName());
	}

	int getValue();

	Color getColor();
	
	String getName();

	<T extends Parcel> T copy();

	Size3D getHitBox();

	int getVolume();
	
	default double getDensity() {
		return getValue() / getVolume();
	}

	Point3D[] getPoints();

	Edge3D[] getEdges();

	Plane3D[] getPlanes();
	
	int getLength();
	
	int getWidth();
	
	int getHeight();
	
	Box[] toBoxes(double scale);

	/** @return the origin-points of all the unit-cube-sized grids this parcel occupies*/
	Point3D[] getOccupiedGrids();
	
	public String toString_nocoord();

}
