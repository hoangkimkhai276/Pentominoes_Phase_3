package knapsack.parcel;

import java.awt.Color;

import javafxstuff.Point3D;
import knapsack.Cube;
import knapsack.Edge3D;
import knapsack.Plane3D;
import knapsack.Size3D;

public class SimpleParcel extends ParcelCore {

	private Cube shape;
	
	public SimpleParcel(int length, int width, int height, int value, Point3D origin, Color color, String name) {
		super(origin, value, new Color(0,0,0,0), name);
		shape = new Cube(length, width, height, origin);
		setColor(color);
	}
	public SimpleParcel(int length, int width, int height, int value, Point3D origin, String name) {
		this(length, width, height, value, origin, new Color(0,0,0,0), name);
	}
	public SimpleParcel(int length, int width, int height, int value, String name) {
		this(length, width, height, value, Point3D.ZERO, name);
	}
	public SimpleParcel(int length, int width, int height, int value, Color color, String name) {
		this(length, width, height, value, Point3D.ZERO, color, name);
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
		int length = getLength();
		int width = getWidth();
		for (int x=0; x < length; x++)
			for (int y=0; y < width; y++)
				for (int z=0; z < getHeight(); z++)
					grids[z*length*width + y*length + x] = new Point3D(x + (int) o.getX(), y + (int) o.getY(), z + (int) o.getZ());
		return grids;
	}

	@Override
	public Size3D getHitBox() {
		return new Size3D(shape.getLength(), shape.getWidth(), shape.getHeight());
	}
	
	@Override
	public String toString() {
		Point3D p = getOrigin();
		return toString_nocoord()+" at ("+p.getX()+", "+p.getY()+", "+p.getZ()+") with value of "+getValue()+"\n";
	}
	public String toString_nocoord() {
		Size3D  s = getHitBox();
		return "SimpleParcel of "+s.length+"x"+s.width+"x"+s.height;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ParcelCore> T duplicate() {
		SimpleParcel copy =  new SimpleParcel(shape.getLength(), shape.getWidth(), shape.getHeight(), getValue(), this.getOrigin(), getColor(), getName());
		return (T) copy;
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
	@Override
	protected Cube[] toCubes() {
		return new Cube[]{shape};
	}

}
