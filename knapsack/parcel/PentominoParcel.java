package knapsack.parcel;

import java.awt.Color;
import java.util.ArrayList;

import javafxstuff.Point3D;
import knapsack.Cube;
import knapsack.Edge3D;
import knapsack.Plane3D;
import knapsack.Size3D;

public class PentominoParcel extends ParcelCore {
	
	/**
	 * These are the origin points of the unit cubes representing the pentomino <br>
	 * They are relative to the origin point of this parcel (thus the origin of this set will always be
	 *  (0,0,0) while the origin of the parcel might be something else)
	 */
	private ArrayList<Point3D> relative_origin_points;
	private int length;
	private int width;
	private int height;
	
	private Cube[] cubes;
	private boolean cubes_calculated = false;
	
	public PentominoParcel(boolean[][] shape, int value, Point3D origin, Color color) {
		super(origin, value, color);
		relative_origin_points = new ArrayList<Point3D>(5);
		length = shape.length;
		width = shape[0].length;
		height = 1;
		for (int x=0; x < length; x++)
			for (int y=0; y < width; y++)
				if (shape[x][y]) relative_origin_points.add(new Point3D(x,y,0));
	}
	public PentominoParcel(boolean[][] shape, int value, Color color) {
		this(shape, value, Point3D.ZERO, color);
	}
	protected PentominoParcel(int length, int width, int height, ArrayList<Point3D> points_to_copy, int value, Point3D origin, Color color) {
		super(origin, value, color);
		this.length = length;
		this.width = width;
		this.height = height;
		relative_origin_points = new ArrayList<Point3D>(points_to_copy.size());
		for (Point3D p : points_to_copy) relative_origin_points.add(p.add(0,0,0));
	}
	
	public String toString() {
		Point3D p = getOrigin();
		return toString_nocoord()+" at ("+p.getX()+", "+p.getY()+", "+p.getZ()+")";
	}
	public String toString_nocoord() {
		String points = "";
		for (Point3D p : relative_origin_points)
			points += "("+p.getX()+", "+p.getY()+", "+p.getZ()+") ";
		return "{["+length+"x"+width+"x"+height+"]:"+points+"}";
	}
	
	private void calculateCubes() {
		if (cubes==null) cubes = new Cube[relative_origin_points.size()];
		for (int i=0; i < cubes.length; i++)
			cubes[i] = new Cube(1, relative_origin_points.get(i));
		cubes_calculated = true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Parcel> T copy() {
		return (T) new PentominoParcel(length, width, height, relative_origin_points, getValue(), getOrigin(), getColor());
	}

	@Override
	public Size3D getHitBox() {
		return new Size3D(length, width, height);
	}

	@Override
	public int getVolume() {
		return relative_origin_points.size();
	}

	@Override
	public Point3D[] getPoints() {
		if (!cubes_calculated) calculateCubes();
		ArrayList<Point3D> point_list = new ArrayList<Point3D>(40);
		for (Cube c : cubes)
			for (Point3D p : c.getPoints()) point_list.add(p);
		return point_list.toArray(new Point3D[point_list.size()]);
	}

	@Override
	public Edge3D[] getEdges() {
		if (!cubes_calculated) calculateCubes();
		ArrayList<Edge3D> edge_list = new ArrayList<Edge3D>(60);
		for (Cube c : cubes)
			for (Edge3D e : c.getEdges()) edge_list.add(e);
		return edge_list.toArray(new Edge3D[edge_list.size()]);
	}

	@Override
	public Plane3D[] getPlanes() {
		if (!cubes_calculated) calculateCubes();
		ArrayList<Plane3D> plane_list = new ArrayList<Plane3D>(30);
		for (Cube c : cubes)
			for (Plane3D p : c.getPlanes()) plane_list.add(p);
		return plane_list.toArray(new Plane3D[plane_list.size()]);
	}

	@Override
	public Point3D[] getOccupiedGrids() {
		Point3D origin = getOrigin();
		Point3D[] result = new Point3D[relative_origin_points.size()];
		for (int i=0; i < result.length; i++) result[i] = relative_origin_points.get(i).add(origin);
		return result;
	}

	@Override
	protected void mutateOrigin(Point3D delta) {
		// nothing has to happen here because the cubes are *relative* to the origin
	}

	@Override
	public void rotateLength() {
		int temp = width;
		width = height;
		height = temp;
		for (Point3D point : relative_origin_points) point.set(new Point3D(point.getX(), -point.getZ()+width-1, point.getY()));
		cubes_calculated = false;
	}

	@Override
	public void rotateWidth() {
		int temp = length;
		length = height;
		height = temp;
		for (Point3D point : relative_origin_points) point.set(new Point3D(-point.getZ()+length-1, point.getY(), point.getX()));
		cubes_calculated = false;
	}

	@Override
	public void rotateHeight() {
		int temp = width;
		width = length;
		length = temp;
		for (Point3D point : relative_origin_points) point.set(new Point3D(-point.getY()+length-1, point.getX(), point.getZ()));
		cubes_calculated = false;
	}
	@Override
	protected Cube[] toCubes() {
		if (!cubes_calculated) calculateCubes();
		return cubes;
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

}
