package depth_first_fill;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

import javafxdraw.Colors;
import javafxstuff.Point3D;
import knapsack.Knapsack;
import knapsack.Size3D;
import knapsack.parcel.Parcels;
import knapsack.parcel.SimpleParcel;
import static knapsack.Variables.color_variation;
import static knapsack.Variables.SELECTED_COLORS;

public class MiniSimpleParcel {

	@FunctionalInterface
	static abstract interface Picker extends Function<ArrayList<MiniSimpleParcel>, MiniSimpleParcel> {
		MiniSimpleParcel pick(ArrayList<MiniSimpleParcel> list);
		
		default MiniSimpleParcel apply(ArrayList<MiniSimpleParcel> list) { return pick(list); }
	}
	
	private static final int LENGTH = 1;
	private static final int WIDTH  = 2;
	private static final int HEIGHT = 4;
	
	public static final String AUG_NAME = "AUG";
	public static final String NO_NAME = "N/A";
	public static final MiniSimpleParcel NONE = new MiniSimpleParcel(0,0,0,0, new int[6]);
	public static final MiniSimpleParcel K = new MiniSimpleParcel(33,5,8,0);
	public static final MiniSimpleParcel A = new MiniSimpleParcel(2,2,4,3);
	public static final MiniSimpleParcel B = new MiniSimpleParcel(2,3,4,4);
	public static final MiniSimpleParcel C = new MiniSimpleParcel(3,3,3,5);
	public static final MiniSimpleParcel[] PARCELS = {A, B, C};
	public static final MiniSimpleParcel LL = new MiniSimpleParcel(1,5,2,6); // L = 3, P = 4, T = 5
	public static final MiniSimpleParcel PTP = new MiniSimpleParcel(1,5,3,13);
	public static final MiniSimpleParcel PP = new MiniSimpleParcel(1,5,2,8);
	public static final MiniSimpleParcel[] PENTOS = {LL, PTP, PP};
	static {
		A.count_A = 1; B.count_B = 1; C.count_C = 1;
		LL.count_L = 2; PTP.count_P = 2; PTP.count_T = 1; PP.count_P = 2;
	}
	
	/** If you have the intend of using a different knapsack than the default size for turning MiniSimpleParcel objects into Parcel objects,
	 *   then this variable has to be changed to the prefered knapsack <b>before</b> any additional MiniSimpleParcel objects are created */
	public static Knapsack reference = new Knapsack();
	
	public static boolean care_about_rotation_in_equals = true;
	public static double best_densevolume_densityrequirement = 0.95;
	
	public static final Comparator<MiniSimpleParcel> SORT_DENSITY  = Parcels.getComparatorOfFunction(p->Double.valueOf(p.getDensity()), false);
	public static final Comparator<MiniSimpleParcel> SORT_VALUE    = Parcels.getComparatorOfFunction(p->Double.valueOf(p.getValue()), false);
	public static final Comparator<MiniSimpleParcel> SORT_VOLUME   = Parcels.getComparatorOfFunction(p->Double.valueOf(p.getVolume()), false);
	
	public static final Picker HIGHEST_DENSITY  = p -> { p.sort(SORT_DENSITY); return p.get(0); };
	public static final Picker HIGHEST_VALUE    = p -> { p.sort(SORT_VALUE);   return p.get(0); };
	public static final Picker HIGHEST_VOLUME   = p -> { p.sort(SORT_VOLUME);  return p.get(0); };
	@SuppressWarnings("unchecked")
	public static final Picker BEST_DENSEVOLUME = p -> {
		if (p.size() == 0) return NONE;
		ArrayList<MiniSimpleParcel> list1 = (ArrayList<MiniSimpleParcel>) p.clone();
		ArrayList<MiniSimpleParcel> list2 = (ArrayList<MiniSimpleParcel>) p.clone();
		list1.sort(SORT_DENSITY);
		list2.sort(SORT_VOLUME);
		int index = 0;
		MiniSimpleParcel pick = list2.get(index);
		while (pick.getDensity() < best_densevolume_densityrequirement * list1.get(0).getDensity()) pick = list2.get(++index);
		return pick;
	};
	
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
	
