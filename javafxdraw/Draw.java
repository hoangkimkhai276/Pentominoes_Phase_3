package javafxdraw;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
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
        SmartGroup group = new SmartGroup();
        Camera camera = new PerspectiveCamera();
        Group c_group = new Group();
        group.translateXProperty().set(100);
        group.translateYProperty().set(100);
        Parcel P = Parcels.P;
        Box[] boxes = P.toBoxes(50);
        for (int i = 0; i < boxes.length; i++) {
            System.out.println(boxes[i].getTranslateX());
            group.getChildren().add(boxes[i]);

        }

        SmartGroup t_group = new SmartGroup();
        Parcel T = Parcels.T;
        Box[] t_boxes = T.toBoxes(50);
        for(int i = 0; i < t_boxes.length; i++){
            t_group.getChildren().add(t_boxes[i]);
        }
        t_group.translateXProperty().set(400);
        t_group.translateYProperty().set(200);

        c_group.getChildren().addAll(group,t_group);
        Scene scene =new Scene(c_group,1000,500,true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);
//        Box box = new Box(100,100,100);
//        group.getChildren().add(box);
//        Scene scene = new Scene(group, 1400, 600);


        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent t) {
                        switch (t.getCode()){
                            case KP_UP:
                                System.out.println(group.getTranslateZ()+" ");
                                group.translateZProperty().set((group.getTranslateZ()+10));
                                break;
                            case KP_DOWN:
                                group.translateZProperty().set(group.getTranslateZ()-10);
                                break;
                            case Q:
                                group.rotateByX(-90 );
                                break;
                            case E:
                                group.rotateByX(90);
                                break;
                            case W:
                                group.rotateByZ(90);
                                break;
                            case S:
                                group.rotateByZ(-90);
                                break;
                            case A:
                                group.rotateByY(90);
                                break;
                            case D:
                                group.rotateByY(-90);
                                break;
                            case NUMPAD4:
                                System.out.println("Moving left");
                                group.translateXProperty().set((group.getTranslateX()-10));
                                break;
                            case NUMPAD6:
                                System.out.println("Moving right");
                                group.translateXProperty().set((group.getTranslateX())+10);
                                break;

                        }
                    }
                }
        );


        primaryStage.setTitle("Khai");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    class SmartGroup extends Group{
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
        void moveX(int dis){

        }

    }
}
