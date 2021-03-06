package depth_first_fill;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import javafxstuff.Point3D;
import knapsack.Knapsack;
import knapsack.Size3D;
import knapsack.parcel.FastParcel;
import knapsack.parcel.Parcel;
import knapsack.parcel.ParcelCore;
import knapsack.parcel.Parcels;
import knapsack.parcel.PentominoParcel;
import knapsack.parcel.SimpleParcel;

enum Plane {
	XY(true,true,false), XZ(true,false,true), YZ(false,true,true);
	public boolean x;
	public boolean y;
	public boolean z;
	Plane(boolean x, boolean y, boolean z) {
		this.x = x; this.y = y; this.z = z;
	}
}

/**
 * Make a new Phase1Algorithm for the seperate problems (simple vs pentomino)
 */
public class ParcelminoesAlgorithm {
	
	public static boolean DEBUG = true;
	
	private final int length;
	private final int width;
	private final int height;
	private SimpleParcel[] simple_parcels;
	private PentominoParcel[] pento_parcels;
	public Knapsack knapsack;
	
	public ParcelminoesAlgorithm(Knapsack knapsack, ParcelCore... parcels) {
		this.knapsack = knapsack.copy();
		length = knapsack.getLength();
		width = knapsack.getWidth();
		height = knapsack.getHeight();
		ArrayList<SimpleParcel> tempsimp = new ArrayList<SimpleParcel>();
		ArrayList<PentominoParcel> temppent = new ArrayList<PentominoParcel>();
		for (ParcelCore parcel : parcels)
			if (parcel instanceof SimpleParcel) tempsimp.add((SimpleParcel)parcel);
			else temppent.add((PentominoParcel)parcel);
		Parcels.sortByDensity(tempsimp);
		Parcels.sortByDensity(temppent);
		simple_parcels = tempsimp.toArray(new SimpleParcel[tempsimp.size()]);
		pento_parcels = temppent.toArray(new PentominoParcel[temppent.size()]);
	}
	
	public static void main(String[] args) {
		ParcelminoesAlgorithm phase = new ParcelminoesAlgorithm(new Knapsack(33,5,8), Parcels.DEFAULT);
		boolean use_pentominoes = false;
		System.out.println(phase.solveKnapsackFillingPentoFirst(use_pentominoes));
	}
	
	public static void debug(String message) {
		if (DEBUG) System.out.println(message);
	}
	
	public Optional<Knapsack> solveUnboundedKnapsackPentoFirst(boolean pentominoes) {
		ParcelCore[] chosen = pento_parcels;
		if (!pentominoes) { FastParcel.initiate(knapsack, simple_parcels); chosen = simple_parcels; }
		else FastParcel.initiate(knapsack, pento_parcels);
		int max_value = (int)((((double)knapsack.getVolume()) / chosen[0].getVolume()) * chosen[0].getValue());
		return solveUnboundedKnapsackPentoFirstSearch(new HashSet<HashSet<Integer>>(), knapsack, knapsack.getEmpty(), 1, max_value, pentominoes, 0, 0, 0);
	}
	
