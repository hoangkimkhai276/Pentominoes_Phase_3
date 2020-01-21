package depth_first_fill;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

import knapsack.Size3D;
import knapsack.parcel.Parcels;
import knapsack.parcel.SimpleParcel;

public class OldMiniSimpleParcel {

	@FunctionalInterface
	static abstract interface Picker extends Function<ArrayList<OldMiniSimpleParcel>, OldMiniSimpleParcel> {
		OldMiniSimpleParcel pick(ArrayList<OldMiniSimpleParcel> list);
		
		default OldMiniSimpleParcel apply(ArrayList<OldMiniSimpleParcel> list) { return pick(list); }
	}
	
	private static final int LENGTH = 1;
	private static final int WIDTH = 2;
	private static final int HEIGHT = 4;
	
	public static final OldMiniSimpleParcel NONE = new OldMiniSimpleParcel(0,0,0,0, new int[6], new ArrayList<OldMiniSimpleParcel>());
	public static final OldMiniSimpleParcel K = new OldMiniSimpleParcel(33,5,8,0);
	public static final OldMiniSimpleParcel A = new OldMiniSimpleParcel(2,2,4,3);
	public static final OldMiniSimpleParcel B = new OldMiniSimpleParcel(2,3,4,4);
	public static final OldMiniSimpleParcel C = new OldMiniSimpleParcel(3,3,3,5);
	public static final OldMiniSimpleParcel[] PARCELS = {A, B, C};
	public static final OldMiniSimpleParcel LL = new OldMiniSimpleParcel(1,5,2,6); // L = 3, P = 4, T = 5
	public static final OldMiniSimpleParcel PTP = new OldMiniSimpleParcel(1,5,3,13);
	public static final OldMiniSimpleParcel PP = new OldMiniSimpleParcel(1,5,2,8);
	public static final OldMiniSimpleParcel[] PENTOS = {LL, PTP, PP};
	static {
		A.count_A = 1; B.count_B = 1; C.count_C = 1;
		LL.count_L = 2; PTP.count_P = 2; PTP.count_T = 1; PP.count_P = 2;
	}
	
	public static boolean care_about_rotation_in_equals = true;
	public static double bets_densevolume_densityrequirement = 0.95;
	
	public static final Comparator<OldMiniSimpleParcel> SORT_DENSITY  = Parcels.getComparatorOfFunction(p->Double.valueOf(p.getDensity()), false);
	public static final Comparator<OldMiniSimpleParcel> SORT_VALUE    = Parcels.getComparatorOfFunction(p->Double.valueOf(p.getValue()), false);
	public static final Comparator<OldMiniSimpleParcel> SORT_VOLUME   = Parcels.getComparatorOfFunction(p->Double.valueOf(p.getVolume()), false);
	
	public static final Picker HIGHEST_DENSITY  = p -> { p.sort(SORT_DENSITY); return p.get(0); };
	public static final Picker HIGHEST_VALUE    = p -> { p.sort(SORT_VALUE);   return p.get(0); };
	public static final Picker HIGHEST_VOLUME   = p -> { p.sort(SORT_VOLUME);  return p.get(0); };
	@SuppressWarnings("unchecked")
	public static final Picker BEST_DENSEVOLUME = p -> {
		ArrayList<OldMiniSimpleParcel> list1 = (ArrayList<OldMiniSimpleParcel>) p.clone();
		ArrayList<OldMiniSimpleParcel> list2 = (ArrayList<OldMiniSimpleParcel>) p.clone();
		list1.sort(SORT_DENSITY);
		list2.sort(SORT_VOLUME);
		int index = 0;
		OldMiniSimpleParcel pick = list2.get(index);
		while (pick.getDensity() < bets_densevolume_densityrequirement * list1.get(0).getDensity()) pick = list2.get(++index);
		return pick;
	};
	
	private int length, width, height, value, count, count_A, count_B, count_C, count_P, count_T, count_L;
	private ArrayList<OldMiniSimpleParcel> components;
	
