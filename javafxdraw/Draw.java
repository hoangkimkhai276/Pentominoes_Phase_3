package javafxdraw;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafxstuff.Point3D;
import knapsack.Knapsack;
import knapsack.parcel.Parcel;
import knapsack.parcel.ParcelCore;
import knapsack.parcel.Parcels;
import knapsack.parcel.SimpleParcel;

public class Draw extends Application {
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
//
        run(primaryStage);

    }
    public void run(Stage primaryStage){
        Camera camera = new PerspectiveCamera();
        SmartGroup mainGroup = new SmartGroup();
        SmartGroup p_group = toGroup(Parcels.P);
        SmartGroup t_group = toGroup(Parcels.T);
        SmartGroup l_group = toGroup(Parcels.L);
        p_group.translateXProperty().set(200);
        p_group.translateYProperty().set(200);
        t_group.translateXProperty().set(400);
        t_group.translateYProperty().set(400);
        l_group.translateXProperty().set(600);
        l_group.translateYProperty().set(300);

        SmartGroup[] smartGroups = new SmartGroup[2];
        smartGroups[0] = p_group;
        smartGroups[1] = t_group;
        mainGroup.getChildren().addAll(p_group,t_group,l_group);

        Scene scene =new Scene(mainGroup,1000,500,true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);
        System.out.println(mainGroup.getLayoutX()+  " dlksahdksa");
        System.out.println(mainGroup.getLayoutY() + "dsadsa");
        addEventHandler(primaryStage,p_group);

        primaryStage.setTitle("Khai");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
    class SmartGroup extends Group{
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
    private void addEventHandler(Stage primaryStage, SmartGroup group){
        int angle = 90;
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent t) {
                        switch (t.getCode()){
                            case Q:
                                group.rotateByX(-angle );
                                break;
                            case E:
                                group.rotateByX(angle);
                                break;
                            case W:
                                group.rotateByZ(angle);
                                break;
                            case S:
                                group.rotateByZ(-angle);
                                break;
                            case A:
                                group.rotateByY(angle);
                                break;
                            case D:
                                group.rotateByY(-angle);
                                break;
                            case NUMPAD4:
                                group.translateXProperty().set((group.getTranslateX()-10));
                                System.out.println("Moving left" + group.translateXProperty().get());
                                break;
                            case NUMPAD6:
                                group.translateXProperty().set((group.getTranslateX())+10);
                                System.out.println("Moving right" + group.translateXProperty().get());
                                break;

                        }
                    }
                }
        );
    }
    private SmartGroup toGroup(Parcel parcel){
        SmartGroup parcelGroup = new SmartGroup();
        Box[] boxes = parcel.toBoxes(50);
        Point3D origin = parcel.getOrigin();
        for (int i = 0; i < boxes.length; i++) {
            System.out.println(boxes[i].getTranslateX());
            parcelGroup.getChildren().add(boxes[i]);
        }
        return parcelGroup;
    }





}