	public Optional<Knapsack> solveKnapsackFillingPentoFirst(boolean pentominoes) {
		if (!pentominoes) FastParcel.initiate(knapsack, simple_parcels);
		else FastParcel.initiate(knapsack, pento_parcels);
		return solvePentoFirstFillSearch(new HashSet<HashSet<Integer>>(), knapsack, knapsack.getEmpty(), 1, knapsack.getVolume(), pentominoes, 0, 0, 0);
	}
	private Optional<Knapsack> solvePentoFirstFillSearch(HashSet<HashSet<Integer>> prev_knaps, Knapsack tempsol, Knapsack bestsol, int depth, int max_volume, boolean pento_mode, int start_x, int start_y, int start_z) {
		int max_x = (depth==1)?tempsol.getLength()/2+1:tempsol.getLength();
		int max_y = (depth==1)?tempsol.getWidth ()/2+1:tempsol.getWidth ();
		int max_z = (depth==1)?tempsol.getHeight()/2+1:tempsol.getHeight();
		int temp_start_x = start_x;
		int temp_start_y = start_y;
		int temp_start_z = start_z;
		for (int id = 0; id < FastParcel.max_parcel_ID(); id++) {
			posloop: {
			for (int x=temp_start_x; x < max_x; x++) {
				for (int y=temp_start_y; y < max_y; y++) {
					pos: for (int z=temp_start_z; z < max_z; z++) {
						if (x>=max_x-1 && y>=max_y-1 && z>=max_z-1) {
							int volume = tempsol.getFilledVolume();
							if (volume > bestsol.getFilledVolume()) {
								if (volume >= max_volume) return Optional.of(tempsol.copy());
								tempsol.copyTo(bestsol);
								System.out.println("best solution yet: "+bestsol.getFilledVolume()+"/"+bestsol.getVolume()+" m3");
							} break posloop;
						}
						if (tempsol.isOccupied(x, y, z)) continue pos;
						FastParcel parcel = new FastParcel(id, x, y, z);
						if (!parcel.isValid() || !knapsack.putParcel(parcel)) continue pos;
						placed: {
							if (prev_knaps.contains(tempsol.getContents())) break placed;
							if (!scanFillable(tempsol, parcel, pento_mode)) break placed;
							prev_knaps.add(tempsol.getContents());
							if (solvePentoFirstFillSearch(prev_knaps, tempsol, bestsol, depth+1, max_volume, pento_mode, x, y, z+1).isPresent()) return Optional.of(tempsol);
						} knapsack.remove(parcel);
						temp_start_z = 0;
					} temp_start_y = 0;
				} temp_start_x = 0;
			}}
		}
		return Optional.empty();
	}
	
	private Optional<Knapsack> solveUnboundedKnapsackPentoFirstSearch(HashSet<HashSet<Integer>> prev_knaps, Knapsack tempsol, Knapsack bestsol, int depth, int max_value, boolean pento_mode, int start_x, int start_y, int start_z) {
		int max_x = (depth==1)?tempsol.getLength()/2+1:tempsol.getLength();
		int max_y = (depth==1)?tempsol.getWidth ()/2+1:tempsol.getWidth ();
		int max_z = (depth==1)?tempsol.getHeight()/2+1:tempsol.getHeight();
		int temp_start_x = start_x;
		int temp_start_y = start_y;
		int temp_start_z = start_z;
		for (int id = 0; id < FastParcel.max_parcel_ID(); id++) {
			posloop: {
			for (int x=temp_start_x; x < max_x; x++) {
				for (int y=temp_start_y; y < max_y; y++) {
					pos: for (int z=temp_start_z; z < max_z; z++) {
						if (x>=max_x-1 && y>=max_y-1 && z>=max_z-1) {
							int value = tempsol.getValue();
							if (value > bestsol.getValue()) {
								if (value >= max_value) return Optional.of(tempsol.copy());
								tempsol.copyTo(bestsol);
								System.out.println("best solution yet: "+bestsol.getValue()+"/"+max_value+" $");
							} break posloop;
						}
						if (tempsol.isOccupied(x, y, z)) continue pos;
						FastParcel parcel = new FastParcel(id, x, y, z);
						if (!parcel.isValid() || !knapsack.putParcel(parcel)) continue pos;
						placed: {
							if (prev_knaps.contains(tempsol.getContents())) break placed;
							if (!scanFillable(tempsol, parcel, pento_mode)) break placed;
							prev_knaps.add(tempsol.getContents());
							if (solveUnboundedKnapsackPentoFirstSearch(prev_knaps, tempsol, bestsol, depth+1, max_value, pento_mode, x, y, z+1).isPresent()) return Optional.of(tempsol);
						} knapsack.remove(parcel);
						temp_start_z = 0;
					} temp_start_y = 0;
				} temp_start_x = 0;
			}}
		}
		return Optional.empty();
	}
	