	public void adjustLimits(int[] limits) {
		int[] counts = getCounts();
		for (int i=0; i < counts.length; i++) limits[i] -= counts[i];
	}
	
	private static ArrayList<MiniSimpleParcel> toList(MiniSimpleParcel...components) {
		ArrayList<MiniSimpleParcel> result = new ArrayList<MiniSimpleParcel>(Arrays.asList(components));
		while(result.contains(NONE)) result.remove(NONE);
		return result;
	}
	
	private void setCounts(int[] counts) {
		count_A = counts[0]; count_B = counts[1]; count_C = counts[2];
		count_P = counts[3]; count_T = counts[4]; count_L = counts[5];
	}
	
	private int length, width, height, value, count, count_A, count_B, count_C, count_P, count_T, count_L;
	private ArrayList<MiniSimpleParcel> components;
	private Point3D baseOrigin;
	
	public MiniSimpleParcel(int length, int width, int height, int value) {
		this(length, width, height, value, new int[6]);
	}
	public MiniSimpleParcel(int length, int width, int height, int value, int[] counts) {
		this.length = length;
		this.width = width;
		this.height = height;
		this.value = value;
		count = 1;
		baseOrigin = Point3D.ZERO;
		components = toList(this);
		setCounts(counts);
	}
	private MiniSimpleParcel(int length, int width, int height, int value, int[] counts, MiniSimpleParcel component) {
		this(length, width, height, value, counts);
		this.components = toList(component);
	}
	private MiniSimpleParcel(int length, int width, int height, int value, int[] counts, ArrayList<MiniSimpleParcel> components) {
		this(length, width, height, value, counts);
		this.components.clear();
		this.components.addAll(components);
	}
	
	public MiniSimpleParcel rotateX() {
		ArrayList<MiniSimpleParcel> components = new ArrayList<MiniSimpleParcel>();
		for (MiniSimpleParcel parcel : this.components)
			if (parcel != this && parcel != NONE) components.add(parcel.rotateX());
		return new MiniSimpleParcel(length, height, width, value, getCounts(), components);
	}
	public MiniSimpleParcel rotateY() {
		ArrayList<MiniSimpleParcel> components = new ArrayList<MiniSimpleParcel>();
		for (MiniSimpleParcel parcel : this.components)
			if (parcel != this && parcel != NONE) components.add(parcel.rotateY());
		return new MiniSimpleParcel(height, width, length, value, getCounts(), components);
	}
	public MiniSimpleParcel rotateZ() {
		ArrayList<MiniSimpleParcel> components = new ArrayList<MiniSimpleParcel>();
		for (MiniSimpleParcel parcel : this.components)
			if (parcel != this && parcel != NONE) components.add(parcel.rotateZ());
		return new MiniSimpleParcel(width, length, height, value, getCounts(), components);
	}
	
	public MiniSimpleParcel multiplyX(int count) {
		ArrayList<MiniSimpleParcel> components = new ArrayList<MiniSimpleParcel>(count);
		for (int i=0; i < count; i++) components.add(this);
		return new MiniSimpleParcel(length * count, width, height, value * count, getCounts(count), components);
	}
	public MiniSimpleParcel multiplyY(int count) {
		ArrayList<MiniSimpleParcel> components = new ArrayList<MiniSimpleParcel>(count);
		for (int i=0; i < count; i++) components.add(this);
		return new MiniSimpleParcel(length, width * count, height, value * count, getCounts(count), components);
	}
	public MiniSimpleParcel multiplyZ(int count) {
		ArrayList<MiniSimpleParcel> components = new ArrayList<MiniSimpleParcel>(count);
		for (int i=0; i < count; i++) components.add(this);
		return new MiniSimpleParcel(length, width, height * count, value * count, getCounts(count), components);
	}
	
