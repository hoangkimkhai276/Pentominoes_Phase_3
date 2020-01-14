package javafxdraw;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Error extends Application {

@Override
public void start(Stage primaryStage) {
    ContainerPane container = new ContainerPane(750, 750);
    Scene scene = new Scene(container);
    scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){
        @Override
        public void handle(KeyEvent event){
           if(event.getCode() == KeyCode.X){
               container.drawBox1();
           }

    }});
    primaryStage.setScene(scene);
    primaryStage.show();
}

/**
 * @param args the command line arguments
 */
public static void main(String[] args) {
    launch(args);
}

}