	private int[] getCounts() {
		return new int[] {count_A, count_B, count_C, count_P, count_T, count_L};
	}
	private int[] getCounts(int multiply) {
		return new int[] {count_A * multiply, count_B * multiply, count_C * multiply, count_P * multiply, count_T * multiply, count_L * multiply};
	}
	private int[] getCounts(int[] counts) {
		return new int[] {count_A + counts[0], count_B + counts[1], count_C + counts[2], count_P + counts[3], count_T + counts[4], count_L + counts[5]};
	}
	private int[] subtractCounts(int[] counts) {
		return new int[] {count_A - counts[0], count_B - counts[1], count_C - counts[2], count_P - counts[3], count_T - counts[4], count_L - counts[5]};
	}
	public boolean exceedsLimit(int[] limits) {
		return count_A > limits[0] || count_B > limits[1] || count_C > limits[2] || count_P > limits[3] || count_T > limits[4] || count_L > limits[5];
	}
	
	private void setCounts(int[] counts) {
		count_A = counts[0]; count_B = counts[1]; count_C = counts[2];
		count_P = counts[3]; count_T = counts[4]; count_L = counts[5];
	}
	public OldMiniSimpleParcel(int length, int width, int height, int value) {
		this(length, width, height, value, new int[6]);
	}
	public OldMiniSimpleParcel(int length, int width, int height, int value, int[] counts) {
		this.length = length;
		this.width = width;
		this.height = height;
		this.value = value;
		count = 1;
		components = new ArrayList<OldMiniSimpleParcel>();
		components.add(this);
		setCounts(counts);
	}
	private OldMiniSimpleParcel(int length, int width, int height, int value, int[] counts, ArrayList<OldMiniSimpleParcel> components) {
		this(length, width, height, value, counts);
		this.components.clear();
		this.components.addAll(components);
	}
	@SafeVarargs
	private OldMiniSimpleParcel(int length, int width, int height, int value, int[] counts, ArrayList<OldMiniSimpleParcel>... components) {
		this(length, width, height, value, counts);
		this.components.clear();
		for (ArrayList<OldMiniSimpleParcel> set : components) this.components.addAll(set);
	}
	
	public OldMiniSimpleParcel rotateX() {
		return new OldMiniSimpleParcel(length, height, width, value, getCounts(), components);
	}
	public OldMiniSimpleParcel rotateY() {
		return new OldMiniSimpleParcel(height, width, length, value, getCounts(), components);
	}
	public OldMiniSimpleParcel rotateZ() {
		return new OldMiniSimpleParcel(width, length, height, value, getCounts(), components);
	}
	
	public OldMiniSimpleParcel multiplyX(int count) {
		ArrayList<OldMiniSimpleParcel> components = new ArrayList<OldMiniSimpleParcel>(this.components.size() * count);
		for (int i=0; i < count; i++) components.addAll(this.components);
		return new OldMiniSimpleParcel(length * count, width, height, value * count, getCounts(count), components);
	}
	public OldMiniSimpleParcel multiplyY(int count) {
		ArrayList<OldMiniSimpleParcel> components = new ArrayList<OldMiniSimpleParcel>(this.components.size() * count);
		for (int i=0; i < count; i++) components.addAll(this.components);
		return new OldMiniSimpleParcel(length, width * count, height, value * count, getCounts(count), components);
	}
	public OldMiniSimpleParcel multiplyZ(int count) {
		ArrayList<OldMiniSimpleParcel> components = new ArrayList<OldMiniSimpleParcel>(this.components.size() * count);
		for (int i=0; i < count; i++) components.addAll(this.components);
		return new OldMiniSimpleParcel(length, width, height * count, value * count, getCounts(count), components);
	}
	
	/** creates a low-level copy of this {@code MiniSimpleParcel} */
	@Override
	public OldMiniSimpleParcel clone() {
		return new OldMiniSimpleParcel(length, width, height, value, getCounts(), components);
	}
	public OldMiniSimpleParcel deepClone() {
		OldMiniSimpleParcel clone = this.clone();
		ArrayList<OldMiniSimpleParcel> cloned_components = new ArrayList<OldMiniSimpleParcel>();
		for (OldMiniSimpleParcel component : clone.components) {
			if (component==this) cloned_components.add(clone);
			else {
				cloned_components.add(component.deepClone());
			}
		} clone.components = cloned_components;
		return clone;
	}
	
