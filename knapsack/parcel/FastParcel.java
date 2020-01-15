package knapsack.parcel;

import java.util.ArrayList;
import static knapsack.Variables.*;
import javafxstuff.Point3D;
import knapsack.Knapsack;
import knapsack.Variables;

public class FastParcel implements Parcel {

	private static ArrayList<int[]> stored_parcel_shapes;
	private static boolean initiated = false;
	private static Knapsack reference;
	
	private static void initiate(Knapsack knapsack, Parcel[] basic_form_parcels) {
		reference = knapsack;
		stored_parcel_shapes = new ArrayList<int[]>((12+24+24+1+3+6)*reference.getLength()*reference.getWidth()*reference.getHeight());
		Parcel[] permutations = Parcels.
		initiated = true;
	}
	
	public static int[] generateParcelShape(Parcel parcel) {
		Point3D[] points = parcel.getOccupiedGrids();
		int[] shape = new int[parcel.getVolume()];
		for (int i=0; i < shape.length; i++) shape[i] = reference.to1DCoord(points[i].getX(), points[i].getY(), points[i].getZ());
		return shape;
	}
	
}
