package knapsack.parcel;

import java.awt.Color;

import javafxstuff.Point3D;
import knapsack.Cube;
import knapsack.Edge3D;
import knapsack.Plane3D;
import knapsack.Size3D;

public class SimpleParcel extends Parcel {

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
		int length = shape.getLength();
		int width  = shape.getWidth();
		int height = shape.getHeight();
		Point3D[] grids = new Point3D[length * width * height];
		Point3D current = getOrigin().subtract(1,1,1);
		int index = 0;
		for (int x=0; x < length; x++) {
			current.set(current.add(1, 0, 0));
			for (int y=0; y < width; y++) {
				current.set(current.add(0, 1, 0));
				for (int z=0; z < height; z++) {
					current.set(current.add(0, 0, 1));
					grids[index++] = current;
					current = current.add(0,0,0);
				} current.set(current.subtract(0, 0, height));
			} current.set(current.subtract(0, width, 0));
		}
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

}
