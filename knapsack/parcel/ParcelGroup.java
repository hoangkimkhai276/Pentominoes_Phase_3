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
	
	public SmartGroup toGroup(Parcel parcel){

        SmartGroup parcelGroup = new SmartGroup();

        Box[] boxes = parcel.toBoxes(50);

        Point3D origin = parcel.getOrigin();

        for (int i = 0; i < boxes.length; i++) {

            System.out.println(boxes[i].getTranslateX());

            parcelGroup.getChildren().add(boxes[i]);

        }

        return parcelGroup;

    }
	
	static class SmartGroup extends Group {

        public SmartGroup(){

        }

        public SmartGroup(Parcel p, double scale){
            Point3D origin = p.getOrigin();
        }
        Point3D origin;
        Rotate r;
        Transform t = new Rotate();

        void rotateByX(int angle){
            r = new Rotate(angle,Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByY(int angle){
            r = new Rotate(angle,Rotate.Z_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByZ(int angle){
            r = new Rotate(angle,Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }
}
