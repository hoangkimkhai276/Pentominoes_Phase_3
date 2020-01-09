package depth_first_fill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import knapsack.Knapsack;
import knapsack.parcel.ParcelCore;
import knapsack.parcel.ParcelType;
import knapsack.parcel.Parcels;

public class Phase1Algorithm {

	public Knapsack knapsack;
	public ParcelType[] parcel_types;
	
	public Phase1Algorithm(Knapsack knapsack, ParcelCore... parcels) {
		this.knapsack = knapsack;
		parcel_types = makePermutationTypes(parcels);
	}
	
	private ParcelType[] makePermutationTypes(ParcelCore...parcels) {
		ParcelType[] result;
		ArrayList<ParcelCore> permutations = new ArrayList<ParcelCore>(parcels.length * 24);
		for (ParcelCore parcel : parcels) permutations.addAll(Arrays.asList(Parcels.createParcelPermutations(parcel)));
		ArrayList<ParcelCore> filtered = new ArrayList<ParcelCore>(permutations.size());
		for (ParcelCore parcel : permutations) if (!filtered.contains(parcel)) filtered.add(parcel);
		result = new ParcelType[filtered.size()];
		for (int i=0; i < filtered.size(); i++) result[i] = new ParcelType(filtered.get(i));
		return result;
	}
	
	public Optional<Knapsack> fillKnapsack() {
		Knapsack result = null;
		
		// TODO filling problem
		
		return Optional.ofNullable(result);
	}
	
}
