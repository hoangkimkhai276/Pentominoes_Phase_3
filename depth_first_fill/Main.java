package depth_first_fill;

import knapsack.parcel.Parcel;
import knapsack.parcel.ParcelType;

import static knapsack.parcel.Parcels.*;

import knapsack.Knapsack;

public class Main {

	// The parcels and their amounts
	public static Parcel[] parcels = {A, B, C};
	public static int[] amounts = 	 {10,10,10};
	public static Knapsack knapsack = new Knapsack();
	
	public static ParcelData[] generateData(Parcel[] parcels, int[] amounts) throws IndexOutOfBoundsException {
		if (parcels.length != amounts.length) throw new IndexOutOfBoundsException("parcels and amounts differ in length");
		ParcelData[] result = new ParcelData[parcels.length];
		for (int i=0; i < result.length; i++)
			result[i] = new ParcelData(new ParcelType(parcels[i]), amounts[i]);
		return result;
	}
	
	public static void main(String[] args) {
		Phase1Algorithm alg = new Phase1Algorithm(knapsack);
	}
	
}
