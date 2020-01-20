package depth_first_fill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;

import knapsack.Size3D;
import knapsack.parcel.Parcels;

public class MiniSimpleParcel {

	@FunctionalInterface
	static abstract interface Picker extends Function<ArrayList<MiniSimpleParcel>, MiniSimpleParcel> {
		MiniSimpleParcel pick(ArrayList<MiniSimpleParcel> list);
		
		default MiniSimpleParcel apply(ArrayList<MiniSimpleParcel> list) { return pick(list); }
	}
	
	private static final int LENGTH = 1;
	private static final int WIDTH = 2;
	private static final int HEIGHT = 4;
	
	public static final MiniSimpleParcel NONE = new MiniSimpleParcel(0,0,0,0, new ArrayList<MiniSimpleParcel>());
	public static final MiniSimpleParcel K = new MiniSimpleParcel(33,5,8,0);
	public static final MiniSimpleParcel A = new MiniSimpleParcel(2,2,4,3);
	public static final MiniSimpleParcel B = new MiniSimpleParcel(2,3,4,4);
	public static final MiniSimpleParcel C = new MiniSimpleParcel(3,3,3,5);
	public static final MiniSimpleParcel[] PARCELS = {A, B, C};
	public static final MiniSimpleParcel LL = new MiniSimpleParcel(1,5,2,6); // L = 3, P = 4, T = 5
	public static final MiniSimpleParcel PTP = new MiniSimpleParcel(1,5,3,13);
	public static final MiniSimpleParcel PP = new MiniSimpleParcel(1,5,2,8);
	public static final MiniSimpleParcel[] PENTOS = {LL, PTP, PP};
	
	public static boolean care_about_rotation_in_equals = true;
	public static double bets_densevolume_densityrequirement = 0.95;
	
	public static final Comparator<MiniSimpleParcel> SORT_DENSITY  = Parcels.getComparatorOfFunction(p->Double.valueOf(p.getDensity()), false);
	public static final Comparator<MiniSimpleParcel> SORT_VALUE    = Parcels.getComparatorOfFunction(p->Double.valueOf(p.getValue()), false);
	public static final Comparator<MiniSimpleParcel> SORT_VOLUME   = Parcels.getComparatorOfFunction(p->Double.valueOf(p.getVolume()), false);
	
	public static final Picker HIGHEST_DENSITY  = p -> { p.sort(SORT_DENSITY); return p.get(0); };
	public static final Picker HIGHEST_VALUE    = p -> { p.sort(SORT_VALUE);   return p.get(0); };
	public static final Picker HIGHEST_VOLUME   = p -> { p.sort(SORT_VOLUME);  return p.get(0); };
	@SuppressWarnings("unchecked")
	public static final Picker BEST_DENSEVOLUME = p -> {
		ArrayList<MiniSimpleParcel> list1 = (ArrayList<MiniSimpleParcel>) p.clone();
		ArrayList<MiniSimpleParcel> list2 = (ArrayList<MiniSimpleParcel>) p.clone();
		list1.sort(SORT_DENSITY);
		list2.sort(SORT_VOLUME);
		int index = 0;
		MiniSimpleParcel pick = list2.get(index);
		while (pick.getDensity() < bets_densevolume_densityrequirement * list1.get(0).getDensity()) pick = list2.get(++index);
		return pick;
	};
	
	private int length, width, height, value, count;
	private ArrayList<MiniSimpleParcel> components;
	
	public MiniSimpleParcel(int length, int width, int height, int value) {
		this.length = length;
		this.width = width;
		this.height = height;
		this.value = value;
		count = 1;
		components = new ArrayList<MiniSimpleParcel>();
		components.add(this);
	}
	private MiniSimpleParcel(int length, int width, int height, int value, ArrayList<MiniSimpleParcel> components) {
		this(length, width, height, value);
		this.components.clear();
		this.components.addAll(components);
	}
	@SafeVarargs
	private MiniSimpleParcel(int length, int width, int height, int value, ArrayList<MiniSimpleParcel>... components) {
		this(length, width, height, value);
		this.components.clear();
		for (ArrayList<MiniSimpleParcel> set : components) this.components.addAll(set);
	}
	
	public MiniSimpleParcel rotateX() {
		return new MiniSimpleParcel(length, height, width, value, components);
	}
	public MiniSimpleParcel rotateY() {
		return new MiniSimpleParcel(height, width, length, value, components);
	}
	public MiniSimpleParcel rotateZ() {
		return new MiniSimpleParcel(width, length, height, value, components);
	}
	