	/** creates a low-level copy of this {@code MiniSimpleParcel} */
	@SuppressWarnings("unchecked")
	@Override
	public MiniSimpleParcel clone() {
		return new MiniSimpleParcel(length, width, height, value, getCounts(), (ArrayList<MiniSimpleParcel>)components.clone());
	}
	
	public ArrayList<MiniSimpleParcel> getAllComponents() {
		ArrayList<MiniSimpleParcel> result = new ArrayList<MiniSimpleParcel>(components);
		return result;
	}
	
	public static void expand(MiniSimpleParcel parcel, String spaces) {
		if (parcel.isItself()) {
			System.out.println(spaces+spaces.length()+": "+parcel); return; }
		System.out.println(spaces+spaces.length()+": "+parcel+" consists of:");
		for (MiniSimpleParcel sub : parcel.getAllComponents())
			expand(sub, spaces+" ");
	}
	
	public void cleanup() {
		if (isItself()) { components = new ArrayList<MiniSimpleParcel>(); return; }
		for (MiniSimpleParcel component : components) component.cleanup();
	}
	
	public boolean isItself() {
		if (components.size() == 1 && components.get(0) == this) return true;
		if (components.size() == 1) return components.get(0).isItself();
		return false;
	}
	
	public static MiniSimpleParcel getFromKnapsack(Knapsack knapsack) {
		return new MiniSimpleParcel(knapsack.getLength(), knapsack.getWidth(), knapsack.getHeight(), 0);
	}
	
	public void putInKnapsack(Knapsack knapsack) {
		MiniSimpleParcel to_put = rotateToFitInside(getFromKnapsack(knapsack));
		ArrayList<SimpleParcel> parcels = to_put.convert();
		for (SimpleParcel parcel : parcels) knapsack.putParcel(parcel);
	}
	
	public ArrayList<SimpleParcel> convert() {
		cleanup();
		return cleanconvert(baseOrigin);	
	}
	private ArrayList<SimpleParcel> cleanconvert(Point3D base) {
		ArrayList<SimpleParcel> result = new ArrayList<SimpleParcel>();
		if (isItself() || components.size() == 0) {
			String name = getNameFromCounts(); Color color;
			if (name.equals("A") || name.equals("P")) color = Colors.randomDeviation(SELECTED_COLORS[0], color_variation);
			else if (name.equals("B") || name.equals("L")) color = Colors.randomDeviation(SELECTED_COLORS[1], color_variation);
			else if (name.equals("C") || name.equals("T")) color = Colors.randomDeviation(SELECTED_COLORS[2], color_variation);
			else color = new Color(100,100,100);
			result.add(new SimpleParcel(length, width, height, value, base, color, name));
			return result;
		}
		MiniSimpleParcel comp1 = components.get(0);
		int c = getSimilar(comp1);
		Point3D totaldelta = Point3D.ZERO;
		Point3D delta = new Point3D(1,0,0);
		if ((c & WIDTH) == 0) delta = new Point3D(0,1,0);
		else if ((c & HEIGHT) == 0) delta = new Point3D(0,0,1);
		for (MiniSimpleParcel component : components) {
			result.addAll(component.cleanconvert(base.add(totaldelta)));
			totaldelta = totaldelta.add(product(delta, new Point3D(component.length, component.width, component.height)));
		}
		return result;
	}
	private Point3D product(Point3D a, Point3D b) {
		return new Point3D(a.getX() * b.getX(), a.getY() * b.getY(), a.getZ() * b.getZ());
	}
	
	public String getNameFromCounts() {
		int[] counts = getCounts(); int variants = 0;
		for (int i=0; i < counts.length; i++) if (counts[i]!=0) variants++;
		if (variants == 0) return NO_NAME; if (variants > 1) return AUG_NAME;
		if (count_A != 0) return "A"; if (count_B != 0) return "B";
		if (count_C != 0) return "C"; if (count_P != 0) return "P";
		if (count_T != 0) return "T"; return "L";
	}
	