	public HashSet<OldMiniSimpleParcel> unravelComponents() {
		ArrayList<OldMiniSimpleParcel> all_components = new ArrayList<OldMiniSimpleParcel>();
		for (OldMiniSimpleParcel component : components)
			for (int c=0; c < component.count; c++) {
				if (component.components.size()==1) all_components.add(component);
				else all_components.addAll(component.unravelComponents());
			}
		while (all_components.contains(NONE)) all_components.remove(NONE);
		HashSet<OldMiniSimpleParcel> reduced_components = new HashSet<OldMiniSimpleParcel>();
		reduced_components.addAll(all_components);
		for (OldMiniSimpleParcel parcel : reduced_components) {
			while (all_components.contains(parcel)) {
				all_components.remove(parcel);
				parcel.count++;
			} parcel.count--;
		}
		return reduced_components;
	}
	
	public OldMiniSimpleParcel add(OldMiniSimpleParcel other) {
		if (this.equals(NONE) || other.equals(NONE))
			return new OldMiniSimpleParcel(other.length + length, other.width + width, other.height + height, value + other.value, getCounts(other.getCounts()), this.components, other.components);
		int c = getSimilar(other);
		if (!isSimilar(c)) return null;
		if ((c & LENGTH) == 0) return new OldMiniSimpleParcel(other.length + length, width, height, value + other.value, getCounts(other.getCounts()), this.components, other.components);
		if ((c & WIDTH)  == 0) return new OldMiniSimpleParcel(length, other.width + width, height, value + other.value, getCounts(other.getCounts()), this.components, other.components);
		if ((c & HEIGHT) == 0) return new OldMiniSimpleParcel(length, width, other.height + height, value + other.value, getCounts(other.getCounts()), this.components, other.components);
		return new OldMiniSimpleParcel(other.length + length, width, height, value + other.value, getCounts(other.getCounts()), this.components, other.components);
	}
	
	public OldMiniSimpleParcel subtract(OldMiniSimpleParcel other) {
		if (this.equals(NONE) && !other.equals(NONE)) return null;
		if (other.equals(NONE)) return clone();
		int c = getSimilar(other);
		if (!isSimilar(c)) return null;
		if ((c & LENGTH) == 0) {
			if (other.length > length) return null;
			return new OldMiniSimpleParcel(length - other.length, width, height, value - other.value, subtractCounts(other.getCounts()));
		}
		if ((c & WIDTH)  == 0) {
			if (other.width > width) return null;
			return new OldMiniSimpleParcel(length, width - other.width, height, value - other.value, subtractCounts(other.getCounts()));
		}
		if ((c & HEIGHT) == 0) {
			if (other.height > height) return null;
			return new OldMiniSimpleParcel(length, width, height - other.height, value - other.value, subtractCounts(other.getCounts()));
		}
		return new OldMiniSimpleParcel(length - other.length, width, height, value + other.value, subtractCounts(other.getCounts()), this.components, other.components);
	}
	
	/**
	 * @param other the other miniSimpleParcel that this parcel can be similar to
	 * @param similarCountAndOrder the result from {@link #getSimilarCountAndOrder}
	 * @return a parcel that is a rotated version of the original parcel such that it is similar to {@code other}
	 */
	public OldMiniSimpleParcel rotateUntilSimilar(OldMiniSimpleParcel other, int[] similarCountAndOrder) {
		int[] nlwh = {length, width, height};
		boolean got_length = false, got_width = false, got_height = false;
		int odd = -1;
		int[] sim = similarCountAndOrder;
		part: {
			if (sim[0] < 2) break part;
			for (int i=0; i < 3; i++) {
				if 		(sim[i+1]==LENGTH) { nlwh[i] = length; got_length = true; }
				else if (sim[i+1]==WIDTH)  { nlwh[i] = width;  got_width  = true; }
				else if (sim[i+1]==HEIGHT) { nlwh[i] = height; got_height = true; }
				else odd = i;
			}
			if 		(!got_length) nlwh[odd] = length;
			else if (!got_width)  nlwh[odd] = width;
			else if (!got_height) nlwh[odd] = height;
			//TODO gather from this, what rotations were performed
		}
		return new OldMiniSimpleParcel(nlwh[0], nlwh[1], nlwh[2], value, getCounts(), components);
	}
	
