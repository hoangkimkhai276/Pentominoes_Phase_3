package knapsack.parcel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Function;

import graphics.Colors;

public final class Parcels {
	
	public static final Color[] SELECTED_COLORS = Colors.getRandomColors(6, 0.8f, 0f);
	
	public static final Parcel A = new SimpleParcel(2, 2, 4, 3, SELECTED_COLORS[0]);
	public static final Parcel B = new SimpleParcel(2, 3, 4, 4, SELECTED_COLORS[1]);
	public static final Parcel C = new SimpleParcel(3, 3, 3, 5, SELECTED_COLORS[2]);
	public static final Parcel L = new PentominoParcel(new boolean[][]{{true, true},{true, false},{true, false},{true, false}}, 3, SELECTED_COLORS[3]);
	public static final Parcel P = new PentominoParcel(new boolean[][]{{true, false},{true, true}, {true, true}}, 4, SELECTED_COLORS[4]);
	public static final Parcel T = new PentominoParcel(new boolean[][]{{true, true, true}, {false, true, false}, {false, true, false}}, 5, SELECTED_COLORS[5]);
	
	public static final Function<Parcel, Double> DISTANCE_SORT = a-> {return Double.valueOf(a.getOrigin().magnitude());};
	public static final Function<Parcel, Double> VALUE_SORT = a-> {return Double.valueOf(a.getValue());};
	public static final Function<Parcel, Double> VOLUME_SORT = a-> {return Double.valueOf(a.getVolume());};
	
	public static void sortByFunction(ArrayList<Parcel> parcels, Function<Parcel, Double> function) {
		parcels.sort(getComparatorOfFunction(function));
	}
	public static void sortByDistance(ArrayList<Parcel> parcels) {
		sortByFunction(parcels, DISTANCE_SORT);
	}
	public static void sortByValue(ArrayList<Parcel> parcels) {
		sortByFunction(parcels, VALUE_SORT);
	}
	public static void sortByVolume(ArrayList<Parcel> parcels) {
		sortByFunction(parcels, VOLUME_SORT);
	}
	
	public static Comparator<Parcel> getComparatorOfFunction(Function<Parcel, Double> function) {
		return (a,b)->{
			double ad = function.apply(a);
			double bd = function.apply(b);
			if (ad  > bd) return 1;
			if (ad == bd) return 0;
			return -1;
		};
	}
	
}
