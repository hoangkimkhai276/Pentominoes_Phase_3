package knapsack;

import javafxstuff.Point3D;
import javafx.scene.shape.Box;

/**
 * A {@code Cube} can be any 3-dimensional rectangular shape with 8 points, 12 edges and 6 planes.<br>
 * It contains an origin point, a length, a width and a height, and provides methods for calculating 
 *  the points, edges and planes of the {@code Cube}.<br>
 * Points, edges and planes are <b>only</b> stored once requested and will only ever be calculated <b>once</b> as long as the {@code Cube}
 *  stays the same.
 */
public class Cube {

	/** 
	 * Contains all the points where outer edges of the {@code Cube} meet, this will always be 8 points.<br>
	 * The points will only be calculated once one of the following methods is called:<blockquote>
	 * {@link #getPoints()}<br>{@link #getEdges()}<br>{@link #getPlanes()}</blockquote>
	 * The points will have to be re-calculated once a non-(0,0,0) point is {@link #add}-ed to the {@code Cube}.
	 */
	private Point3D[] points;
	/** 
	 * Contains all the outer edges of the {@code Cube}, which equals all the outer edges of all planes, this 
	 * will always be 12 edges.<br>
	 * The edges will only be calculated once the {@link #getEdges()} method is called at least once.<br>
	 * The {@link #getEdges()} method will prompt the {@link #points} to be re-calculated after a non-(0,0,0) point is
	 * {@code add}-ed to the {@code Cube} in case they have not been re-calculated yet.
	 */
	private Edge3D[] edges;
	/** 
	 * Contains all the planes of the {@code Cube}, this will always be 6 planes.<br>
	 * The planes will only be calculated once the {@link #getPlanes()} method is called at least once.<br>
	 * The {@link #getPlanes()} method will prompt the {@link #points} to be re-calculated after a non-(0,0,0) point is
	 *  {@link #add}-ed to the {@code Cube} in case they have not been re-calculated yet.
	 */
	private Plane3D[] planes;
	
	private boolean points_calculated = false;
	private boolean edges_calculated = false;
	private boolean planes_calculated = false;
	
	/** this is the point in the {@code Cube} that has the lowest total coordinates (the bottom-left-front corner)*/
	private Point3D origin;
	
	/** the size of the edges of this {@code Cube} that lie parallel to the x-axis<br><br>The represented length of this {@code Cube} in meters is given
	 * by multiplying the {@code length} of it by {@code 2}.*/
	private final int length;
	/** the size of the edges of this {@code Cube} that lie parallel to the y-axis<br><br>The represented width of this {@code Cube} in meters is given
	 * by multiplying the {@code width} of it by {@code 2}.*/
	private final int width;
	/** the size of the edges of this {@code Cube} that lie parallel to the z-axis<br><br>The represented height of this {@code Cube} in meters is given
	 * by multiplying the {@code height} of it by {@code 2}.*/
	private final int height;
	
	/**
	 * Creates a {@code Cube} with the given dimensions and origin point.
	 * @param length the {@linkplain #length} of this {@code Cube}
	 * @param width the {@linkplain #width} of this {@code Cube}
	 * @param height the {@linkplain #height} of this {@code Cube}
	 * @param origin the {@linkplain #origin} of this {@code Cube}
	 */
	public Cube(int length, int width, int height, Point3D origin) {
		super(); // points, edges and planes are initialized as null
		this.origin = origin;	
		this.length = length;
		this.width = width;
		this.height = height;
	}
	/**
	 * Creates a cube-shaped {@code Cube} object at the origin point, with all sides being the same length.
	 * @param size the {@linkplain length}, {@linkplain width} and {@linkplain height} of this {@code Cube}
	 * @param origin the {@linkplain origin} of this {@code Cube}
	 * @see #Cube(int, int, int, Point3D)
	 */
	public Cube(int size, Point3D origin) {
		this(size, size, size, origin);
	}
	/**
	 * Creates a cube-shaped {@code Cube} object at (0,0,0), with all sides being the same length.
	 * @param size the {@linkplain length}, {@linkplain width} and {@linkplain height} of this {@code Cube}
	 * @see #Cube(int, int, int, Point3D)
	 */
	public Cube(int size) {
		this(size, Point3D.ZERO);
	}
	/** Creates a unit-{@code Cube} at (0,0,0), with all sides being of length 1.
	 * @see #Cube(int, int, int, Point3D)*/
	public Cube() {
		this(1);
	}
	
