package javafxdraw;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class DragGroupSample extends Application {

    public static void main( String[] args ) {
        launch();
    }

    private void makeDraggable( Circle circle ) {
        DragContext dragContext = new DragContext();

        // --- remember initial coordinates of mouse cursor and node
        circle.addEventFilter( MouseEvent.MOUSE_PRESSED, (
                final MouseEvent mouseEvent ) -> {
            dragContext.dx = mouseEvent.getX() - circle.getCenterX();
            dragContext.dy = mouseEvent.getY() - circle.getCenterY();
        } );

        // --- Shift node calculated from mouse cursor movement
        circle.addEventFilter( MouseEvent.MOUSE_DRAGGED, (
                final MouseEvent mouseEvent ) -> {
            circle.setCenterX( mouseEvent.getX() + dragContext.dx );
            circle.setCenterY( mouseEvent.getY() + dragContext.dy );
        } );

        // --- Drop card onto allowed target field
        circle.addEventFilter( MouseEvent.MOUSE_RELEASED, (
                final MouseEvent mouseEvent ) -> {
            circle.setCenterX( mouseEvent.getX() + dragContext.dx );
            circle.setCenterY( mouseEvent.getY() + dragContext.dy );
        } );
    }

    @Override
    public void start( Stage primaryStage ) throws Exception {
        Circle[] circles = new Circle[3];
        circles[0] = new Circle( 30.0, 30.0, 30.0, Color.RED );
        circles[1] = new Circle( 45.0, 45.0, 30.0, Color.GREEN );
        circles[2] = new Circle( 60.0, 60.0, 30.0, Color.BLUE );
        for ( Circle circle : circles ) {
            makeDraggable( circle );
        }

        Group root = new Group();
        root.getChildren().addAll( circles[0], circles[1], circles[2] );
        primaryStage.setResizable( false );
        primaryStage.setScene( new Scene( root, 400, 350 ) );
        primaryStage.setTitle( DragGroupSample.class.getSimpleName() );
        primaryStage.show();
    }

    private static final class DragContext {
        public double dx, dy;
    }
}