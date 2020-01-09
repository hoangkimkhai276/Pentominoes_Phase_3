package knapsack.parcel;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;

import javafxstuff.Point3D;
import knapsack.Cube;
import knapsack.Edge3D;
import knapsack.Knapsack;
import knapsack.Plane3D;
import knapsack.Size3D;

public class SimpleParcel extends ParcelCore {

	private Cube shape;
	
	public SimpleParcel(int length, int width, int height, int value, Point3D origin, Color color) {
		super(origin, value, new Color(0,0,0,0));
		shape = new Cube(length, width, height, origin);
		setColor(color);
	}
	public SimpleParcel(int length, int width, int height, int value, Point3D origin) {
		this(length, width, height, value, origin, new Color(0,0,0,0));
	}
	public SimpleParcel(int length, int width, int height, int value) {
		this(length, width, height, value, Point3D.ZERO);
	}
	public SimpleParcel(int length, int width, int height, int value, Color color) {
		this(length, width, height, value, Point3D.ZERO, color);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Parcel> T copy() {
		SimpleParcel copy =  new SimpleParcel(shape.getLength(), shape.getWidth(), shape.getHeight(), getValue(), this.getOrigin(), getColor());
		return (T) copy;
	}
	
	@Override
	public int getVolume() {
		return shape.getVolume();
	}

	@Override
	protected void mutateOrigin(Point3D delta) {
		shape.add(delta);
	}

	@Override
	public void rotateLength() {
		if (shape.isCube()) return;
		// rotating axis stays the same, other two axis swap
		shape = new Cube(shape.getLength(), shape.getHeight(), shape.getWidth(), shape.getOrigin());
	}
	@Override
	public void rotateWidth() {
		if (shape.isCube()) return;
		// rotating axis stays the same, other two axis swap
		shape = new Cube(shape.getHeight(), shape.getWidth(), shape.getLength(), shape.getOrigin());
	}
	@Override
	public void rotateHeight() {
		if (shape.isCube()) return;
		// rotating axis stays the same, other two axis swap
		shape = new Cube(shape.getWidth(), shape.getLength(), shape.getHeight(), shape.getOrigin());
	}

	@Override
	public Point3D[] getPoints() {
		return shape.getPoints();
	}
	@Override
	public Edge3D[] getEdges() {
		return shape.getEdges();
	}
	@Override
	public Plane3D[] getPlanes() {
		return shape.getPlanes();
	}

	@Override
	public Point3D[] getOccupiedGrids() {
		Point3D[] grids = new Point3D[getVolume()];
		Point3D o = getOrigin();
		int height = getHeight();
		int width = getWidth();
		int start_x = (int) o.getX();
		int start_y = (int) o.getY();
		int start_z = (int) o.getZ();
		for (int x=start_x; x < getLength() + start_x; x++)
			for (int y=start_y; y < width + start_y; y++)
				for (int z=start_z; z < height + start_z; z++)
					grids[x*height*width + y*height + z] = new Point3D(x, y, z);
		return grids;
	}

	@Override
	public Size3D getHitBox() {
		return new Size3D(shape.getLength(), shape.getWidth(), shape.getHeight());
	}
	
	@Override
	public String toString() {
		Size3D  s = getHitBox();
		Point3D p = getOrigin();
		return "SimpleParcel of "+s.length+"x"+s.width+"x"+s.height+" at ("+p.getX()+", "+p.getY()+", "+p.getZ()+")";
	}
	
	public int getLength() {
		return shape.getLength();
	}
	public int getWidth() {
		return shape.getWidth();
	}
	public int getHeight() {
		return shape.getHeight();
	}

}