	public MiniSimpleParcel multiplyX(int count) {
		ArrayList<MiniSimpleParcel> components = new ArrayList<MiniSimpleParcel>(this.components.size() * count);
		for (int i=0; i < count; i++) components.addAll(this.components);
		return new MiniSimpleParcel(length * count, width, height, value * count, components);
	}
	public MiniSimpleParcel multiplyY(int count) {
		ArrayList<MiniSimpleParcel> components = new ArrayList<MiniSimpleParcel>(this.components.size() * count);
		for (int i=0; i < count; i++) components.addAll(this.components);
		return new MiniSimpleParcel(length, width * count, height, value * count, components);
	}
	public MiniSimpleParcel multiplyZ(int count) {
		ArrayList<MiniSimpleParcel> components = new ArrayList<MiniSimpleParcel>(this.components.size() * count);
		for (int i=0; i < count; i++) components.addAll(this.components);
		return new MiniSimpleParcel(length, width, height * count, value * count, components);
	}
	
	/** creates a low-level copy of this {@code MiniSimpleParcel} */
	@Override
	public MiniSimpleParcel clone() {
		return new MiniSimpleParcel(length, width, height, value, components);
	}
	public MiniSimpleParcel deepClone() {
		MiniSimpleParcel clone = this.clone();
		ArrayList<MiniSimpleParcel> cloned_components = new ArrayList<MiniSimpleParcel>();
		for (MiniSimpleParcel component : clone.components) {
			if (component==this) cloned_components.add(clone);
			else {
				cloned_components.add(component.deepClone());
			}
		} clone.components = cloned_components;
		return clone;
	}
	
	public HashSet<MiniSimpleParcel> unravelComponents() {
		ArrayList<MiniSimpleParcel> all_components = new ArrayList<MiniSimpleParcel>();
		for (MiniSimpleParcel component : components)
			for (int c=0; c < component.count; c++) {
				if (component.components.size()==1) all_components.add(component);
				else all_components.addAll(component.unravelComponents());
			}
		while (all_components.contains(NONE)) all_components.remove(NONE);
		HashSet<MiniSimpleParcel> reduced_components = new HashSet<MiniSimpleParcel>();
		reduced_components.addAll(all_components);
		for (MiniSimpleParcel parcel : reduced_components) {
			while (all_components.contains(parcel)) {
				all_components.remove(parcel);
				parcel.count++;
			} parcel.count--;
		}
		return reduced_components;
	}
	
	public MiniSimpleParcel add(MiniSimpleParcel other) {
		if (this.equals(NONE) || other.equals(NONE))
			return new MiniSimpleParcel(other.length + length, other.width + width, other.height + height, value + other.value, this.components, other.components);
		int c = getSimilar(other);
		if (!isSimilar(c)) return null;
		if ((c & LENGTH) == 0) return new MiniSimpleParcel(other.length + length, width, height, value + other.value, this.components, other.components);
		if ((c & WIDTH)  == 0) return new MiniSimpleParcel(length, other.width + width, height, value + other.value, this.components, other.components);
		if ((c & HEIGHT) == 0) return new MiniSimpleParcel(length, width, other.height + height, value + other.value, this.components, other.components);
		return new MiniSimpleParcel(other.length + length, width, height, value + other.value, this.components, other.components);
	}
	
	public MiniSimpleParcel subtract(MiniSimpleParcel other) {
		if (this.equals(NONE) && !other.equals(NONE)) return null;
		if (other.equals(NONE)) return clone();
		int c = getSimilar(other);
		if (!isSimilar(c)) return null;
		if ((c & LENGTH) == 0) {
			if (other.length > length) return null;
			return new MiniSimpleParcel(length - other.length, width, height, value - other.value);
		}
		if ((c & WIDTH)  == 0) {
			if (other.width > width) return null;
			return new MiniSimpleParcel(length, width - other.width, height, value - other.value);
		}
		if ((c & HEIGHT) == 0) {
			if (other.height > height) return null;
			return new MiniSimpleParcel(length, width, height - other.height, value - other.value);
		}
		return new MiniSimpleParcel(length - other.length, width, height, value + other.value, this.components, other.components);
	}
	