	private boolean scanFillable(Knapsack tempsol, Parcel just_placed, boolean pento_mode) {
		if (!pento_mode) return scanFillableSimple(tempsol.getOccupiedCubes(), just_placed);
		return scanFillablePento(tempsol.getOccupiedCubes(), just_placed);
	}
	
	/**
	 * Properly scans the testing knapsack for unfillable spaces right after placing a parcel
	 */
	private boolean scanFillableSimple(BigInteger test, Parcel just_placed) {
		Point3D o = just_placed.getOrigin();
		dim3count range = new dim3count((int)o.getX(), (int)o.getY(), (int)o.getZ(), just_placed.getLength(), just_placed.getWidth(), just_placed.getHeight());
		return mightBeSolvable(test, range);
	}
	
	/**
	 * Properly scans the testing knapsack for unfillable spaces right after placing a parcel
	 */
	private boolean scanFillablePento(BigInteger test, Parcel just_placed) {
		Point3D o = just_placed.getOrigin();
		dim3count range = new dim3count((int)o.getX(), (int)o.getY(), (int)o.getZ(), just_placed.getLength(), just_placed.getWidth(), just_placed.getHeight());
		return mightBeSolvable(test, range, Plane.XY) && mightBeSolvable(test, range, Plane.XZ) && mightBeSolvable(test, range, Plane.YZ);
	}
	
	/**
	 * Scans the area of the given range for unfillable holes by any Parcels (doesn't correct for shape, only hitbox and volume)
	 */
	private boolean mightBeSolvable(BigInteger test, dim3count range) {
		changableBI used = new changableBI(0);
		for (int x=range.x_min; x < length && x < range.x_max; x++)
			for (int y=range.y_min; y < width && y < range.y_max; y++)
				for (int z=range.z_min; z < height && z < range.z_max; z++) {
					if (test(used,x,y,z) || test(test,x,y,z)) continue;
					boolean valid = check3DFreedom(test, used, x, y, z);
					if (!valid) return false;
				}
		return true;
	}
	
	/**
	 * Scans the area of the given range for unfillable holes by pentomino Parcels (corrects for a volume of 5, and a flat shape on the given plane)
	 */
	private boolean mightBeSolvable(BigInteger test, dim3count range, Plane plane) {
		changableBI used = new changableBI(0);
		for (int x=range.x_min; x < length && x < range.x_max; x++)
			for (int y=range.y_min; y < width && y < range.y_max; y++)
				for (int z=range.z_min; z < height && z < range.z_max; z++) {
					if (test(used,x,y,z) || test(test,x,y,z)) continue;
					boolean valid = checkPentoFreedom(test, used, x, y, z, plane);
					if (!valid) return false;
				}
		return true;
	}

	/**
	 * This recursive method will check whether a 2D area from a certain point is divisible by 5 (can you fill it with pentonimos?)
	 * @param test : the temporary situation of the plane (with the current check-pentomino in there)
	 * @param used : all the elements that are already checked before by the {@link #mightBeSolvable} method.
	 * @param x position
	 * @param y position
	 * @param z position
	 * @param plane the plane at which the freedom will be checked
	 * @return {@code true} if it can theoretically be filled with pentominoes
	 */
	private boolean checkPentoFreedom(BigInteger test, changableBI used, int x, int y, int z, Plane plane) {
		if (test.testBit(to1DCoord(x,y,z))) return false;
		return countEmptyPlaneSpaces(test, used, x, y, z, plane, 0) %5 == 0;
	}
	
