package knapsack.parcel;

import java.awt.Color;
import java.util.ArrayList;

import javafx.scene.shape.Box;
import javafxstuff.Point3D;
import knapsack.Edge3D;
import knapsack.Plane3D;
import knapsack.Size3D;

/**
 * The {@code ParcelType} class is a wrapper for {@link ParcelCore} objects which makes comparing two parcels much more efficient by
 *  giving each different parcel an ID {@code byte}.<br>
 * When the {@code ParcelType} is created, it uses the {@link #equals} method of {@link ParcelCore} to check the unique parcels. 
 *  Then during calculations, only the checking of the {@code ID} is required to compare two parcels, giving more efficient computations
 */
public class ParcelType implements Parcel {

	private static ArrayList<ParcelCore> stored_parcels = new ArrayList<ParcelCore>();
	private static ArrayList<Byte> stored_IDs = new ArrayList<Byte>();
	public static byte last_ID = Byte.MIN_VALUE;
	
	private final ParcelCore parcel;
	private final byte ID;
	
	public ParcelType(ParcelCore parcel) {
		int index = stored_parcels.indexOf(parcel);
		if (index != -1) {
			this.parcel = stored_parcels.get(index);
			this.ID = stored_IDs.get(index).byteValue();
		} else {
			this.parcel = parcel;
			this.ID = last_ID;
			stored_parcels.add(parcel);
			stored_IDs.add(last_ID++);
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o==this) return true;
		if (!(o instanceof ParcelType)) return false;
		if (((ParcelType)o).ID != ID) return false;
		if (!((ParcelType)o).parcel.getOrigin().equals(parcel.getOrigin())) return false;
		return true;
	}
	@Override
	public String toString() {
		return "<"+ID+">"+parcel.toString();
	}

	// Parcel methods:
	@Override
	public void moveParcel(Point3D delta) {
		parcel.moveParcel(delta);
	}
	@Override
	public Point3D getOrigin() {
		return parcel.getOrigin();
	}
	@Override
	public void setOrigin(Point3D origin) {
		parcel.setOrigin(origin);
	}
	@Override
	public int getValue() {
		return parcel.getValue();
	}
	@Override
	public Color getColor() {
		return parcel.getColor();
	}
	@Override
	public <T extends Parcel> T copy() {
		return parcel.copy();
	}
	@Override
	public Size3D getHitBox() {
		return parcel.getHitBox();
	}
	@Override
	public int getVolume() {
		return parcel.getVolume();
	}
	@Override
	public Point3D[] getPoints() {
		return parcel.getPoints();
	}
	@Override
	public Edge3D[] getEdges() {
		return parcel.getEdges();
	}
	@Override
	public Plane3D[] getPlanes() {
		return parcel.getPlanes();
	}
	@Override
	public Box[] toBoxes(double scale) {
		return parcel.toBoxes(scale);
	}
	@Override
	public Point3D[] getOccupiedGrids() {
		return parcel.getOccupiedGrids();
	}
	@Override
	public void rotateLength() {
		parcel.rotateLength();
	}
	@Override
	public void rotateWidth() {
		parcel.rotateWidth();
	}
	@Override
	public void rotateHeight() {
		parcel.rotateHeight();
	}
	
}
