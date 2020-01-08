package knapsack.parcel;

import java.util.ArrayList;

/**
 * The {@code ParcelType} class is a wrapper for {@link Parcel} objects which makes comparing two parcels much more efficient by
 *  giving each different parcel an ID {@code byte}.<br>
 * When the {@code ParcelType} is created, it uses the {@link #equals} method of {@link Parcel} to check the unique parcels. 
 *  Then during calculations, only the checking of the {@code ID} is required to compare two parcels, giving more efficient computations
 */
public class ParcelType {

	private static ArrayList<Parcel> stored_parcels = new ArrayList<Parcel>();
	private static ArrayList<Byte> stored_IDs = new ArrayList<Byte>();
	public static byte last_ID = Byte.MIN_VALUE;
	
	public final Parcel parcel;
	private final byte ID;
	
	public ParcelType(Parcel parcel) {
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
	
}
