package knapsack;

public class Size3D {

	public final int length;
	public final int width;
	public final int height;
	
	public Size3D(int length, int width, int height) {
		this.length = length;
		this.width  = width;
		this.height = height;
	}
	
	public String toString() {
		return "Size3D ["+length+"x"+width+"x"+height+"]";
	}
	
}