	/**
	 * Do mind: this method uses the sizes of Parcels.A, Parcels.B and Parcels.C to approximate hitboxes,
	 * do change these if other (simple) parcels are being used
	 */
	private boolean check3DFreedom(BigInteger test, changableBI used, int x, int y, int z) {
		if (test.testBit(to1DCoord(x,y,z))) return false;
		dim3count result = countEmptySpaces(test, used, x, y, z, new dim3count());
		int x_r = result.getRangeX();
		int y_r = result.getRangeX();
		int z_r = result.getRangeX();
		Parcel p1 = Parcels.A; Size3D s1 = p1.getHitBox();
		Parcel p2 = Parcels.B; Size3D s2 = p1.getHitBox();
		Parcel p3 = Parcels.C; Size3D s3 = p1.getHitBox();
		if (x_r < s1.length && x_r < s2.length && x_r < s3.length) return false;
		if (y_r < s1.width  && y_r < s2.width  && y_r < s3.width ) return false;
		if (z_r < s1.height && z_r < s2.height && z_r < s3.height) return false;
		return canBeFilledNumerically(p1.getVolume(), p2.getVolume(), p3.getVolume(), result.getVolume());
	}
	
	/**
	 * checks whether the volume {@code V} can theoretically be achieved by adding the different volumes ({@code a, b, c}) together (no bounds)
	 * @return {@code true} if it can be filled, {@code false} otherwise
	 */
	private boolean canBeFilledNumerically(int a, int b, int c, int v) {
		if (v%a==0) return true;
		if (v%b==0) return true;
		if (v%c==0) return true;
		int[] list = {a, b, c};
		Arrays.sort(list);
		a = list[0]; // smallest
		b = list[1];
		c = list[2]; // highest
		if (v < a) return false;
		if (v < b) return v%a==0;
		if (v < c) return canBeFilledNumerically(a, b, v);
		// now we have all three to work with:
		int a_count = 0;
		for (int x; (x = v - a * a_count) >= b; a_count++) {
			if (x%b==0) return true;
			if (x%c==0) return true;
			if (canBeFilledNumerically(b, c, x)) return true;
		}
		return false;
	}
	
	private boolean canBeFilledNumerically(int a, int b, int v) {
		// c is not in it, v can be a combination of a and b (with both at least 1x)
		int a_count = 1;
		for (int x; (x = v - a * a_count) >= b; a_count++)
			// x is the restant if you remove all the b-volumes from v (x is made up of all a's)
			if (x%b==0) return true; // true if the restant can actually be made out of b's
		return false;
	}
	
	/**
	 * Main drive behind the recursive open-space cut-off 'algorithm'. It works from the starting position as a tree. Never backtracking and
	 * 	continuously counting every step it makes to get to a total area of the open-space.<br>
	 * The starting point (x,y,z) needs to be an empty spot, if it is not, unexpected behaviour might occur.
	 * @param test
	 * @param used
	 * @param x x-coordinate of an empty grid
	 * @param y y-coordinate of an empty grid
	 * @param z z-coordinate of an empty grid
	 * @param count used in the recursive call to keep track of the counted amount of empty spaces, initiate as 0
	 * @return
	 */
	private dim3count countEmptySpaces(BigInteger test, changableBI used, int x, int y, int z, dim3count count) {
		used.setBit(to1DCoord(x,y,z));
		count.add(x,y,z);
		
		if (x < length-1 && !test.testBit(to1DCoord(x+1,y,z)) && !used.testBit(to1DCoord(x+1,y,z))) count.add(countEmptySpaces(test, used, x+1, y, z, new dim3count()));
		if (x > 0 && !test.testBit(to1DCoord(x-1,y,z)) && !used.testBit(to1DCoord(x-1,y,z))) count.add(countEmptySpaces(test, used, x-1, y, z, new dim3count()));
		if (y < width-1 && !test.testBit(to1DCoord(x,y+1,z)) && !used.testBit(to1DCoord(x,y+1,z))) count.add(countEmptySpaces(test, used, x, y+1, z, new dim3count()));
		if (y > 0 && !test.testBit(to1DCoord(x,y-1,z)) && !used.testBit(to1DCoord(x,y-1,z))) count.add(countEmptySpaces(test, used, x, y-1, z, new dim3count()));
		if (z < height-1 && !test.testBit(to1DCoord(x,y,z+1)) && !used.testBit(to1DCoord(x,y,z+1))) count.add(countEmptySpaces(test, used, x, y, z+1, new dim3count()));
		if (z > 0 && !test.testBit(to1DCoord(x,y,z-1)) && !used.testBit(to1DCoord(x,y,z-1))) count.add(countEmptySpaces(test, used, x, y, z-1, new dim3count()));
		return count;
	}

