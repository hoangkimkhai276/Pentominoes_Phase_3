package knapsack;

import javafxstuff.Point3D;

public class Plane3D {
	
	public final Point3D p1;
	public final Point3D p2;
	public final Point3D p3;
	public final Point3D p4;
	
	public Plane3D(Point3D p1, Point3D p2, Point3D p3, Point3D p4) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
	}
	
	public Point3D midpoint() {
		return p1.midpoint(p2).midpoint(p3.midpoint(p4));
	}
	
}