	public static void main(String[] args) {
		best_densevolume_densityrequirement = 0;
		int[] limits = {0,20,10,0,0,0};
		MiniSimpleParcel result = MiniSimpleParcel.maximizeKnapsackValue(K, PENTOS, null);
		result.adjustLimits(limits);
		System.out.println(Arrays.toString(limits));
		expand(result, "");
		System.out.println(result.counts());
		Knapsack knapsack = new Knapsack();
		result.putInKnapsack(knapsack);
		System.out.println(knapsack.getFilledVolume()+"/"+knapsack.getVolume());
	}
	
	public MiniSimpleParcel add(MiniSimpleParcel other) {
		if (this.equals(NONE) || other.equals(NONE)) 
			return new MiniSimpleParcel(other.length + length, other.width + width, other.height + height, value + other.value, getCounts(other.getCounts()), toList(this, other));
		int c = getSimilar(other);
		if (!isSimilar(c)) return null;
		if ((c & WIDTH)  == 0) return new MiniSimpleParcel(length, other.width + width, height, value + other.value, getCounts(other.getCounts()), toList(this, other.clone()));
		if ((c & HEIGHT) == 0) return new MiniSimpleParcel(length, width, other.height + height, value + other.value, getCounts(other.getCounts()), toList(this, other.clone()));
		return new MiniSimpleParcel(other.length + length, width, height, value + other.value, getCounts(other.getCounts()), toList(this, other.clone()));
	}
	
	public MiniSimpleParcel subtract(MiniSimpleParcel other) {
		if (this.equals(NONE) && !other.equals(NONE)) return null;
		if (other.equals(NONE)) return clone();
		int c = getSimilar(other);
		if (!isSimilar(c)) return null;
		if ((c & LENGTH) == 0) {
			if (other.length > length) return null;
			return new MiniSimpleParcel(length - other.length, width, height, value - other.value, subtractCounts(other.getCounts()));
		}
		if ((c & WIDTH)  == 0) {
			if (other.width > width) return null;
			return new MiniSimpleParcel(length, width - other.width, height, value - other.value, subtractCounts(other.getCounts()));
		}
		if ((c & HEIGHT) == 0) {
			if (other.height > height) return null;
			return new MiniSimpleParcel(length, width, height - other.height, value - other.value, subtractCounts(other.getCounts()));
		}
		return null;
	}
	
