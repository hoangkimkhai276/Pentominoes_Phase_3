package javafxdraw;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Translate;
import knapsack.Knapsack;
import knapsack.KnapsackGroup;

public class ContainerPane extends Parent {
    //size of big container
    private final double CONTAINER_DEPTH = 16.5;
    private final double CONTAINER_WIDTH = 2.5;
    private final double CONTAINER_HEIGHT = 4.0;
    //group in which the box, container and camera are added
    private Group root;
    //coordiantes of box 1, row one is the actual coordinates and row two is the width, height, depth
    private double[][] box1 = {{0, 4, 30},
            {1, 2, 1.5}};
    //coordiantes of box 2, row one is the actual coordinates and row two is the width, height, depth
    private double[][] box2 = {{0, 6, 28},
            {1.5, 1, 2}};

    public ContainerPane(int Scene_Width, int Scene_Length){
        //create the group
        root = new Group();
        root.setAutoSizeChildren(false);

        //creating container
        Box container = new Box(CONTAINER_WIDTH , CONTAINER_HEIGHT, CONTAINER_DEPTH);
        container.setCullFace(CullFace.NONE);
        //drawing the container with only lines
        container.setDrawMode(DrawMode.LINE);
        //setting the color of the container
        PhongMaterial material = new PhongMaterial(Color.ORANGE);
        container.setMaterial(material);
        root.getChildren().add(container);

        //create a camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        //add possible rotations and position of camera
        camera.getTransforms().addAll(new Translate(0, 0, -35));
        root.getChildren().add(camera);

        //create a Scene from the group
        SubScene subScene = new SubScene(root, Scene_Width, Scene_Length, true, SceneAntialiasing.BALANCED);
        //set a camera for the scene
        subScene.setCamera(camera);
        getChildren().add(subScene);
    }
    public void drawBox1(){
        //clear everything from root except container and camera
        try{
            root.getChildren().remove(2);
        }
        catch(Exception exception){

        }
        //create box1
        KnapsackGroup box = KnapsackGroup.example;
        box.setTranslateX(-CONTAINER_WIDTH/2 + box.getOutline().getWidth()/2 );
        box.setTranslateY(-CONTAINER_HEIGHT/2 + box.getOutline().getHeight()/2);
        box.setTranslateZ(CONTAINER_DEPTH/2 - box.getOutline().getDepth()/2);
        //add it to the group
        root.getChildren().add(box);
    }


}