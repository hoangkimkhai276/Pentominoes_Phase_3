package knapsack;

import javafxstuff.Point3D;

/**
 * {@code Edge3D} objects represent a line-piece in 3D space.<br>
 * it contains of two {@code Point3D} objects
 */
public class Edge3D {

	public final Point3D point_1;
	public final Point3D point_2;
	
	public Edge3D(Point3D a, Point3D b) {
		point_1 = a;
		point_2 = b;
	}
	
	public String toString() {
		return point_1.toString()+" -> "+point_2.toString();
	}
	
}
