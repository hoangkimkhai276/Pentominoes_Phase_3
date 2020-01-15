package knapsack;

/**
 * interface containing common variables so that they can be altered from a single location
 */
public final class Variables {

	/**default length (x-direction) of the knapsack in 0.5 meters */
	public static final int DEFAULT_LENGTH 	= 33;	// 16.5 meter
	/**default width (y-direction) of the knapsack in 0.5 meters */
	public static final int DEFAULT_WIDTH 	= 5;	// 2.5  meter
	/**default height (z-direction) of the knapsack in 0.5 meters */
	public static final int DEFAULT_HEIGHT 	= 8;	// 4.0  meter
	
	public static int to1DCoord(int x, int y, int z) {
		return z * DEFAULT_WIDTH * DEFAULT_LENGTH + y * DEFAULT_LENGTH + x;
	}
	
}
