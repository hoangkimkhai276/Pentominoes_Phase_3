package javafxdraw;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public abstract class SmartGroup extends Group {

	private Rotate rotate;
    private Transform transform = new Rotate();
    protected javafx.geometry.Point3D center;

    public void rotateByX(int angle){
        rotate = new Rotate(angle,Rotate.X_AXIS);
        setCenter();
        transform = transform.createConcatenation(rotate);
        this.getTransforms().clear();
        this.getTransforms().addAll(transform);
    }

	public void rotateByY(int angle){
        rotate = new Rotate(angle,Rotate.Z_AXIS);
        setCenter();
        transform = transform.createConcatenation(rotate);
        this.getTransforms().clear();
        this.getTransforms().addAll(transform);
    }

	public void rotateByZ(int angle) {
        rotate = new Rotate(angle, Rotate.Y_AXIS);
        setCenter();
        transform = transform.createConcatenation(rotate);
        this.getTransforms().clear();
        this.getTransforms().addAll(transform);
    }

    private void setCenter() {
        System.out.println(center);
        rotate.setPivotX(center.getX());
        rotate.setPivotY(center.getY());
        rotate.setPivotZ(center.getZ());
    }
	
}