	/**
	 * @param other
	 * @return
	 * @see #rotateUntilSimilar(OldMiniSimpleParcel, int[])
	 */
	public OldMiniSimpleParcel rotateUntilSimilar(OldMiniSimpleParcel other) {
		return rotateUntilSimilar(other, getSimilarCountAndOrder(other));
	}
	
	public boolean isSimilar(OldMiniSimpleParcel other) {
		return isSimilar(getSimilar(other));
	}
	public boolean isSimilar(int c) {
		return c == 3 || c >= 5; 
	}
	public int getSimilar(OldMiniSimpleParcel other) {
		int c = 0;
		if (length==other.length) c |= LENGTH;
		if (width==other.width  ) c |= WIDTH;
		if (height==other.height) c |= HEIGHT;
		return c;
	}
	public boolean canBeSimilar(OldMiniSimpleParcel other) {
		return getSimilarCount(other) >= 2;
	}

	public int getSimilarCount(OldMiniSimpleParcel other) {
		int[] lwh = {length, width, height};
		boolean len = false, wid = false, hei = false;
		int c = 0;
		for (int i=0; i < 3; i++) {
			if 		(other.length == lwh[i] && !len) { c++; len = true; }
			else if (other.width  == lwh[i] && !wid) { c++; wid = true; }
			else if (other.height == lwh[i] && !hei) { c++; hei = true; }
		}
		return c;
	}

	public int[] getSimilarCountAndOrder(OldMiniSimpleParcel other) {
		int[] lwh = {other.length, other.width, other.height};
		int[] result = new int[4];
		boolean len = false, wid = false, hei = false;
		for (int i=0; i < 3; i++) {
			if 		(length == lwh[i] && !len) { result[0]++; result[i+1] = LENGTH; len = true; }
			else if (width  == lwh[i] && !wid) { result[0]++; result[i+1] = WIDTH;  wid = true; }
			else if (height == lwh[i] && !hei) { result[0]++; result[i+1] = HEIGHT; hei = true; }
		}
		return result;
	}

	public boolean sameShape(OldMiniSimpleParcel other) {
		return getSimilarCount(other) == 3;
	}

	public boolean fitsInside(OldMiniSimpleParcel other) {
		int[] lwh_t = {length, width, height};
		int[] lwh_o = {other.length, other.width, other.height};
		Arrays.sort(lwh_t);
		Arrays.sort(lwh_o);
		boolean result = lwh_t[0] <= lwh_o[0] && lwh_t[1] <= lwh_o[1] && lwh_t[2] <= lwh_o[2];
		return result;
	}

	public OldMiniSimpleParcel rotateToFitInside(OldMiniSimpleParcel other) {
		int[] lwh_t = {length, width, height};
		int[] lwh_o = {other.length, other.width, other.height};
		Arrays.sort(lwh_t);
		Arrays.sort(lwh_o);
		boolean fits = lwh_t[0] <= lwh_o[0] && lwh_t[1] <= lwh_o[1] && lwh_t[2] <= lwh_o[2];
		if (!fits) return null;
		for (int j=0; j < 3; j++)
			for (int i=0; i < 3; i++)
				if (lwh_t[i] == lwh_o[j] && i != 0) {
					int temp = lwh_t[j];
					lwh_t[j] = lwh_t[i];
					lwh_t[i] = temp;
				}
		int[] simo = other.getSimilarCountAndOrder(new OldMiniSimpleParcel(lwh_o[0], lwh_o[1], lwh_o[2], other.value, getCounts()));
		int[] res = new int[3];
		for (int i=0; i < 3; i++) {
			if (simo[i+1] == 1) res[0] = lwh_t[i];
			if (simo[i+1] == 2) res[1] = lwh_t[i];
			if (simo[i+1] == 4) res[2] = lwh_t[i];
		}
		return new OldMiniSimpleParcel(res[0], res[1], res[2], value, getCounts(), components);
	}

