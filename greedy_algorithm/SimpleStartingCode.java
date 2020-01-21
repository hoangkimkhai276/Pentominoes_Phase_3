package greedy_algorithm;

import java.util.ArrayList;
import java.util.Arrays;

import knapsack.Knapsack;
import knapsack.parcel.ParcelCore;
import knapsack.parcel.Parcels;

public class SimpleStartingCode {
	
	public static int count;

	/**
	 * 
	 * @param knapsack the knapsack to fill, doesn't get mutated
	 * @param parcels the parcels to use, doesn't get mutated
	 * @param limits the maximum amount of parcels allowed per type of parcel (sorted in the order of {@code parcels}), set to <b>{@code null}</b>
	 *         to solve the <b>unbounded</b> knapsack problem
	 * @return the modified knapsack, filled with the given parcels
	 */
	public static void simpleGreedy(Knapsack knapsack, ParcelCore[] parcels, int[] limits) {
		count = 0;
		boolean ignore_limit = limits==null;
		ParcelCore[] _rotations = Parcels.createParcelPermutations(parcels);
		ArrayList<ParcelCore> rotations = new ArrayList<ParcelCore>(Arrays.asList(_rotations));
		Parcels.sortByDensity(rotations);
		for (int x=0; x < knapsack.getLength(); x++)
			for (int y=0; y < knapsack.getWidth(); y++)
				for (int z=0; z < knapsack.getHeight(); z++) {
					parcels: for (ParcelCore parcel : rotations) {
						if (!ignore_limit && limits[indexOf(parcel, parcels)] <= 0) continue;
						ParcelCore copy = parcel.copy();
						copy.setOrigin(x, y, z);
						if (knapsack.putParcel(copy)) {
							count++;
							if (!ignore_limit) limits[indexOf(parcel, parcels)]--;
							break parcels;
						}
					}
				}
	}
	
	public static void simpleStochasticGreedy(Knapsack knapsack, ParcelCore[] parcels, int[] limits) {
		count = 0;
		boolean ignore_limit = limits==null;
		ParcelCore[][] parcel_rotations = Parcels.createLayeredParcelPermutations(parcels);
		ArrayList<ArrayList<ParcelCore>> rotations = new ArrayList<ArrayList<ParcelCore>>();
		for (ParcelCore[] list : parcel_rotations) rotations.add(new ArrayList<ParcelCore>(Arrays.asList(list)));
		rotations.sort(Parcels.getComparatorOfFunction(l -> {return l.get(0).getDensity();}, false));
		for (int x=0; x < knapsack.getLength(); x++)
			for (int y=0; y < knapsack.getWidth(); y++)
				for (int z=0; z < knapsack.getHeight(); z++) {
					parcels: for (ArrayList<ParcelCore> parcel_list : rotations) {
						parcel_list.sort(Parcels.getComparatorOfFunction(a -> Math.random(), true));
						for (ParcelCore parcel : parcel_list) {
							if (!ignore_limit && limits[indexOf(parcel, parcels)] <= 0) continue;
							ParcelCore copy = parcel.copy();
							copy.setOrigin(x, y, z);
							if (knapsack.putParcel(copy)) {
								count++;
								if (!ignore_limit) limits[indexOf(parcel, parcels)]--;
								break parcels;
							}
						}
					}
				}
	}
	
	public static int indexOf(ParcelCore parcel, ParcelCore[] parcels) {
		for (int i=0; i < parcels.length; i++) if (parcel.isSameType(parcels[i])) return i;
		return -1;
	}
	
	public static void main(String[] args) {
		Knapsack before = new Knapsack();
		long start = System.nanoTime();
		simpleStochasticGreedy(before, Parcels.PENTOS, null);
		long delta = System.nanoTime() - start;
		Knapsack after = before;
		System.out.println("after "+(delta/1000000d)+"ms, the following result is gathered:");
		System.out.println("volume filled: "+after.getFilledVolume()+"/"+after.getVolume());
		System.out.println("total value: "+after.getValue()+"$");
		System.out.println("parcels used: "+count+"\n");
		
		/*int[] starting_limits = {50,40,200}; // P L T
		int[] limits = Arrays.copyOf(starting_limits, 3);
		start = System.nanoTime();
		after = simpleGreedy(before, Parcels.PENTOS, limits);
		delta = System.nanoTime() - start;
		System.out.println("after "+(delta/1000000d)+"ms, the following result is gathered:");
		System.out.println("volume filled: "+after.getFilledVolume()+"/"+after.getVolume());
		System.out.println("total value: "+after.getValue()+"$");
		System.out.println("parcels used: "+count);
		System.out.println("amount of P used: "+(starting_limits[0]-limits[0])+"/"+starting_limits[0]);
		System.out.println("amount of L used: "+(starting_limits[1]-limits[1])+"/"+starting_limits[1]);
		System.out.println("amount of T used: "+(starting_limits[2]-limits[2])+"/"+starting_limits[2]); */
	}
}
