package knapsack.parcel;

import java.awt.Color;

import graphics.SmartGroup;
import javafx.scene.shape.Box;
import javafxstuff.Point3D;

public class ParcelGroup extends SmartGroup {

	public static final ParcelGroup A = new ParcelGroup(Parcels.A, 50, Point3D.ZERO);
	public static final ParcelGroup B = new ParcelGroup(Parcels.B, 50, Point3D.ZERO);
	public static final ParcelGroup C = new ParcelGroup(Parcels.C, 50, Point3D.ZERO);
	public static final ParcelGroup P = new ParcelGroup(Parcels.P, 50, Point3D.ZERO);
	public static final ParcelGroup L = new ParcelGroup(Parcels.L, 50, Point3D.ZERO);
	public static final ParcelGroup T = new ParcelGroup(Parcels.T, 50, Point3D.ZERO);
	
	private Parcel parcel;
	private Color color;
	private double scale;
    
    public ParcelGroup(Parcel _parcel, double _scale, Point3D origin_adjustment) {
    	super(_parcel.getOrigin().scale(_scale).add(origin_adjustment));
    	parcel = _parcel;
    	color = parcel.getColor();
    	scale = _scale;
    	Box[] boxes = parcel.toBoxes(scale);
        for (int i = 0; i < boxes.length; i++)
            this.getChildren().add(boxes[i]);
    }
    
    public javafx.scene.paint.Color getColor() {
    	return new javafx.scene.paint.Color(color.getRed()/255d, color.getGreen()/255d, color.getBlue()/255d, color.getAlpha()/255d);
    }



}
