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
