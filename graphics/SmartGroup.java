package graphics;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafxstuff.Point3D;

public abstract class SmartGroup extends Group {

	private Rotate rotate;
    private Transform transform = new Rotate();
	private Point3D origin;
    
    protected SmartGroup(Point3D origin) {
    	this.origin = origin;
    	updateTranslation();
    }
    
    protected void updateTranslation() {
    	this.setTranslateX(origin.getX());
    	this.setTranslateY(origin.getY());
    	this.setTranslateZ(origin.getZ());
    }
    
    public void move(Point3D delta) {
    	origin = origin.add(delta);
    	updateTranslation();
    }
    
    public void setLocation(Point3D new_point) {
    	move(new_point.subtract(origin));
    }
    
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
