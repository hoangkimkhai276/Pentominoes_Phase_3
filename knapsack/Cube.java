package knapsack;

import javafxstuff.Point3D;
//Hello
public class Cube {

	private Point3D[] points;
	private Edge3D[] edges;
	private Plane3D[] planes;
	private boolean points_calculated = false;
	private boolean edges_calculated = false;
	private boolean planes_calculated = false;
	private Point3D origin;
	
	/** x-direction size, in 0.5 meters */
	private final int length;
	/** y-direction size, in 0.5 meters */
	private final int width;
	/** z-direction size, in 0.5 meters */
	private final int height;
	
	public Cube(int length, int width, int height, Point3D origin) {
		super(); // points, edges and planes are initialized as null
		this.origin = origin;	
		this.length = length;
		this.width = width;
		this.height = height;
	}
	public Cube(int size, Point3D origin) {
		this(size, size, size, origin);
	}
	public Cube(int size) {
		this(size, Point3D.ZERO);
	}
	/** constructs the unit cube */
	public Cube() {
		this(1);
	}
	
	private void calculatePoints() {
		if (points==null) points = new Point3D[8];
		points[0].set(origin.add(0,0,0));
		points[1].set(origin.add(length,0,0));
		points[2].set(origin.add(0,width,0));
		points[3].set(origin.add(0,0,height));
		points[4].set(origin.add(length,width,0));
		points[5].set(origin.add(length,0,height));
		points[6].set(origin.add(0,width,height));
		points[7].set(origin.add(length,width,height));
		points_calculated = true;
	}
	
	private void calculateEdges() {
		if (!points_calculated) calculatePoints();
		if (edges_calculated) return;
		edges = new Edge3D[12];
		edges[0]  = new Edge3D(points[0], points[1]);
		edges[1]  = new Edge3D(points[0], points[2]);
		edges[2]  = new Edge3D(points[0], points[3]);
		edges[3]  = new Edge3D(points[1], points[4]);
		edges[4]  = new Edge3D(points[1], points[5]);
		edges[5]  = new Edge3D(points[2], points[4]);
		edges[6]  = new Edge3D(points[2], points[6]);
		edges[7]  = new Edge3D(points[3], points[5]);
		edges[8]  = new Edge3D(points[3], points[6]);
		edges[9]  = new Edge3D(points[4], points[7]);
		edges[10] = new Edge3D(points[5], points[7]);
		edges[11] = new Edge3D(points[6], points[7]);
		edges_calculated = true;
	}
	
	private void calculatePlanes() {
		if (!points_calculated) calculatePoints();
		if (planes_calculated) return;
		planes = new Plane3D[6];
		planes[1] = new Plane3D(points[3],points[6],points[7],points[5]);
		planes[2] = new Plane3D(points[3],points[6],points[2],points[0]);
		planes[3] = new Plane3D(points[5],points[7],points[4],points[1]);
		planes[4] = new Plane3D(points[2],points[6],points[7],points[4]);
		planes[5] = new Plane3D(points[0],points[3],points[5],points[1]);
		planes_calculated = true;
	}
	
	public Point3D[] getPoints() {
		if (!points_calculated) calculatePoints();
		return points;
	}
	public Edge3D[] getEdges() {
		if (!edges_calculated) calculateEdges();
		return edges;
	}
	public Plane3D[] getPlanes() {
		if (!planes_calculated) calculatePlanes();
		return planes;
	}
	
	public int getVolume() {
		return length * width * height;
	}
	
	public void add(int x, int y, int z) {
		origin = origin.add(x, y, z);
		points_calculated = false;
	}
	public void add(Point3D point) {
		add((int)point.getX(), (int)point.getY(), (int)point.getZ());
	}
	public Point3D getOrigin() {
		return origin;
	}
	
	public String toString() {
		return getPoints().toString();
	}
	
	public int getLength() {
		return length;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	
	public boolean isCube() {
		return length==width && width==height;
	}
	
}
