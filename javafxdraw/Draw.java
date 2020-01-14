package javafxdraw;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafxstuff.Point3D;
import knapsack.KnapsackGroup;
import knapsack.parcel.ParcelGroup;
import knapsack.parcel.Parcels;

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
        Group root  = new Group();
        KnapsackGroup knapsackGroup = KnapsackGroup.example;
        PerspectiveCamera camera = new PerspectiveCamera();
        knapsackGroup.setTranslateZ(-500);
        Box b = knapsackGroup.getOutline();
        b.setDrawMode(DrawMode.LINE);
        Bounds layoutBounds = b.getLayoutBounds();
        Bounds parentBounds = b.getBoundsInParent();
        Bounds localBounds = b.getBoundsInParent();
        System.out.println("LAYOUT BOUNDS: " + layoutBounds.getDepth() + " " + layoutBounds.getHeight() + " " + layoutBounds.getWidth());
        System.out.println("LAYOUT BOUNDS: " + layoutBounds.getMaxZ() + " " + layoutBounds.getMaxY() + " " + layoutBounds.getMaxX());
        System.out.println("PARENT BOUNDS: " + parentBounds.getDepth() + " " + parentBounds.getHeight() + " " + parentBounds.getWidth());
        System.out.println("PARENT BOUNDS: " + parentBounds.getMaxZ() + " " + parentBounds.getMaxY() + " " + parentBounds.getMaxX());
        System.out.println("LOCAL BOUNDS: " + localBounds.getDepth() + " " + localBounds.getHeight() + " " + localBounds.getWidth());
        System.out.println("LOCAL BOUNDS: " + localBounds.getMaxZ() + " " + localBounds.getMaxY() + " " + localBounds.getMaxX());
        root.getChildren().addAll(b,knapsackGroup);
        Scene scene = new Scene(root, 1000,800,true);
        scene.setCamera(camera);
        addEventHandler(primaryStage, knapsackGroup);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    private void run(Stage primaryStage){
        ParcelGroup parcelGroup = new ParcelGroup(Parcels.P,50,new Point3D(200,200,0));
        PerspectiveCamera camera = new PerspectiveCamera();
//        addEventHandler(primaryStage,parcelGroup);
        Group mainGroup = new Group();
        Line line = new Line(200,0,200,800);
        mainGroup.getChildren().addAll(line,parcelGroup);
        Scene scene = new Scene(mainGroup,1000,800,true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void addEventHandler(Stage primaryStage, KnapsackGroup group){
        int angle = 5;
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent t) {
                        switch (t.getCode()){
                            case Q:
//                                System.out.println(group.getOrigin().getX() + ", " +group.getOrigin().getY() + ", " +group.getOrigin().getZ());
                                group.rotateByX(-angle );
                                break;
                            case E:
                                group.rotateByX(angle);
                                break;
                            case W:
//                                System.out.println(group.getOrigin().getX() + ", " +group.getOrigin().getY() + ", " +group.getOrigin().getZ());
                                group.rotateByZ(angle);
                                break;
                            case S:
                                group.rotateByZ(-angle);
                                break;
                            case A:
//                                System.out.println(group.getOrigin().getX() + ", " +group.getOrigin().getY() + ", " +group.getOrigin().getZ());
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



}