	public Size3D getHitBox() {
		return new Size3D(length, width, height);
	}

	public int getVolume() {
		return length * width * height;
	}

	public int getValue() {
		return value;
	}

	public double getDensity() {
		return getValue()/((double)getVolume());
	}

	public OldMiniSimpleParcel[] getRotations() {
		if (length==width && width==height) return new OldMiniSimpleParcel[]{this};
		if (length==width || length==height || width==height) return new OldMiniSimpleParcel[]{rotateX(), rotateY(), rotateZ()};
		return new OldMiniSimpleParcel[]{rotateX(), rotateY(), rotateZ(), rotateX().rotateY(), rotateX().rotateZ(), rotateY().rotateZ()};
	}

	public static ArrayList<OldMiniSimpleParcel> getAllRotations(OldMiniSimpleParcel...miniSimpleParcels) {
		ArrayList<OldMiniSimpleParcel> rotations = new ArrayList<OldMiniSimpleParcel>();
		for (OldMiniSimpleParcel parcel : miniSimpleParcels)
			for (OldMiniSimpleParcel subparcel : parcel.getRotations()) rotations.add(subparcel);
		return rotations;
	}

	public static OldMiniSimpleParcel maximizeKnapsackFilling(OldMiniSimpleParcel knapsack, OldMiniSimpleParcel... parcels) {
		ArrayList<OldMiniSimpleParcel> generated = generateSortedPatchworkPool(knapsack, parcels);
		generated.sort(SORT_VOLUME);
		return generated.get(0);
	}

	public static OldMiniSimpleParcel maximizeKnapsackValue(OldMiniSimpleParcel knapsack, Picker picker, OldMiniSimpleParcel... parcels) {
		ArrayList<OldMiniSimpleParcel> generated = generateSortedPatchworkPool(new int[] {25,25,25,100,100,100}, knapsack, parcels);
		generated.sort(SORT_VALUE);

		//TODO implement segmentation of subdivisions and recursive filling of those
		
		return picker.pick(generated);
	}

