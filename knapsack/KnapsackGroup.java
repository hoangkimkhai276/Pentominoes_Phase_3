package knapsack;

import graphics.SmartGroup;
import javafxstuff.Point3D;

public class KnapsackGroup extends SmartGroup {

	private Knapsack knapsack;
	
	public KnapsackGroup(Knapsack knapsack, double scale, Point3D origin) {
		this.knapsack = knapsack;
	}
	
}
