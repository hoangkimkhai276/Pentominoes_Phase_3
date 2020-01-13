package knapsack.parcel;

import java.awt.Color;

import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafxstuff.Point3D;

public class ParcelGroup {

	private Parcel parcel;
	private Point3D adjusted_origin_position;
	private Color color;
	private Group group;
	private double scale;
	
	private Rotate rotate;
    private Transform transform;
    
    public ParcelGroup(Parcel _parcel, double _scale, Point3D origin_of_knapsack) {
    	group = new Group();
    	parcel = _parcel;
    	color = parcel.getColor();
    	scale = _scale;
    	Box[] boxes = parcel.toBoxes(scale);
        adjusted_origin_position = parcel.getOrigin().scale(scale).add(origin_of_knapsack);
        for (int i = 0; i < boxes.length; i++)
            group.getChildren().add(boxes[i]);
        updateTranslation();
    }
    
    public void move(Point3D delta) {
    	adjusted_origin_position = adjusted_origin_position.add(delta);
    	updateTranslation();
    }
    
    public void setLocation(Point3D new_point) {
    	move(new_point.subtract(adjusted_origin_position));
    }
    
    public javafx.scene.paint.Color getColor() {
    	return new javafx.scene.paint.Color(color.getRed()/255d, color.getGreen()/255d, color.getBlue()/255d, color.getAlpha()/255d);
    }
    
    private void updateTranslation() {
    	group.setTranslateX(adjusted_origin_position.getX());
    	group.setTranslateY(adjusted_origin_position.getY());
    	group.setTranslateZ(adjusted_origin_position.getZ());
    }

    void rotateByX(int angle){
        rotate = new Rotate(angle,Rotate.X_AXIS);
        transform = transform.createConcatenation(rotate);
        group.getTransforms().clear();
        group.getTransforms().addAll(transform);
    }

    void rotateByY(int angle){
        rotate = new Rotate(angle,Rotate.Z_AXIS);
        transform = transform.createConcatenation(rotate);
        group.getTransforms().clear();
        group.getTransforms().addAll(transform);
    }

    void rotateByZ(int angle){
        rotate = new Rotate(angle,Rotate.Y_AXIS);
        transform = transform.createConcatenation(rotate);
        group.getTransforms().clear();
        group.getTransforms().addAll(transform);
    }
}