	/**
	 * @param other the other miniSimpleParcel that this parcel can be similar to
	 * @param similarCountAndOrder the result from {@link #getSimilarCountAndOrder}
	 * @return a parcel that is a rotated version of the original parcel such that it is similar to {@code other}
	 */
	public MiniSimpleParcel rotateUntilSimilar(MiniSimpleParcel other, int[] similarCountAndOrder) {
		int[] nlwh = {length, width, height};
		int[] old = {length, width, height};
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
			int similar = getSimilar(old, nlwh);
			if (similar==LENGTH) return rotateX();
			else if (similar==WIDTH) return rotateY();
			else if (similar==HEIGHT) return rotateZ();
			else {
				int temp = old[0]; old[0] = old[1]; old[1] = temp;
				similar = getSimilar(old, nlwh);
				if (similar==LENGTH) return rotateZ().rotateX();
				else if (similar==WIDTH) return rotateZ().rotateY();
			}
		}
		return new MiniSimpleParcel(nlwh[0], nlwh[1], nlwh[2], value, getCounts(), this);
	}
	
	public static int getSimilar(int[] a, int[] b) {
		int c = 0;
		if (a[0]==b[0]) c |= LENGTH;
		if (a[1]==b[1]) c |= WIDTH;
		if (a[2]==b[2]) c |= HEIGHT;
		return c;
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
	
	public MiniSimpleParcel rotateToFitInside(MiniSimpleParcel other) {
		int[] old   = {length, width, height};
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
		int[] simo = other.getSimilarCountAndOrder(new MiniSimpleParcel(lwh_o[0], lwh_o[1], lwh_o[2], other.value, getCounts()));
		int[] res = new int[3];
		for (int i=0; i < 3; i++) {
			if (simo[i+1] == 1) res[0] = lwh_t[i];
			if (simo[i+1] == 2) res[1] = lwh_t[i];
			if (simo[i+1] == 4) res[2] = lwh_t[i];
		}
		int similar = getSimilar(old, res);
		if (similar==LENGTH) return rotateX();
		else if (similar==WIDTH) return rotateY();
		else if (similar==HEIGHT) return rotateZ();
		else {
			int temp = old[0]; old[0] = old[1]; old[1] = temp;
			similar = getSimilar(old, res);
			if (similar==LENGTH) return rotateZ().rotateX();
			else if (similar==WIDTH) return rotateZ().rotateY();
		}
		return new MiniSimpleParcel(res[0], res[1], res[2], value, getCounts(), this);
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
	
	public static MiniSimpleParcel maximizeKnapsackFilling(MiniSimpleParcel knapsack, MiniSimpleParcel... parcels) {
		ArrayList<MiniSimpleParcel> generated = generatePatchworkPool(knapsack, parcels);
		generated.sort(SORT_VOLUME);
		System.out.println(generated);
		return generated.get(0);
	}
	
	/**
	 * 
	 * @param knapsack the MiniSimpleParcel representing the knapsack to be filled, does not get mutated
	 * @param picker the {@code Picker} object used to select the best parcel out of each patchworkpool
	 * @param parcels the parcels to be used to fill the knapsack (for counting limits, the values count_A, count_B, count_C, etc. need to be specified)
	 * @param limits an {@code int[]} representing the amounts of each parcel to maximally be used when filling (in the order of: [count_A, count_B,
	 *         count_C, count_P, count_T, count_L]), let this be <b>{@code null}</b> to solve the <b>unbounded</b> knapsack problem
	 * @return a single MiniSimpleParcel representing an optimum parcel combination that maximizes whatever the {@code Picker} wants
	 */
	public static MiniSimpleParcel maximizeKnapsackPicker(MiniSimpleParcel knapsack, Picker picker, MiniSimpleParcel[] parcels, int[] limits) {
		ArrayList<MiniSimpleParcel> generated = generatePatchworkPool(limits, knapsack, parcels);
		return picker.pick(generated);
	}
	
	public static MiniSimpleParcel maximizeKnapsackValue(MiniSimpleParcel knapsack, MiniSimpleParcel[] parcels, int[] limits) {
		return maximizeKnapsackPicker(knapsack, HIGHEST_VALUE, parcels, limits);
	}
	
	/*public static ArrayList<MiniSimpleParcel> createSegmentedAugmentationParcels(MiniSimpleParcel knapsack, Picker picker, MiniSimpleParcel[] parcels, int[] limits) {
		ArrayList<MiniSimpleParcel> generated = generatePatchworkPool(limits, knapsack, parcels);
		if (generated.size() < 1) return new ArrayList<MiniSimpleParcel>(0);
		MiniSimpleParcel nextPlacement = picker.pick(generated);
		
		//TODO implement segmentation of subdivisions and recursive filling of those
	} */
	
	public static MiniSimpleParcel[][] getSubDivisions(MiniSimpleParcel knapsack, MiniSimpleParcel placedBlock) {
		MiniSimpleParcel[][] result; int subcount = getSubCount(knapsack, placedBlock);
		if (subcount<=0) return new MiniSimpleParcel[0][0];
		if (subcount==1) return new MiniSimpleParcel[][]{{knapsack.subtract(placedBlock.rotateUntilSimilar(knapsack))}};
		if (subcount==2) {
			result = new MiniSimpleParcel[2][2];
			MiniSimpleParcel main = placedBlock.rotateToFitInside(knapsack);
			MiniSimpleParcel A_part, B_part, C_part;
			int sim = main.getSimilar(knapsack);
			if (sim==LENGTH) {
				A_part = new MiniSimpleParcel(main.length, main.width, knapsack.height, 0, main.getCounts()).subtract(main);
				B_part = new MiniSimpleParcel(main.length, knapsack.width, main.height, 0, main.getCounts()).subtract(main);
				C_part = new MiniSimpleParcel(main.length, knapsack.width - main.width, knapsack.height - main.height, 0);
			} else if (sim==WIDTH) {
				A_part = new MiniSimpleParcel(main.length, main.width, knapsack.height, 0, main.getCounts()).subtract(main);
				B_part = new MiniSimpleParcel(knapsack.length, main.width, main.height, 0, main.getCounts()).subtract(main);
				C_part = new MiniSimpleParcel(knapsack.length - main.length, main.width, knapsack.height - main.height, 0);
			} else {
				A_part = new MiniSimpleParcel(main.length, knapsack.width, main.height, 0, main.getCounts()).subtract(main);
				B_part = new MiniSimpleParcel(knapsack.length, main.width, main.height, 0, main.getCounts()).subtract(main);
				C_part = new MiniSimpleParcel(knapsack.length - main.length, knapsack.width - main.width, main.height, 0);
			}
			result[0][0] = A_part.add(C_part);
			result[0][1] = B_part;
			result[1][0] = A_part;
			result[1][1] = B_part.add(C_part);
		}
		else {
			result = new MiniSimpleParcel[6][3];
			MiniSimpleParcel main = placedBlock.rotateToFitInside(knapsack);
			MiniSimpleParcel A_part, B_part, C_part, D_part, E_part, F_part, G_part;
			A_part = new MiniSimpleParcel(knapsack.length - main.length, main.width, main.height, 0); // length
			B_part = new MiniSimpleParcel(main.length, knapsack.width - main.width, main.height, 0);  // width
			C_part = new MiniSimpleParcel(main.length, main.width, knapsack.height - main.height, 0); // height
			D_part = new MiniSimpleParcel(A_part.length, main.width, C_part.height, 0);
			E_part = new MiniSimpleParcel(A_part.length, B_part.width, main.height, 0);
			F_part = new MiniSimpleParcel(main.length, B_part.width, C_part.height, 0);
			G_part = new MiniSimpleParcel(A_part.length, B_part.width, C_part.height, 0);
			MiniSimpleParcel A_plane = A_part.add(D_part).add(E_part.add(G_part));
			MiniSimpleParcel B_plane = B_part.add(E_part).add(F_part.add(G_part));
			MiniSimpleParcel C_plane = C_part.add(F_part).add(D_part.add(G_part));
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
	
	public static int getSubCount(MiniSimpleParcel knapsack, MiniSimpleParcel placedBlock) {
		return 3 - knapsack.getSimilarCount(placedBlock);
	}
	
	public static ArrayList<MiniSimpleParcel> generatePatchworkPool(MiniSimpleParcel knapsack, MiniSimpleParcel...parcels) {
		return generatePatchworkPool(null, knapsack, parcels);
	}
	
	public static ArrayList<MiniSimpleParcel> generatePatchworkPool(int[] countLimits, MiniSimpleParcel knapsack, MiniSimpleParcel... parcels) {
		boolean ignore_limit = countLimits==null; int amount_added = 0;
		boolean care = care_about_rotation_in_equals;
		care_about_rotation_in_equals = false;
		ArrayList<MiniSimpleParcel> patchwork_pool = new ArrayList<MiniSimpleParcel>(Arrays.asList(parcels));
		boolean keep_going = true;
		while (keep_going) {
			keep_going = false;
			ArrayList<MiniSimpleParcel> to_add = new ArrayList<MiniSimpleParcel>();
			for (MiniSimpleParcel current : patchwork_pool) {
				putParcels(ignore_limit, countLimits, patchwork_pool, to_add, knapsack, current.multiplyX(2), current.multiplyY(2), current.multiplyZ(2));
				for (MiniSimpleParcel parcel : patchwork_pool) {
					if (parcel==current) continue;
					MiniSimpleParcel next;
					if (current.isSimilar(parcel)) next = current.add(parcel);
					else if (current.canBeSimilar(parcel)) next = current.rotateUntilSimilar(parcel).add(parcel);
					else continue;
					if (!putParcel(ignore_limit, countLimits, patchwork_pool, to_add, knapsack, next)) continue;
				}
			}
			patchwork_pool.addAll(to_add);
			if (to_add.size() > 0) keep_going = true;
			amount_added += to_add.size();
			System.out.println("added "+to_add.size()+" elements");
		}
		if (amount_added <= 0)
			for (int i=0; i < patchwork_pool.size(); i++) if (patchwork_pool.get(i).exceedsLimit(countLimits)) patchwork_pool.remove(i--);
		care_about_rotation_in_equals = care;
		return patchwork_pool;
	}
	
	/** @return */
	private static boolean putParcel(boolean ignore_limit, int[] limits, ArrayList<MiniSimpleParcel> patchwork_pool, ArrayList<MiniSimpleParcel> to_add, MiniSimpleParcel knapsack, MiniSimpleParcel parcel) {
		if (parcel.fitsInside(knapsack) && (ignore_limit || !parcel.exceedsLimit(limits)) && !patchwork_pool.contains(parcel) && !to_add.contains(parcel)) to_add.add(parcel);
		else return false;
		return true;
	}
	private static void putParcels(boolean ignore_limit, int[] limits, ArrayList<MiniSimpleParcel> patchwork_pool, ArrayList<MiniSimpleParcel> to_add, MiniSimpleParcel knapsack, MiniSimpleParcel...parcels) {
		for (MiniSimpleParcel parcel : parcels) putParcel(ignore_limit, limits, patchwork_pool, to_add, knapsack, parcel);
	}
	
	public static void test2() {
		MiniSimpleParcel result = NONE;
		MiniSimpleParcel knapsack = K;
		MiniSimpleParcel[] parcels = PENTOS;
		best_densevolume_densityrequirement = 0;
		System.out.println("Maximizing "+knapsack+" with "+Arrays.toString(parcels)+" for total value");
		long start_time = System.nanoTime();
		result = maximizeKnapsackPicker(knapsack, BEST_DENSEVOLUME, parcels, null);
		long delta_time = System.nanoTime() - start_time;
		System.out.println("calculation took "+(float)(delta_time/1000000d)+"ms");
		System.out.println("result value = "+result.getValue()+"\nresult = "+result.counts()+" for a total of "+result
				+" with a density of "+result.getDensity());
		System.out.println("filled volume = "+result.getVolume()+"/"+knapsack.getVolume());
		System.out.println("amount of sub sections = "+getSubCount(knapsack, result));
		
	}
	
	public static void test1() {
		MiniSimpleParcel result;
		MiniSimpleParcel knapsack = K;
		MiniSimpleParcel[] parcels = PARCELS;
		System.out.println("Filling "+knapsack+" with "+Arrays.toString(parcels));
		long start_time = System.nanoTime();
		result = maximizeKnapsackFilling(knapsack, parcels);
		long delta_time = System.nanoTime() - start_time;
		System.out.println("calculation took "+(float)(delta_time/1000000d)+"ms");
		System.out.println("It found the best solution:\n"+result.counts()+" for a total of "+result);
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
	
	public String counts() {
		String result = "";
		if (count_A!=0) result += "A: x"+count_A;
		if (count_B!=0) result += addcomma(result)+"B: x"+count_B;
		if (count_C!=0) result += addcomma(result)+"C: x"+count_C;
		if (count_P!=0) result += addcomma(result)+"P: x"+count_P;
		if (count_T!=0) result += addcomma(result)+"T: x"+count_T;
		if (count_L!=0) result += addcomma(result)+"L: x"+count_L;
		return result;
	}
	private static String addcomma(String a) {
		return (a.length() > 0)?", ":"";
	}
}
