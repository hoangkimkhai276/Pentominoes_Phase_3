package greedy_algorithm;

import java.awt.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javafxdraw.Colors;
import knapsack.Knapsack;
import knapsack.parcel.ParcelCore;
import knapsack.parcel.Parcels;
import knapsack.parcel.PentominoParcel;
import knapsack.parcel.SimpleParcel;

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

	public static final Color[] SELECTED_COLORS = Colors.getRandomColors(3, 0.8f, 0f);


	public static void main(String[] args) {
		int[][] limitsList= {{0, 100, 100}, {0, 500, 500}, {0, 100, 500}, {0, 500, 100}, {0, 0, 500}, {0, 500, 0}, {0, 0, 100}, {0, 100, 0},
				{100, 100, 100}, {100, 500, 500}, {100, 100, 500}, {100, 500, 100}, {100, 0, 500}, {100, 500, 0}, {100, 0, 100},
				{100, 100, 0}, {500, 100, 100}, {500, 500, 500}, {500, 100, 500}, {500, 500, 100}, {500, 0, 500},
				{500, 500, 0}, {500, 0, 100}, {500, 100, 0}, {100, 0, 0}, {500, 0, 0}};
		try {
			FileWriter csvWriter = new FileWriter("PentoTestValue2.csv");
			csvWriter.append("P");
			csvWriter.append(",");
			csvWriter.append("L");
			csvWriter.append(",");
			csvWriter.append("T");
			csvWriter.append(",");
			csvWriter.append("i");
			csvWriter.append(",");
			csvWriter.append("Time");
			csvWriter.append(",");
			csvWriter.append("Value");
			csvWriter.append(",");
			csvWriter.append("Number used");
			csvWriter.append(",");
			csvWriter.append("Volume");
			csvWriter.append("\n");
			for (int ii = 0; ii < 26; ii++) {
				long avgTime = 0;
				long avgValue = 0;
				long avgNumberUsed = 0;
				long avgVolume = 0;
				csvWriter.append(limitsList[ii][0]+"");
				csvWriter.append(",");
				csvWriter.append(limitsList[ii][1]+"");
				csvWriter.append(",");
				csvWriter.append(limitsList[ii][2]+"");
				csvWriter.append(",");
				csvWriter.append(ii+"");
				csvWriter.append(",");

				for (int i = 0; i < 1000; i++) {
					int[] limits = new int[3];

					for (int j = 0; j < 3; j++) {
						limits[j] = limitsList[ii][j];
					}
					ParcelCore P = new PentominoParcel(new boolean[][]{{true, false},{true, true}, {true, true}}, limits[0], SELECTED_COLORS[0], "P");
					ParcelCore L = new PentominoParcel(new boolean[][]{{true, true},{true, false},{true, false},{true, false}}, limits[1], SELECTED_COLORS[1], "L");
					ParcelCore T = new PentominoParcel(new boolean[][]{{true, true, true}, {false, true, false}, {false, true, false}}, limits[2], SELECTED_COLORS[2], "T");
					ParcelCore[] PARCELS = {P,L,T};
					Knapsack before = new Knapsack();

					long start = System.nanoTime();
					simpleStochasticGreedy(before, PARCELS, null);

					long delta = System.nanoTime() - start;
					Knapsack after = before;
//					System.out.println("after " + (delta / 1000000d) + "ms, the following result is gathered:");
//					System.out.println("volume filled: " + after.getFilledVolume() + "/" + after.getVolume());
//					System.out.println("total value: " + after.getValue() + "$");
//					System.out.println("parcels used: " + count + "\n");
					avgTime += (delta / 1000000d);
					avgValue += after.getValue();
					avgNumberUsed += count;
					avgVolume += after.getFilledVolume();

				}
				csvWriter.append((avgTime/1000d)+"");
				csvWriter.append(",");
				csvWriter.append((avgValue/1000d)+"");
				csvWriter.append(",");
				csvWriter.append((avgNumberUsed/1000d)+"");
				csvWriter.append(",");
				csvWriter.append((avgVolume/1000d)+"");
				csvWriter.append("\n");

			}
			csvWriter.flush();
			csvWriter.close();
		}
		catch (Exception e){e.printStackTrace();}
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
