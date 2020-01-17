package javafxdraw;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import knapsack.Knapsack;
import knapsack.KnapsackGroup;
import knapsack.parcel.ParcelGroup;
import knapsack.parcel.Parcels;

public class Draw extends Application {
    private static final double WIDTH = 800;
    private static final double HEIGHT = 800;
    private static final int angle = 5;
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private Scene scene;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

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
        draw(primaryStage);

    }

    private void draw(Stage primaryStage) {

        Group root = new Group();
        KnapsackGroup knapsackGroup = KnapsackGroup.example;
        PerspectiveCamera camera = new PerspectiveCamera();
        scene = new Scene(root, WIDTH, HEIGHT, true);

        camera.setTranslateZ(-500);
        root.getChildren().addAll(knapsackGroup);

        knapsackGroup.setTranslateX(WIDTH / 2 - 100);
        knapsackGroup.setTranslateY(HEIGHT / 2);
        scene.setCamera(camera);

        initMouseControl(knapsackGroup, scene, primaryStage);
        addEventHandler(primaryStage, knapsackGroup);

        primaryStage.setTitle("Parcelminoes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initMouseControl(KnapsackGroup knapsackGroup, Scene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        knapsackGroup.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)

        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });
        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + (anchorX - event.getSceneX()));
        });
        stage.addEventHandler(ScrollEvent.SCROLL,event ->{
            double delta = event.getDeltaY();
            knapsackGroup.setTranslateZ(knapsackGroup.getTranslateZ() + delta);
        });
    }




    private void addEventHandler(Stage primaryStage, KnapsackGroup group) {
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent t) {
                        switch (t.getCode()) {
                            case Z:
                                KnapsackGroup g = new KnapsackGroup(KnapsackGroup.test2, 20);
                                group.setParcelGroups(g.getParcelGroups());
                                break;
                            case Q:
                                group.rotateByX(-angle);
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
                                group.translateXProperty().set((group.getTranslateX() - 10));
                                System.out.println("Moving left" + group.translateXProperty().get());
                                break;
                            case NUMPAD6:
                                group.translateXProperty().set((group.getTranslateX()) + 10);
                                System.out.println("Moving right" + group.translateXProperty().get());
                                break;
                            case NUMPAD8:
                                group.translateZProperty().set((group.getTranslateZ()) + 10);
                                System.out.println("Moving forward" + group.translateXProperty().get());
                                break;
                            case NUMPAD2:
                                group.translateZProperty().set((group.getTranslateZ()) - 10);
                                System.out.println("Moving backward" + group.translateXProperty().get());
                                break;
                            default:
                                break;
                        }
                    }
                }
        );

    }

    public Scene getScene() {
        return this.scene;
    }


    private void rotateByX(KnapsackGroup knapsackGroup, int angle){
        knapsackGroup.rotateByX(angle);
    }

    private void rotateByY(KnapsackGroup knapsackGroup, int angle){
        knapsackGroup.rotateByY(angle);
    }

    private void rotateByZ(KnapsackGroup knapsackGroup, int angle){
        knapsackGroup.rotateByZ(angle);
    }

}