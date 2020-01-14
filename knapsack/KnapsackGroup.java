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

        ParcelCore p3 = Parcels.L.copy();
        ParcelCore p4 = Parcels.T.copy();
        ParcelCore p5 = Parcels.L.copy();
        ParcelCore p6 = Parcels.P.copy();
        ParcelCore p7 = Parcels.T.copy();
        ParcelCore p8 = Parcels.L.copy();
        ParcelCore p9 = Parcels.P.copy();

        ParcelCore p1 = Parcels.T.copy();
        ParcelCore p2 = Parcels.L.copy();
        p1.moveParcel(new Point3D(0, 2, 0));
        p2.moveParcel(new Point3D(8, 3, 0));
        p3.moveParcel(new Point3D(25, 3, 0));
        p4.moveParcel(new Point3D(6, 3, 0));
        p5.moveParcel(new Point3D(8, 3, 0));
        p6.moveParcel(new Point3D(10, 3, 0));
        p7.moveParcel(new Point3D(14, 3, 0));
        p8.moveParcel(new Point3D(20, 3, 0));
        p9.moveParcel(new Point3D(31, 3, 0));

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

    public static final KnapsackGroup example = new KnapsackGroup(test, 20);

    private Knapsack knapsack;
    private double scale;
    private ArrayList<ParcelGroup> groups;
    private Box outline;

    public KnapsackGroup(Knapsack knapsack, double scale) {
        this.knapsack = knapsack;
        this.scale = scale;
        outline = knapsack.toBox(scale);
        groups = knapsack.getParcelGroups(scale);
        this.getChildren().add(outline);
        for (ParcelGroup group : groups)
            this.getChildren().add(group);
        center = javafx.geometry.Point3D.ZERO.midpoint(knapsack.getWidth() * scale, knapsack.getHeight() * scale, knapsack.getLength() * scale);
    }

    public Box getOutline() {
    	return outline;
    }

    public ArrayList<ParcelGroup> getParcelGroups() {
        return groups;
    }

}
