package graphics;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class SmartGroup extends Group {

	private Rotate rotate;
    private Transform transform = new Rotate();
	
	public void rotateByX(int angle){
        rotate = new Rotate(angle,Rotate.X_AXIS);
        transform = transform.createConcatenation(rotate);
        this.getTransforms().clear();
        this.getTransforms().addAll(transform);
    }

	public void rotateByY(int angle){
        rotate = new Rotate(angle,Rotate.Z_AXIS);
        transform = transform.createConcatenation(rotate);
        this.getTransforms().clear();
        this.getTransforms().addAll(transform);
    }

	public void rotateByZ(int angle){
        rotate = new Rotate(angle,Rotate.Y_AXIS);
        transform = transform.createConcatenation(rotate);
        this.getTransforms().clear();
        this.getTransforms().addAll(transform);
    }
	
}