	private int countEmptyPlaneSpaces(BigInteger test, changableBI used, int x, int y, int z, Plane plane, int count) {
		used.setBit(to1DCoord(x,y,z));
		count++;
		if (plane.x && x < length-1 && !test.testBit(to1DCoord(x+1,y,z)) && !used.testBit(to1DCoord(x+1,y,z))) count += countEmptyPlaneSpaces(test, used, x+1, y, z, plane, 0);
		if (plane.x && x > 0 && !test.testBit(to1DCoord(x-1,y,z)) && !used.testBit(to1DCoord(x-1,y,z))) count += countEmptyPlaneSpaces(test, used, x-1, y, z, plane, 0);
		if (plane.y && y < width-1 && !test.testBit(to1DCoord(x,y+1,z)) && !used.testBit(to1DCoord(x,y+1,z))) count += countEmptyPlaneSpaces(test, used, x, y+1, z, plane, 0);
		if (plane.y && y > 0 && !test.testBit(to1DCoord(x,y-1,z)) && !used.testBit(to1DCoord(x,y-1,z))) count += countEmptyPlaneSpaces(test, used, x, y-1, z, plane, 0);
		if (plane.z && z < height-1 && !test.testBit(to1DCoord(x,y,z+1)) && !used.testBit(to1DCoord(x,y,z+1))) count += countEmptyPlaneSpaces(test, used, x, y, z+1, plane, 0);
		if (plane.z && z > 0 && !test.testBit(to1DCoord(x,y,z-1)) && !used.testBit(to1DCoord(x,y,z-1))) count += countEmptyPlaneSpaces(test, used, x, y, z-1, plane, 0);
		return count;
	}
	
	class changableBI {
		BigInteger value;
		public changableBI(int value) {
			this.value = BigInteger.valueOf(value);
		}
		public void setBit(int n) {
			value = value.setBit(n);	
		}
		public boolean testBit(int n) {
			return value.testBit(n);
		}
		public BigInteger getvalue() {
			return value;
		}
	}
	
	class dim3count {
		int x_min, y_min, z_min, x_max, y_max, z_max, count;
		
		dim3count() {
			x_min = Integer.MAX_VALUE;
			x_max = Integer.MIN_VALUE;
			y_min = x_min; z_min = x_min;
			y_max = x_max; z_max = x_max;
		}
		
		dim3count(int x, int y, int z) {
			x_min = x; x_max = x;
			y_min = y; y_max = y;
			z_min = z; z_max = z;
			count = 1;
		}
		
		dim3count(int x, int y, int z, int l, int w, int h) {
			this(x, y, z);
			add(new dim3count(l, w, h));
		}
		
		void add(int x, int y, int z) {
			add(new dim3count(x,y,z));
		}
		
		void add(dim3count other) {
			x_min = (x_min <= other.x_min)? x_min : other.x_min;
			x_max = (x_max >= other.x_max)? x_max : other.x_max;
			y_min = (y_min <= other.y_min)? y_min : other.y_min;
			y_max = (y_max >= other.y_max)? y_max : other.y_max;
			z_min = (z_min <= other.z_min)? z_min : other.z_min;
			z_max = (z_max >= other.z_max)? z_max : other.z_max;
			count += other.count;
		}
		
		int getRangeX() { return x_max - x_min; }
		int getRangeY() { return y_max - y_min; }
		int getRangeZ() { return z_max - z_min; }
		int getVolume() { return count; }
	}
	
	private int to1DCoord(int x, int y, int z) {
		return z * width * length + y * length + x;
	}
	
	private boolean test(BigInteger integer, int x, int y, int z) {
		return integer.testBit(to1DCoord(x, y, z));
	}
	private boolean test(changableBI integer, int x, int y, int z) {
		return integer.testBit(to1DCoord(x, y, z));
	}
	
}
