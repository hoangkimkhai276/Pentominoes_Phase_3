package knapsack.parcel;

import java.awt.Color;

import javafx.scene.shape.Box;
import javafxdraw.SmartGroup;

public class ParcelGroup extends SmartGroup {

	public static final ParcelGroup A = new ParcelGroup(Parcels.A, 50);
	public static final ParcelGroup B = new ParcelGroup(Parcels.B, 50);
	public static final ParcelGroup C = new ParcelGroup(Parcels.C, 50);
	public static final ParcelGroup P = new ParcelGroup(Parcels.P, 50);
	public static final ParcelGroup L = new ParcelGroup(Parcels.L, 50);
	public static final ParcelGroup T = new ParcelGroup(Parcels.T, 50);
	
	private Parcel parcel;
	private Color color;
	private double scale;
    
    public ParcelGroup(Parcel _parcel, double _scale) {
    	parcel = _parcel;
    	color = parcel.getColor();
    	scale = _scale;
    	Box[] boxes = parcel.toBoxes(scale);
        for (int i = 0; i < boxes.length; i++)
            this.getChildren().add(boxes[i]);
        if (boxes.length==1) {
			boxes[0].setTranslateX(boxes[0].getWidth()/2d);
			boxes[0].setTranslateY(-boxes[0].getHeight()/2d);
			boxes[0].setTranslateZ(boxes[0].getDepth()/2d);
		} else {
			this.setTranslateX(scale/2d);
			this.setTranslateY(-scale/2d);
			this.setTranslateZ(scale/2d);
		}
		this.setTranslateX(this.getTranslateX() + parcel.getOrigin().getY() * scale);
		this.setTranslateY(this.getTranslateY() - parcel.getOrigin().getZ() * scale);
		this.setTranslateZ(this.getTranslateZ() + parcel.getOrigin().getX() * scale);
    }
    
    public javafx.scene.paint.Color getColor() {
    	return new javafx.scene.paint.Color(color.getRed()/255d, color.getGreen()/255d, color.getBlue()/255d, color.getAlpha()/255d);
    }
}