	public Box toBox() {
		Box box = new Box(length, width, height);
		Point3D origin = getOrigin();
		box.setTranslateX(origin.getX());
		box.setTranslateY(origin.getY());
		box.setTranslateZ(origin.getZ());
		return box;
	}
	
	private void calculatePoints() {
		if (points==null) {
			points = new Point3D[8];
			for (int i=0; i < 8; i++) points[i] = Point3D.ZERO.add(0,0,0);
		}
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
	
	/**
	 * Gives the {@link #points} of this {@code Cube} and calls {@link #calculatePoints()} if needed.
	 * @return the points of this {@code Cube}
	 */
	public Point3D[] getPoints() {
		if (!points_calculated) calculatePoints();
		return points;
	}
	/**
	 * Gives the {@link #edges} of this {@code Cube} and calls {@link #calculateEdges()} if needed.
	 * @return the outer edges of this {@code Cube}
	 */
	public Edge3D[] getEdges() {
		if (!edges_calculated) calculateEdges();
		return edges;
	}
	/**
	 * Gives the {@link #planes} of this {@code Cube} and calls {@link #calculatePlanes()} if needed.
	 * @return the planes of this {@code Cube}
	 */
	public Plane3D[] getPlanes() {
		if (!planes_calculated) calculatePlanes();
		return planes;
	}
	
	/**
	 * Gives the volume this {@code Cube} takes up where a volume of {@code 8} equals 1 meter squared.
	 * @return {@link #length} {@code x} {@link #width} {@code x} {@link #height}
	 */
	public int getVolume() {
		return length * width * height;
	}
	
	/**
	 * This will move the {@link #origin} of this {@code Cube} by the given x, y, z values.<br>
	 * If a non-(0,0,0) input is given, this will require the {@code Cube} the re-calculate the {@link #points} whenever one of 
	 * the following methods is called:<blockquote>
	 * {@link #getPoints()}<br>{@link #getEdges()}<br>{@link #getPlanes()}</blockquote>
	 * @param x the x value to be added to or subtracted from the {@code origin} of the {@code Cube}
	 * @param y the y value to be added to or subtracted from the {@code origin} of the {@code Cube}
	 * @param z the z value to be added to or subtracted from the {@code origin} of the {@code Cube}
	 */
	public void add(int x, int y, int z) {
		origin = origin.add(x, y, z);
		if (x!=0 || y!=0 || z!=0) points_calculated = false;
	}
	/**
	 * This will move the {@code origin} of this {@code Cube} by the given {@code Point3D}'s x, y, z values.<br>
	 * @see #add(int, int, int)
	 */
	public void add(Point3D point) {
		add((int)point.getX(), (int)point.getY(), (int)point.getZ());
	}
	
	/** Gives the {@link #origin} of this {@code Cube}.
	 *  @return the {@code origin} of this {@code Cube} object */
	public Point3D getOrigin() {
		return origin;
	}
	
	@Override
	public String toString() {
		return "Cube of ["+length+"x"+width+"x"+height+"] at ("+origin.getX()+", "+origin.getY()+", "+origin.getZ()+")";
	}
	@Override
	public boolean equals(Object o) {
		if (o==this) return true;
		if (!(o instanceof Cube)) return false;
		Cube other = (Cube)o;
		if (other.length != length || other.width != width || other.height != height) return false;
		if (!other.origin.equals(origin)) return false;
		return true;
	}
	
	/** the {@link #length} of this {@code Cube}
	 *  @return the {@code length} of this {@code Cube} */
	public int getLength() {
		return length;
	}
	/** the {@link #width} of this {@code Cube}
	 *  @return the {@code width} of this {@code Cube} */
	public int getWidth() {
		return width;
	}
	/** the {@link #height} of this {@code Cube}
	 *  @return the {@code height} of this {@code Cube} */
	public int getHeight() {
		return height;
	}
	/**
	 * Tells you whether this {@code Cube} object is shaped like a cube or not.<br>
	 * A cube being any 3-dimensional rectangular shape where all sides have the same length.
	 * @return {@code true} if all sides have the same length, {@code false} otherwise
	 * @see #Cube(int, Point3D)
	 */
	public boolean isCube() {
		return length==width && width==height;
	}
	
}
