package knapsack.parcel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import graphics.Colors;
import knapsack.Knapsack;

public final class Parcels {
	
	public static final Color[] SELECTED_COLORS = Colors.getRandomColors(6, 0.8f, 0f);
	
	public static final ParcelCore A = new SimpleParcel(2, 2, 4, 3, SELECTED_COLORS[0]);
	public static final ParcelCore B = new SimpleParcel(2, 3, 4, 4, SELECTED_COLORS[1]);
	public static final ParcelCore C = new SimpleParcel(3, 3, 3, 5, SELECTED_COLORS[2]);
	public static final ParcelCore L = new PentominoParcel(new boolean[][]{{true, true},{true, false},{true, false},{true, false}}, 3, SELECTED_COLORS[3]);
	public static final ParcelCore P = new PentominoParcel(new boolean[][]{{true, false},{true, true}, {true, true}}, 4, SELECTED_COLORS[4]);
	public static final ParcelCore T = new PentominoParcel(new boolean[][]{{true, true, true}, {false, true, false}, {false, true, false}}, 5, SELECTED_COLORS[5]);
	
	public static final Function<Parcel, Double> DISTANCE_SORT = a-> {return Double.valueOf(a.getOrigin().magnitude());};
	public static final Function<Parcel, Double> VALUE_SORT = a-> {return -Double.valueOf(a.getValue());};
	public static final Function<Parcel, Double> VOLUME_SORT = a-> {return -Double.valueOf(a.getVolume());};
	public static final Function<Parcel, Double> DENSITY_SORT = a-> {return -Double.valueOf(a.getDensity());};
	
	public static <T> void sortByFunction(List<T> items, Function<? super T, Double> function) {
		items.sort(getComparatorOfFunction(function));
	}
	public static void sortByDistance(List<? extends Parcel> parcels) {
		sortByFunction(parcels, DISTANCE_SORT);
	}
	public static void sortByValue(List<? extends Parcel> arrayList) {
		sortByFunction(arrayList, VALUE_SORT);
	}
	public static void sortByVolume(List<? extends Parcel> parcels) {
		sortByFunction(parcels, VOLUME_SORT);
	}
	public static void sortByDensity(List<? extends Parcel> parcels) {
		sortByFunction(parcels, DENSITY_SORT);
	}
	
	public static void main(String[] args) {
		List<SimpleParcel> parcels = Arrays.asList(randomSimpleParcels(new Knapsack(), 0.05, 100));
		Parcels.sortByDensity(parcels);
		System.out.println(parcels);
	}
	
	public static <T> Comparator<T> getComparatorOfFunction(Function<T, Double> function) {
		return (a,b)->{
			double ad = function.apply(a);
			double bd = function.apply(b);
			if (ad  > bd) return 1;
			if (ad == bd) return 0;
			return -1;
		};
	}
	
	public static SimpleParcel randomSimpleParcel(double mean_length, double mean_width, double mean_height, double mean_value, Random rand) {
		double stdv = 2.0;
		int length = (int)(((rand.nextGaussian() * stdv + mean_length) + (rand.nextGaussian() * mean_length + mean_length + stdv))/2d); if (length < 1) length = 1;
		int width = (int)(((rand.nextGaussian() * stdv + mean_width) + (rand.nextGaussian() * mean_width + mean_width + stdv))/2d); if (width < 1) width = 1;
		int height = (int)(((rand.nextGaussian() * stdv + mean_height) + (rand.nextGaussian() * mean_height + mean_height + stdv))/2d); if (height < 1) height = 1;
		int value = (int)(((rand.nextGaussian() * stdv + mean_value) + (rand.nextGaussian() * mean_value + mean_value + stdv))/2d - 0.5); if (value < 1) value = 1;
		return new SimpleParcel(length, width, height, value);
	}
	
	public static SimpleParcel[] randomSimpleParcels(Knapsack knapsack, double desired_fill_rate, int desired_amount) {
		SimpleParcel[] result = new SimpleParcel[desired_amount];
		double adj_num = desired_amount * desired_fill_rate;
		double mean_length = knapsack.getLength()/adj_num;
		double mean_width = knapsack.getWidth()/adj_num;
		double mean_height = knapsack.getHeight()/adj_num;
		double mean_value = knapsack.getVolume() / (adj_num*adj_num*adj_num); // it is equal to the average volume of a parcel in the set
		Random random = new Random();
		for (int i=0; i < result.length; i++) result[i] = randomSimpleParcel(mean_length, mean_width, mean_height, mean_value, random);
		return result;
	}
	
	public static ParcelCore[] createParcelPermutations(ParcelCore parcel) {
		ArrayList<ParcelCore> permutations = new ArrayList<ParcelCore>();
		for (int i=0; i < 4; i++) {
			for (int j=0; j < 4; j++) {
				for (int k=0; k < 4; k++) {
					permutations.add(parcel.copy());
					parcel.rotateHeight();
				}
				parcel.rotateWidth();
			}
			parcel.rotateLength();
		}
		ArrayList<ParcelCore> list = new ArrayList<ParcelCore>();
		for (ParcelCore p : permutations)
			if (!list.contains(p)) list.add(p);
		ParcelCore[] result = new ParcelCore[list.size()];
		for (int i=0; i < result.length; i++) result[i] = list.get(i);
		return result;
	}
	
	public static ParcelCore[] createParcelPermutations(ParcelCore[] parcels) {
		ArrayList<ParcelCore> permutations = new ArrayList<ParcelCore>(parcels.length * 24);
		for (ParcelCore parcel : parcels) permutations.addAll(Arrays.asList(Parcels.createParcelPermutations(parcel)));
		ArrayList<ParcelCore> filtered = new ArrayList<ParcelCore>(permutations.size());
		for (ParcelCore parcel : permutations) if (!filtered.contains(parcel)) filtered.add(parcel);
		ParcelCore[] result = new ParcelCore[filtered.size()];
		for (int i=0; i < filtered.size(); i++) result[i] = filtered.get(i);
		return result;
	}
	
}