	public static OldMiniSimpleParcel[][] getSubDivisions(OldMiniSimpleParcel knapsack, OldMiniSimpleParcel placedBlock) {
		OldMiniSimpleParcel[][] result; int subcount = getSubCount(knapsack, placedBlock);
		if (subcount<=0) return new OldMiniSimpleParcel[0][0];
		if (subcount==1) return new OldMiniSimpleParcel[][]{{knapsack.subtract(placedBlock.rotateUntilSimilar(knapsack))}};
		if (subcount==2) {
			result = new OldMiniSimpleParcel[2][2];
			OldMiniSimpleParcel main = placedBlock.rotateToFitInside(knapsack);
			OldMiniSimpleParcel A_part, B_part, C_part;
			int sim = main.getSimilar(knapsack);
			if (sim==LENGTH) {
				A_part = new OldMiniSimpleParcel(main.length, main.width, knapsack.height, 0, main.getCounts()).subtract(main);
				B_part = new OldMiniSimpleParcel(main.length, knapsack.width, main.height, 0, main.getCounts()).subtract(main);
				C_part = new OldMiniSimpleParcel(main.length, knapsack.width - main.width, knapsack.height - main.height, 0);
			} else if (sim==WIDTH) {
				A_part = new OldMiniSimpleParcel(main.length, main.width, knapsack.height, 0, main.getCounts()).subtract(main);
				B_part = new OldMiniSimpleParcel(knapsack.length, main.width, main.height, 0, main.getCounts()).subtract(main);
				C_part = new OldMiniSimpleParcel(knapsack.length - main.length, main.width, knapsack.height - main.height, 0);
			} else {
				A_part = new OldMiniSimpleParcel(main.length, knapsack.width, main.height, 0, main.getCounts()).subtract(main);
				B_part = new OldMiniSimpleParcel(knapsack.length, main.width, main.height, 0, main.getCounts()).subtract(main);
				C_part = new OldMiniSimpleParcel(knapsack.length - main.length, knapsack.width - main.width, main.height, 0);
			}
			result[0][0] = A_part.add(C_part);
			result[0][1] = B_part;
			result[1][0] = A_part;
			result[1][1] = B_part.add(C_part);
		}

		else {
			result = new OldMiniSimpleParcel[6][3];
			OldMiniSimpleParcel main = placedBlock.rotateToFitInside(knapsack);
			OldMiniSimpleParcel A_part, B_part, C_part, D_part, E_part, F_part, G_part;
			A_part = new OldMiniSimpleParcel(knapsack.length - main.length, main.width, main.height, 0); // length
			B_part = new OldMiniSimpleParcel(main.length, knapsack.width - main.width, main.height, 0);  // width
			C_part = new OldMiniSimpleParcel(main.length, main.width, knapsack.height - main.height, 0); // height
			D_part = new OldMiniSimpleParcel(A_part.length, main.width, C_part.height, 0);
			E_part = new OldMiniSimpleParcel(A_part.length, B_part.width, main.height, 0);
			F_part = new OldMiniSimpleParcel(main.length, B_part.width, C_part.height, 0);
			G_part = new OldMiniSimpleParcel(A_part.length, B_part.width, C_part.height, 0);
			OldMiniSimpleParcel A_plane = A_part.add(D_part).add(E_part.add(G_part));
			OldMiniSimpleParcel B_plane = B_part.add(E_part).add(F_part.add(G_part));
			OldMiniSimpleParcel C_plane = C_part.add(F_part).add(D_part.add(G_part));

			result[0][0] = A_plane;
			result[0][1] = B_part.add(F_part);
			result[0][2] = C_part;
			result[1][0] = A_plane;
			result[1][1] = B_part;
			result[1][2] = C_part.add(F_part);

			result[2][0] = A_part.add(D_part);
			result[2][1] = B_plane;
			result[2][2] = C_part;
			result[3][0] = A_part;
			result[3][1] = B_plane;
			result[3][2] = C_part.add(D_part);

			result[4][0] = A_part.add(E_part);
			result[4][1] = B_part;
			result[4][2] = C_plane;
			result[5][0] = A_part;
			result[5][1] = B_part.add(E_part);
			result[5][2] = C_plane;
		}
		return result;
	}

	public static int getSubCount(OldMiniSimpleParcel knapsack, OldMiniSimpleParcel placedBlock) {
		return 3 - knapsack.getSimilarCount(placedBlock);
	}

	public static ArrayList<OldMiniSimpleParcel> generateSortedPatchworkPool(OldMiniSimpleParcel knapsack, OldMiniSimpleParcel...parcels) {
		return generateSortedPatchworkPool(null, knapsack, parcels);
	}

	public static ArrayList<OldMiniSimpleParcel> generateSortedPatchworkPool(int[] countLimits, OldMiniSimpleParcel knapsack, OldMiniSimpleParcel... parcels) {
		boolean ignore_limit = countLimits==null;
		boolean care = care_about_rotation_in_equals;
		care_about_rotation_in_equals = false;
		ArrayList<OldMiniSimpleParcel> patchwork_pool = new ArrayList<OldMiniSimpleParcel>(Arrays.asList(parcels));
		boolean keep_going = true;
		while (keep_going) {
			keep_going = false;
			ArrayList<OldMiniSimpleParcel> to_add = new ArrayList<OldMiniSimpleParcel>();
			for (OldMiniSimpleParcel current : patchwork_pool) {
				putParcels(ignore_limit, countLimits, patchwork_pool, to_add, knapsack, current.multiplyX(2), current.multiplyY(2), current.multiplyZ(2));
				for (OldMiniSimpleParcel parcel : patchwork_pool) {
					if (parcel==current) continue;
					OldMiniSimpleParcel next;
					if (current.isSimilar(parcel)) next = current.add(parcel);
					else if (current.canBeSimilar(parcel)) next = current.rotateUntilSimilar(parcel).add(parcel);
					else continue;
					if (!putParcel(ignore_limit, countLimits, patchwork_pool, to_add, knapsack, next)) continue;
				}
			}
			patchwork_pool.addAll(to_add);
			if (to_add.size() > 0) keep_going = true;
			System.out.println("added "+to_add.size()+" elements");
		}
		care_about_rotation_in_equals = care;
		return patchwork_pool;
	}

