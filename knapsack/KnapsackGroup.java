package knapsack;

import java.util.ArrayList;

import javafx.scene.shape.Box;
import javafxdraw.SmartGroup;
import javafxstuff.Point3D;
import knapsack.parcel.ParcelCore;
import knapsack.parcel.ParcelGroup;
import knapsack.parcel.Parcels;

public class KnapsackGroup extends SmartGroup {

    private static final Knapsack test = new Knapsack();
    public static final Knapsack test2 = test.getEmpty();

    static {

        ParcelCore p3 = Parcels.A.copy();
        ParcelCore p4 = Parcels.A.copy();
        ParcelCore p5 = Parcels.B.copy();
        ParcelCore p6 = Parcels.B.copy();
        ParcelCore p7 = Parcels.C.copy();
        ParcelCore p8 = Parcels.C.copy();
        ParcelCore p9 = Parcels.P.copy();
        ParcelCore p1 = Parcels.T.copy();
        ParcelCore p2 = Parcels.L.copy();
        ParcelCore p11 = Parcels.C.copy();
        p11.moveParcel(new Point3D(0,2,1));
        p1.moveParcel(new Point3D(0, 2, 0));
        p2.moveParcel(new Point3D(8, 3, 0));
        p3.moveParcel(new Point3D(15, 3, 0));
        p4.moveParcel(new Point3D(6, 3, 0));
        p5.moveParcel(new Point3D(8, 3, 0));
        p6.moveParcel(new Point3D(10, 3, 0));
        p7.moveParcel(new Point3D(14, 3, 0));
        p8.moveParcel(new Point3D(5, 3, 0));
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

        ParcelCore p10 = Parcels.C.copy();
        p10.moveParcel(new Point3D(20,0,0));
        test2.putParcel(p11);
        test2.putParcel(p10);
    }

    public static final KnapsackGroup example = new KnapsackGroup(test, 20);

    Knapsack knapsack;
    double scale;
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

    public void setParcelGroups(ArrayList<ParcelGroup> list) {
        this.getChildren().clear();
        for (ParcelGroup group : list)
            this.getChildren().add(group);
        this.getChildren().add(outline);

    }

    public Knapsack getKnapsack() {
        return knapsack;
    }

}
