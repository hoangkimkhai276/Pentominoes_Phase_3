package knapsack;

import java.util.ArrayList;

import graphics.SmartGroup;
import javafxstuff.Point3D;
import knapsack.parcel.ParcelGroup;

public class KnapsackGroup extends SmartGroup {

	private Knapsack knapsack;
	private double scale;
	
	public KnapsackGroup(Knapsack knapsack, double scale, Point3D origin) {
		super(origin);
		this.knapsack = knapsack;
		this.scale = scale;
		ArrayList<ParcelGroup> groups = knapsack.getParcelGroups(scale);
		for (ParcelGroup group : groups)
            this.getChildren().add(group);
	}
	
}
