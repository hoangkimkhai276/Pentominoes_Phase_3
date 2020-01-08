package depth_first_fill;

import java.util.ArrayList;
import java.util.Arrays;

import knapsack.Knapsack;
import knapsack.parcel.Parcel;
import knapsack.parcel.ParcelType;
import knapsack.parcel.Parcels;

public class Phase1Algorithm {

	public Knapsack knapsack;
	public ParcelType[] parcel_types;
	
	public Phase1Algorithm(Knapsack knapsack, Parcel... parcels) {
		this.knapsack = knapsack;
		ArrayList<Parcel> permutations = new ArrayList<Parcel>(parcels.length * 24);
		for (Parcel parcel : parcels) permutations.addAll(Arrays.asList(Parcels.createParcelPermutations(parcel)));
		ArrayList<Parcel> filtered = new ArrayList<Parcel>(permutations.size());
		for (Parcel parcel : permutations) if (!filtered.contains(parcel)) filtered.add(parcel);
		parcel_types = new ParcelType[filtered.size()];
		for (int i=0; i < filtered.size(); i++) parcel_types[i] = new ParcelType(filtered.get(i));
	}
	
	public static void main(String[] args) {
		Phase1Algorithm algA = new Phase1Algorithm(new Knapsack(), Parcels.A);
		System.out.println("num of A rotations: "+algA.parcel_types.length);
		Phase1Algorithm algB = new Phase1Algorithm(new Knapsack(), Parcels.B);
		System.out.println("num of B rotations: "+algB.parcel_types.length);
		Phase1Algorithm algC = new Phase1Algorithm(new Knapsack(), Parcels.C);
		System.out.println("num of C rotations: "+algC.parcel_types.length);
		Phase1Algorithm algL = new Phase1Algorithm(new Knapsack(), Parcels.L);
		System.out.println("num of L rotations: "+algL.parcel_types.length);
		Phase1Algorithm algP = new Phase1Algorithm(new Knapsack(), Parcels.P);
		System.out.println("num of P rotations: "+algP.parcel_types.length);
		Phase1Algorithm algT = new Phase1Algorithm(new Knapsack(), Parcels.T);
		System.out.println("num of T rotations: "+algT.parcel_types.length);
	}
	
}
