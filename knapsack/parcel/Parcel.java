package knapsack.parcel;

import java.awt.Color;

import javafx.scene.shape.Box;

import javafxstuff.Point3D;
import knapsack.Edge3D;
import knapsack.Plane3D;
import knapsack.Size3D;

public interface Parcel {

	void moveParcel(Point3D delta);

	//
	Point3D getScaledOrigin(double scale);
	Point3D getOrigin();

	void setOrigin(Point3D origin);

	int getValue();

	Color getColor();

	<T extends Parcel> T copy();

	Size3D getHitBox();

	int getVolume();

	Point3D[] getPoints();

	Edge3D[] getEdges();

	Plane3D[] getPlanes();
	
	Box[] toBoxes(double scale);

	/** @return the origin-points of all the unit-cube-sized grids this parcel occupies*/
	Point3D[] getOccupiedGrids();

}
