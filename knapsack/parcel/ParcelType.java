package knapsack.parcel;

import java.awt.Color;
import java.lang.constant.ClassDesc;
import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.lang.constant.DirectMethodHandleDesc;
import java.lang.constant.DynamicConstantDesc;
import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.shape.Box;
import javafxstuff.Point3D;
import knapsack.Edge3D;
import knapsack.Plane3D;
import knapsack.Size3D;

/**
 * The {@code ParcelType} class is a wrapper for {@link ParcelCore} objects which makes comparing two parcels much more efficient by
 *  giving each different parcel an {@code int} ID.<br>
 * When the {@code ParcelType} is created, it uses the {@link #equals} method of {@link ParcelCore} to check the unique parcels. 
 *  Then during calculations, only the checking of the {@code ID} is required to compare two parcels, giving more efficient computations
 */
public class ParcelType implements Parcel {

	private static ArrayList<ParcelCore> stored_parcels = new ArrayList<ParcelCore>();
	
	private Point3D origin;
	private final int ID;
	
	public ParcelType(ParcelCore parcel) {
		int index = stored_parcels.indexOf(parcel);
		if (index != -1) {
			// the parcel already exists as a stored parcel
			this.origin = stored_parcels.get(index).getOrigin();
			this.ID = index;
		} else {
			// the parcel is new, so we add it to the stored parcels
			this.origin = parcel.getOrigin();
			this.ID = stored_parcels.size();
			stored_parcels.add(parcel);
		}
	}
	public ParcelType(int ID, Point3D origin) {
		if (ID >= stored_parcels.size()) throw new IndexOutOfBoundsException("ID "+ID+" does not exist yet");
		this.ID = ID;
		this.origin = origin;
	}
	public ParcelType(int ID) {
		if (ID >= stored_parcels.size()) throw new IndexOutOfBoundsException("ID "+ID+" does not exist yet");
		this.ID = ID;
	}
	
	public static ParcelCore getParcel(int ID) {
		return stored_parcels.get(ID);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o==this) return true;
		if (!(o instanceof ParcelType)) return false;
		if (((ParcelType)o).ID != ID) return false;
		if (!((ParcelType)o).getOrigin().equals(getOrigin())) return false;
		return true;
	}
	@Override
	public String toString() {
		return "<"+ID+"> at ("+getOrigin().getX()+", "+origin.getY()+", "+origin.getZ()+")";
	}

	// Parcel methods:
	@Override
	public void moveParcel(Point3D delta) {
		getOrigin().add(delta);
	}

    @Override
    public Point3D getScaledOrigin(double scale) {
        return null;
    }

    @Override
	public Point3D getOrigin() {
		if (origin==null) origin = new Point3D(0,0,0);
		return origin;
	}
	@Override
	public void setOrigin(Point3D origin) {
		this.origin = origin;
	}
	@Override
	public int getValue() {
		return stored_parcels.get(ID).getValue();
	}
	@Override
	public Color getColor() {
		return stored_parcels.get(ID).getColor();
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Parcel> T copy() {
		return (T) new ParcelType(ID, getOrigin().add(0,0,0));
	}
	@Override
	public Size3D getHitBox() {
		return stored_parcels.get(ID).getHitBox();
	}
	@Override
	public int getVolume() {
		return stored_parcels.get(ID).getVolume();
	}
	@Override
	public Point3D[] getPoints() {
		return stored_parcels.get(ID).getPoints();
	}
	@Override
	public Edge3D[] getEdges() {
		return stored_parcels.get(ID).getEdges();
	}
	@Override
	public Plane3D[] getPlanes() {
		return stored_parcels.get(ID).getPlanes();
	}
	@Override
	public Box[] toBoxes(double scale) {
		return stored_parcels.get(ID).toBoxes(scale);
	}
	@Override
	public Point3D[] getOccupiedGrids() {
		return stored_parcels.get(ID).getOccupiedGrids();
	}

	@Override
	public int getLength() {
		return stored_parcels.get(ID).getLength();
	}

	@Override
	public int getWidth() {
		return stored_parcels.get(ID).getWidth();
	}

	@Override
	public int getHeight() {
		return stored_parcels.get(ID).getHeight();
	}
}
