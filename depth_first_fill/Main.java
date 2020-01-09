package depth_first_fill;

import knapsack.parcel.Parcel;

import static knapsack.parcel.Parcels.*;

import knapsack.Knapsack;

public class Main {

	// The parcels and their amounts
	public static Parcel[] parcels = {A, B, C};
	public static Knapsack knapsack = new Knapsack();
	
	public static void main(String[] args) {
		Phase1Algorithm alg = new Phase1Algorithm(knapsack);
		System.out.println("hey");
	}
	
}