	/**
	 * @param other the other miniSimpleParcel that this parcel can be similar to
	 * @param similarCountAndOrder the result from {@link #getSimilarCountAndOrder}
	 * @return a parcel that is a rotated version of the original parcel such that it is similar to {@code other}
	 */
	public MiniSimpleParcel rotateUntilSimilar(MiniSimpleParcel other, int[] similarCountAndOrder) {
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
		}
		return new MiniSimpleParcel(nlwh[0], nlwh[1], nlwh[2], value, components);
	}
	
	/**
	 * @param other
	 * @return
	 * @see #rotateUntilSimilar(MiniSimpleParcel, int[])
	 */
	public MiniSimpleParcel rotateUntilSimilar(MiniSimpleParcel other) {
		return rotateUntilSimilar(other, getSimilarCountAndOrder(other));
	}
	
	public boolean isSimilar(MiniSimpleParcel other) {
		return isSimilar(getSimilar(other));
	}
	public boolean isSimilar(int c) {
		return c == 3 || c >= 5; 
	}
	public int getSimilar(MiniSimpleParcel other) {
		int c = 0;
		if (length==other.length) c |= LENGTH;
		if (width==other.width  ) c |= WIDTH;
		if (height==other.height) c |= HEIGHT;
		return c;
	}
	public boolean canBeSimilar(MiniSimpleParcel other) {
		return getSimilarCount(other) >= 2;
	}
	public int getSimilarCount(MiniSimpleParcel other) {
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
	public int[] getSimilarCountAndOrder(MiniSimpleParcel other) {
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
	public boolean sameShape(MiniSimpleParcel other) {
		return getSimilarCount(other) == 3;
	}
	public boolean fitsInside(MiniSimpleParcel other) {
		int[] lwh_t = {length, width, height};
		int[] lwh_o = {other.length, other.width, other.height};
		Arrays.sort(lwh_t);
		Arrays.sort(lwh_o);
		boolean result = lwh_t[0] <= lwh_o[0] && lwh_t[1] <= lwh_o[1] && lwh_t[2] <= lwh_o[2];
		return result;
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
	
	public MiniSimpleParcel[] getRotations() {
		if (length==width && width==height) return new MiniSimpleParcel[]{this};
		if (length==width || length==height || width==height) return new MiniSimpleParcel[]{rotateX(), rotateY(), rotateZ()};
		return new MiniSimpleParcel[]{rotateX(), rotateY(), rotateZ(), rotateX().rotateY(), rotateX().rotateZ(), rotateY().rotateZ()};
	}
	
	public static ArrayList<MiniSimpleParcel> getAllRotations(MiniSimpleParcel...miniSimpleParcels) {
		ArrayList<MiniSimpleParcel> rotations = new ArrayList<MiniSimpleParcel>();
		for (MiniSimpleParcel parcel : miniSimpleParcels)
			for (MiniSimpleParcel subparcel : parcel.getRotations()) rotations.add(subparcel);
		return rotations;
	}
	
	public static Optional<StoredMini> canBeFilled(MiniSimpleParcel knapsack, MiniSimpleParcel... parcels) {
		ArrayList<MiniSimpleParcel> rotations = getAllRotations(parcels);
		StoredMini awnser = new StoredMini(NONE);
		boolean result = recursion(awnser, null, knapsack, rotations, new HashSet<MiniSimpleParcel>(), knapsack.getVolume());
		if (result) return Optional.of(awnser);
		return Optional.empty();
	}
	
	private static boolean recursion(StoredMini result, MiniSimpleParcel current, MiniSimpleParcel knapsack, ArrayList<MiniSimpleParcel> rotations, HashSet<MiniSimpleParcel> found, int desired_volume) {
		if (current==null)
			return recursion(result, NONE, knapsack, rotations, found, desired_volume);
		int size = rotations.size();
		for (int i=0; i < size; i++) {
			MiniSimpleParcel next = current.add(rotations.get(i));
			if (next==null) continue;
			if (!next.fitsInside(knapsack)) continue;
			if (found.contains(next)) continue;
			found.add(next);
			if (!rotations.contains(next)) rotations.add(next);
			result.parcel = next;
			if (next.getVolume() == desired_volume ||
					recursion(result, next, knapsack, rotations, found, desired_volume)) {
				return true;
			}
		}
		return false;
	}
	
	public static void maximizeKnapsackValue(StoredMini dynamic_awnser, MiniSimpleParcel knapsack, Picker picker, MiniSimpleParcel... parcels) {
		ArrayList<MiniSimpleParcel> generated = generateSortedPatchworkPool(knapsack, parcels);
		generated.sort(SORT_VALUE);
		//TODO implement segmentation of subdivisions and recursive filling of those
		dynamic_awnser.parcel = picker.pick(generated);
	}
	
	public static MiniSimpleParcel[][] getSubDivisions(MiniSimpleParcel knapsack, MiniSimpleParcel placedBlock) {
		MiniSimpleParcel[][] result; int subcount = getSubCount(knapsack, placedBlock);
		if (subcount<=0) return new MiniSimpleParcel[0][0];
		if (subcount==1) return new MiniSimpleParcel[][]{{knapsack.subtract(placedBlock.rotateUntilSimilar(knapsack))}};
		if (subcount==2) {
			result = new MiniSimpleParcel[2][2];
			//TODO seperate the subdivisions
		}
		else {
			result = new MiniSimpleParcel[5][3];
			//TODO create all the subdivisions
		}
		return result;
	}
	
	public static int getSubCount(MiniSimpleParcel knapsack, MiniSimpleParcel placedBlock) {
		return 3 - knapsack.getSimilarCount(placedBlock);
	}
	
	public static ArrayList<MiniSimpleParcel> generateSortedPatchworkPool(MiniSimpleParcel knapsack, MiniSimpleParcel... parcels) {
		boolean care = care_about_rotation_in_equals;
		care_about_rotation_in_equals = false;
		ArrayList<MiniSimpleParcel> patchwork_pool = new ArrayList<MiniSimpleParcel>(Arrays.asList(parcels));
		boolean keep_going = true;
		while (keep_going) {
			keep_going = false;
			ArrayList<MiniSimpleParcel> to_add = new ArrayList<MiniSimpleParcel>();
			for (MiniSimpleParcel current : patchwork_pool) {
				putParcels(patchwork_pool, to_add, knapsack, current.multiplyX(2), current.multiplyY(2), current.multiplyZ(2));
				for (MiniSimpleParcel parcel : patchwork_pool) {
					if (parcel==current) continue;
					MiniSimpleParcel next;
					if (current.isSimilar(parcel)) next = current.add(parcel);
					else if (current.canBeSimilar(parcel)) next = current.rotateUntilSimilar(parcel).add(parcel);
					else continue;
					if (!putParcel(patchwork_pool, to_add, knapsack, next)) continue;
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
	private static boolean putParcel(ArrayList<MiniSimpleParcel> patchwork_pool, ArrayList<MiniSimpleParcel> to_add, MiniSimpleParcel knapsack, MiniSimpleParcel parcel) {
		if (parcel.fitsInside(knapsack) && !patchwork_pool.contains(parcel) && !to_add.contains(parcel)) to_add.add(parcel);
		else return false;
		return true;
	}
	private static void putParcels(ArrayList<MiniSimpleParcel> patchwork_pool, ArrayList<MiniSimpleParcel> to_add, MiniSimpleParcel knapsack, MiniSimpleParcel...parcels) {
		for (MiniSimpleParcel parcel : parcels) putParcel(patchwork_pool, to_add, knapsack, parcel);
	}
	
	/*public static void main(String[] args) {
		test2();
	}*/
	
	public static void test2() {
		StoredMini result = new StoredMini(NONE);
		MiniSimpleParcel knapsack = K;
		MiniSimpleParcel[] parcels = PARCELS;
		bets_densevolume_densityrequirement = 1;
		System.out.println("Maximizing "+knapsack+" with "+Arrays.toString(parcels)+" for total value");
		long start_time = System.nanoTime();
		maximizeKnapsackValue(result, knapsack, BEST_DENSEVOLUME, parcels);
		long delta_time = System.nanoTime() - start_time;
		System.out.println("calculation took "+(float)(delta_time/1000000d)+"ms");
		System.out.println("result value = "+result.parcel.getValue()+"\nresult = "+result.parcel.unravelComponents()+" for a total of "+result.parcel
				+ " with a density of "+result.parcel.getDensity());
		System.out.println("filled volume = "+result.parcel.getVolume()+"/"+knapsack.getVolume());
		System.out.println("amount of sub sections = "+getSubCount(knapsack, result.parcel));
	}
	
	public static void test1() {
		Optional<StoredMini> result;
		MiniSimpleParcel knapsack = new MiniSimpleParcel(12,5,8,0);
		MiniSimpleParcel[] parcels = new MiniSimpleParcel[] {PTP,LL};
		System.out.println("Filling "+knapsack+" with "+Arrays.toString(parcels));
		long start_time = System.nanoTime();
		result = canBeFilled(knapsack, parcels);
		long delta_time = System.nanoTime() - start_time;
		System.out.println("calculation took "+(float)(delta_time/1000000d)+"ms");
		if (result.isPresent()) {
			StoredMini get = result.get();
			System.out.println("It found the following solution:\n"+get.parcel.unravelComponents());
		}
		else System.out.println("It found that there is no solution");
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
		if (!(o instanceof MiniSimpleParcel)) return false;
		MiniSimpleParcel other = (MiniSimpleParcel)o;
		if (care_about_rotation_in_equals) return length==other.length && width == other.width && height == other.height;
		return sameShape(other);
	}
	
	static class StoredMini {
		public MiniSimpleParcel parcel;
		public StoredMini(MiniSimpleParcel parcel) { this.parcel = parcel; }
	}
	
}
