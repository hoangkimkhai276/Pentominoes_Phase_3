package knapsack;

import java.util.ArrayList;

import graphics.SmartGroup;
import javafx.scene.shape.Box;
import javafxstuff.Point3D;
import knapsack.parcel.ParcelCore;
import knapsack.parcel.ParcelGroup;
import knapsack.parcel.Parcels;

public class KnapsackGroup extends SmartGroup {
	
	private static final Knapsack test = new Knapsack();
	static {
		ParcelCore p1 = Parcels.A.copy();
		ParcelCore p2 = Parcels.P.copy();
		ParcelCore p3 = Parcels.T.copy();
		ParcelCore p4 = Parcels.L.copy();
		ParcelCore p5 = Parcels.A.copy();
		ParcelCore p6 = Parcels.B.copy();
		ParcelCore p7 = Parcels.C.copy();
		ParcelCore p8 = Parcels.P.copy();
		ParcelCore p9 = Parcels.T.copy();
		p1.moveParcel(new Point3D(1,0,1));
		p2.moveParcel(new Point3D(8,0,2));
		p3.moveParcel(new Point3D(3,4,0));
		p4.moveParcel(new Point3D(6,2,1));
		p5.moveParcel(new Point3D(8,2,0));
		p6.moveParcel(new Point3D(10,1,0));
		p7.moveParcel(new Point3D(14,2,1));
		p8.moveParcel(new Point3D(23,1,0));
		p9.moveParcel(new Point3D(28,0,0));
		test.putParcel(p1);
		test.putParcel(p2);
		test.putParcel(p3);
		test.putParcel(p4);
		test.putParcel(p5);
		test.putParcel(p6);
		test.putParcel(p7);
		p8.rotateWidth();
		test.putParcel(p8);
		test.putParcel(p9);
	}
	public static final KnapsackGroup example = new KnapsackGroup(test, 50, new Point3D(200,200,-50));

	private Knapsack knapsack;
	private double scale;
	private ArrayList<ParcelGroup> groups;
	private Box outline;
	
	public KnapsackGroup(Knapsack knapsack, double scale, Point3D origin) {
		super(origin);
		this.knapsack = knapsack;
		this.scale = scale;
		this.outline = knapsack.toBox(scale);
		groups = knapsack.getParcelGroups(scale);
		for (ParcelGroup group : groups)
            this.getChildren().add(group);
	}
	
	public Box getOutline() {
		return outline;
	}
	
	public ArrayList<ParcelGroup> getParcelGroups() {
		return groups;
	}
	
}
