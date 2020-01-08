package depth_first_fill;

import knapsack.Knapsack;
import knapsack.parcel.ParcelType;

public class Phase1Algorithm {

	public Knapsack knapsack;
	
	public Phase1Algorithm(Knapsack knapsack, ParcelData...parcels) {
		this.knapsack = knapsack;
	}
	
}

class ParcelData {
	public ParcelType ptype;
	public int count;
	public ParcelData(ParcelType ptype, int count) {
		this.ptype = ptype;
		this.count = count;
	}
}
