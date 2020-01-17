package depth_first_fill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import knapsack.Size3D;

public class MiniSimpleParcel {

	private static final int LENGTH = 1;
	private static final int WIDTH = 2;
	private static final int HEIGHT = 4;
	
	public static final MiniSimpleParcel NONE = new MiniSimpleParcel(0,0,0,0, 1, new ArrayList<MiniSimpleParcel>());
	public static final MiniSimpleParcel K = new MiniSimpleParcel(33,5,8,0);
	public static final MiniSimpleParcel A = new MiniSimpleParcel(2,2,4,3);
	public static final MiniSimpleParcel B = new MiniSimpleParcel(2,3,4,4);
	public static final MiniSimpleParcel C = new MiniSimpleParcel(3,3,3,5);
	public static final MiniSimpleParcel[] PARCELS = {A, B, C};
	
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
	private MiniSimpleParcel(int length, int width, int height, int value, int count, ArrayList<MiniSimpleParcel> components) {
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
		return new MiniSimpleParcel(length, height, width, value, count, components);
	}
	public MiniSimpleParcel rotateY() {
		return new MiniSimpleParcel(height, width, length, value, count, components);
	}
	public MiniSimpleParcel rotateZ() {
		return new MiniSimpleParcel(width, length, height, value, count, components);
	}
	
	public MiniSimpleParcel multiplyX(int count) {
		return new MiniSimpleParcel(length * count, width, height, value * count, this.count * count, components);
	}
	public MiniSimpleParcel multiplyY(int count) {
		return new MiniSimpleParcel(length, width * count, height, value * count, this.count * count, components);
	}
	public MiniSimpleParcel multiplyZ(int count) {
		return new MiniSimpleParcel(length, width, height * count, value * count, this.count * count, components);
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
		if ((c & WIDTH) == 0) return new MiniSimpleParcel(length, other.width + width, height, value + other.value, this.components, other.components);
		if ((c & HEIGHT) == 0) return new MiniSimpleParcel(length, width, other.height + height, value + other.value, this.components, other.components);
		return new MiniSimpleParcel(other.length + length, width, height, value + other.value, this.components, other.components);
	}
	
	public MiniSimpleParcel rotateUntilSimilar(MiniSimpleParcel other) {
		int c = 0;
		int[] lwh = {length, width, height};
		int new_length = 0;
		int new_width = 0;
		int new_height = 0;
		for (int i=0; i < 3; i++) {
			if (other.length == lwh[i]) { new_length = lwh[i]; c++; }
			if (other.width == lwh[i])  { new_width  = lwh[i]; c++; }
			if (other.height == lwh[i]) { new_height = lwh[i]; c++; }
		} if (c < 2) return null;
		return new MiniSimpleParcel(new_length, new_width, new_height, value, count, components);
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
		if (width==other.width) c |= WIDTH;
		if (height==other.height) c |= HEIGHT;
		return c;
	}
	public boolean canBeSimilar(MiniSimpleParcel other) {
		return getSimilarNoOrder(other) >= 2;
	}
	public int getSimilarNoOrder(MiniSimpleParcel other) {
		int[] lwh = {length, width, height};
		int c = 0;
		for (int i=0; i < 3; i++) if (other.length == lwh[i] || other.width == lwh[i] || other.height == lwh[i]) c++;
		return c;
	}
	public boolean sameShape(MiniSimpleParcel other) {
		return getSimilarNoOrder(other) == 3;
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
	
	public static Optional<StoredMini> canBeFilled(MiniSimpleParcel knapsack, MiniSimpleParcel[] parcels) {
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
	
	public static void main(String[] args) {
		Optional<StoredMini> result;
		long start_time = System.nanoTime();
		result = canBeFilled(new MiniSimpleParcel(33,5,8,0), PARCELS);
		long delta_time = System.nanoTime() - start_time;
		System.out.println("calculation took "+(float)(delta_time/1000000d)+"ms");
		if (result.isPresent()) {
			StoredMini get = result.get();
			System.out.println("It found the following solution:\n"+get.parcel.unravelComponents());
		}
		else System.out.println("It found that there is no solution");
	}
	
	public String toString() {
		return count+"x ("+length+"x"+width+"x"+height+")";
	}
	public boolean equals(Object o) {
		if (this==o) return true;
		if (!(o instanceof MiniSimpleParcel)) return false;
		MiniSimpleParcel other = (MiniSimpleParcel)o;
		return length==other.length && width == other.width && height == other.height;
	}
	
	static class StoredMini {
		public MiniSimpleParcel parcel;
		public StoredMini(MiniSimpleParcel parcel) { this.parcel = parcel; }
	}
	
}