	/** @return */
	private static boolean putParcel(boolean ignore_limit, int[] limits, ArrayList<OldMiniSimpleParcel> patchwork_pool, ArrayList<OldMiniSimpleParcel> to_add, OldMiniSimpleParcel knapsack, OldMiniSimpleParcel parcel) {
		if (parcel.fitsInside(knapsack) && (ignore_limit || !parcel.exceedsLimit(limits)) && !patchwork_pool.contains(parcel) && !to_add.contains(parcel)) to_add.add(parcel);
		else return false;
		return true;
	}

	private static void putParcels(boolean ignore_limit, int[] limits, ArrayList<OldMiniSimpleParcel> patchwork_pool, ArrayList<OldMiniSimpleParcel> to_add, OldMiniSimpleParcel knapsack, OldMiniSimpleParcel...parcels) {
		for (OldMiniSimpleParcel parcel : parcels) putParcel(ignore_limit, limits, patchwork_pool, to_add, knapsack, parcel);
	}

	public static void main(String[] args) {
		test2();
	}

	public static void test2() {
		OldMiniSimpleParcel result = NONE;
		OldMiniSimpleParcel knapsack = K;
		OldMiniSimpleParcel[] parcels = PARCELS;
		bets_densevolume_densityrequirement = 0.95;
		System.out.println("Maximizing "+knapsack+" with "+Arrays.toString(parcels)+" for total value");
		long start_time = System.nanoTime();
		result = maximizeKnapsackValue(knapsack, BEST_DENSEVOLUME, parcels);
		long delta_time = System.nanoTime() - start_time;
		System.out.println("calculation took "+(float)(delta_time/1000000d)+"ms");
		System.out.println("result value = "+result.getValue()+"\nresult = "+result.unravelComponents()+" for a total of "+result
				+ " with a density of "+result.getDensity()+" made from "+result.counts());
		System.out.println("filled volume = "+result.getVolume()+"/"+knapsack.getVolume());
		System.out.println("amount of sub sections = "+getSubCount(knapsack, result));
	}

	public static void test1() {
		OldMiniSimpleParcel result;
		OldMiniSimpleParcel knapsack = new OldMiniSimpleParcel(33,5,8,0);
		OldMiniSimpleParcel[] parcels = new OldMiniSimpleParcel[] {PTP, PP, LL};
		System.out.println("Filling "+knapsack+" with "+Arrays.toString(parcels));
		long start_time = System.nanoTime();
		result = maximizeKnapsackFilling(knapsack, parcels);
		long delta_time = System.nanoTime() - start_time;
		System.out.println("calculation took "+(float)(delta_time/1000000d)+"ms");
		System.out.println("It found the best solution:\n"+result.unravelComponents()+" for a total of "+result+" made from "+result.counts());
	}

	@Override
	public String toString() {
		String string = "";
		if (count > 1) string = count+"x ";
		string += "("+length+"x"+width+"x"+height+")";
		if (value > 0) string += "$"+value;
		return string;
	}

	@Override
	public boolean equals(Object o) {
		if (this==o) return true;
		if (!(o instanceof OldMiniSimpleParcel)) return false;
		OldMiniSimpleParcel other = (OldMiniSimpleParcel)o;
		if (care_about_rotation_in_equals) return length==other.length && width == other.width && height == other.height;
		return sameShape(other);
	}

	public String counts() {
		return "A: "+count_A+"x, B: "+count_B+"x, C: "+count_C+"x, P: "+count_P+"x, T: "+count_T+"x, L: "+count_L+"x";
	}
	
}