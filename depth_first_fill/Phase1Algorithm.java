package depth_first_fill;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import knapsack.Knapsack;
import knapsack.parcel.Parcel;
import knapsack.parcel.ParcelCore;
import knapsack.parcel.ParcelType;
import knapsack.parcel.Parcels;

enum Plane {
	XY(true,true,false), XZ(true,false,true), YZ(false,true,true);
	public boolean x;
	public boolean y;
	public boolean z;
	Plane(boolean x, boolean y, boolean z) {
		this.x = x; this.y = y; this.z = z;
	}
}

public class Phase1Algorithm {

	private final int length;
	private final int width;
	private final int height;
	public Knapsack knapsack;
	public ParcelType[] parcel_types;
	
	public Phase1Algorithm(Knapsack knapsack, ParcelCore... parcels) {
		this.knapsack = knapsack;
		parcel_types = makePermutationTypes(parcels);
		length = knapsack.getLength();
		width = knapsack.getWidth();
		height = knapsack.getHeight();
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
	
	/**
	 * This is the main algorithm behind all this. It is a depth-first branching algorithm that prioritizes on fitting the pentominoes in
	 *  all the possible spaces to find a solution. The reason it does this instead of trying to fill each square in the grid seperately is
	 *  so we can use the simple pruning algorithm we'd like to call "recursive open-space cut-off" ({@link Solver.mightBeSolvable})
	 *  How this works: <br>
	 *  1. start at depth == 1; the temporary_solution ({@code tempsol}) is {@code EMPTY}; [pentomino-index pi = 0] <br>
	 * 	->	2. if we are not yet on a leaf node: <br>
	 * 	-------- for each pentomino-permutation: <br>
	 * 	------------ for each x in {@code tempsol} <br>
	 * 	---------------- for each y in {@code tempsol} <br>
	 * 	--------------------- if pentomino-permutation fits --> <br>
	 * 	------------------------> if !needToPrune --> <br>
	 * 	---------------------------> if {@code found_solution} return true; <br>
	 * 	---------------------------> else do everything of step (2) for the next pentomino [pi++] and store the position of old_pi <br>
	 *  3. if not_yet_found_solution --> <br>
	 * 	-----> return false; <br>
	 *
	* @param database : all your pentomino-permutations
	* @param tempsol starts as {@code EMPTY}, 'temporary solution'
	* @param depth starts at 1
	* @param size : (width*height/5), basically describes how many pentominoes fit inside the grid in total
	* @return
	*/
	@SuppressWarnings("unused")
	private boolean solveRecursive(PentoPermutation[][] database, int[][] tempsol, int depth, int size) {

		if (SHOW) display(tempsol, SLEEPTIME);

		int pento = depth-1;
		debug("depth= "+depth+", size= "+size);
		if (depth <= size) {
			debug("not at leaf node yet");
			trypento: for (int p=0; p < database[pento].length; p++) {
				debug("trying pentomino nr. "+database[pento][p].ID+", permutation "+p);

				rows: for (int x=0; x < tempsol.length - database[pento][p].width +1; x++) {
					cols: for (int y=0; y < tempsol[0].length - database[pento][p].height +1; y++) {

						pos: {
							debug("now trying "+x+", "+y+" for "+database[pento][p].ID+", permutation "+p);

							if (!database[pento][p].fitsAt(tempsol, x, y)) break pos;
							int[][] tempsol2 = Functions.arrayCopy(tempsol);
							database[pento][p].placeIn(tempsol2, x, y);
							if (pento!=size && !mightBeSolvable(tempsol2)) break pos;
							else if (isSolved(tempsol2)) {
								Functions.cloneArrayToArray(tempsol2, tempsol);
								return true;
							}
							if (solveRecursive(database, tempsol2, depth+1, size)) {
								Functions.cloneArrayToArray(tempsol2, tempsol);
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * @param m : grid
	 * @return {@code true} if the grid is filled with non-EMPTY tiles.
	 */
	private boolean isSolved(int[][] m) {
		for (int i=0; i < m.length; i++)
			for (int j=0; j < m[i].length; j++)
				if (m[i][j]==-1) return false;
		return true;
	}

	void display(int[][] temp, int sleep) {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ui.setState(temp);
	}

	/**
	 * A fillable part is an 'area' in the array that is all connected zeros where the total number of connected zeros in that
	 * 	area is divisible by 5. The reason for this is that if a given placement of a pentomino would cut-off a section that has
	 *  a connected area of 1, 2, 3, 4, 6, 7, etc., you could <b>never</b> fill it with pentominoes... So we prune that entire
	 *  branch right off the search tree using this!
	 * @param test is the temporary situation of the rectangle (with the current check-pentomino in there)
	 * @return true if it divides the array up into fillable parts, false otherwise
	 */
	private boolean mightBeSolvable(BigInteger test) {
		boolean[][] used = new boolean[width][height];
		for (int i=0; i < width; i++) {
			for (int j=0; j < height; j++) {
				test: {
					if (used[i][j] || test[i][j]!=EMPTY) break test;
					boolean valid = true;
					valid = checkFreedom(test, used, i, j);
					if (!valid) {
						debug("It is NOT solvable");
						return false;
					}
				}
			}
		} debug("It IS solvable");
		return true;
	}

	/**
	 * This recursive method will check whether an area from a certain point is divisible by 5 (can you fill it with pentonimos?)
	 * @param test : the temporary situation of the plane (with the current check-pentomino in there)
	 * @param used : all the elements that are already checked before by the {@link #mightBeSolvable} method.
	 * @param x position
	 * @param y position
	 * @param z position
	 * @param plane the plane at which the freedom will be checked
	 * @return {@code true} if it can theoretically be filled with pentominoes
	 */
	private boolean checkPentoFreedom(BigInteger test, BigInteger used, int x, int y, int z, Plane plane) {
		return countEmptyPlaneSpaces(test, used, x, y, z, plane, 0) %5 == 0;
	}
	
	private boolean check3DFreedom(BigInteger test, BigInteger used, int x, int y, int z,) {
		
	}
	
	private boolean canBeFilled(Parcel a, Parcel b, Parcel c, int volume) {
		// TODO implement tiny algorithm for tiny knapsack problem
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
	@SuppressWarnings("unused")
	private int countEmptySpaces(BigInteger test, BigInteger used, int x, int y, int z, int count) {
		used.setBit(to1DCoord(x,y,z));
		count++;
		if (x < length-1 && !test.testBit(to1DCoord(x+1,y,z)) && !used.testBit(to1DCoord(x+1,y,z))) count += countEmptySpaces(test, used, x+1, y, z, 0);
		if (x > 0 && !test.testBit(to1DCoord(x-1,y,z)) && !used.testBit(to1DCoord(x-1,y,z))) count += countEmptySpaces(test, used, x-1, y, z, 0);
		if (y < width-1 && !test.testBit(to1DCoord(x,y+1,z)) && !used.testBit(to1DCoord(x,y+1,z))) count += countEmptySpaces(test, used, x, y+1, z, 0);
		if (y > 0 && !test.testBit(to1DCoord(x,y-1,z)) && !used.testBit(to1DCoord(x,y-1,z))) count += countEmptySpaces(test, used, x, y-1, z, 0);
		if (z < height-1 && !test.testBit(to1DCoord(x,y,z+1)) && !used.testBit(to1DCoord(x,y,z+1))) count += countEmptySpaces(test, used, x, y, z+1, 0);
		if (z > 0 && !test.testBit(to1DCoord(x,y,z-1)) && !used.testBit(to1DCoord(x,y,z-1))) count += countEmptySpaces(test, used, x, y, z-1, 0);
		return count;
	}
	
	@SuppressWarnings("unused")
	private int countEmptyPlaneSpaces(BigInteger test, BigInteger used, int x, int y, int z, Plane plane, int count) {
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
	
	private int to1DCoord(int x, int y, int z) {
		return z * width * length + y * length + x;
	}
	
}
