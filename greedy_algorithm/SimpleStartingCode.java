package greedy_algorithm;

import java.util.ArrayList;
import java.util.Arrays;

import knapsack.Knapsack;
import knapsack.parcel.ParcelCore;
import knapsack.parcel.Parcels;

public class SimpleStartingCode {

	public static Knapsack simpleGreedy(Knapsack _knapsack, ParcelCore... parcels) {
		Knapsack knapsack = _knapsack.getEmpty();
		ParcelCore[] _rotations = Parcels.createParcelPermutations(parcels);
		ArrayList<ParcelCore> rotations = new ArrayList<ParcelCore>(Arrays.asList(_rotations));
		Parcels.sortByDensity(rotations);
		for (int x=0; x < knapsack.getLength(); x++)
			for (int y=0; y < knapsack.getWidth(); y++)
				for (int z=0; z < knapsack.getHeight(); z++) {
					parcels: for (ParcelCore parcel : rotations) {
						ParcelCore copy = parcel.copy();
						copy.setOrigin(x, y, z);
						if (knapsack.putParcel(copy)) break parcels;
					}
				}
		return knapsack;
	}
	
	public static void main(String[] args) {
		Knapsack before = new Knapsack();
		long start = System.nanoTime();
		Knapsack after = simpleGreedy(before, Parcels.PENTOS);
		long delta = System.nanoTime() - start;
		System.out.println("after "+(delta/1000000d)+"ms, the following result is gathered:");
		System.out.println("volume filled: "+after.getFilledVolume()+"/"+after.getVolume());
		System.out.println("parcels used: "+(after.getVolume()/5));
	}
}
