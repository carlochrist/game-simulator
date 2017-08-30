package Visualization;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.TextArea;
import java.io.File;
import java.io.IOException;

/**
 * Created by Carlo on 04.08.2017.
 */

public class Visualization extends Application {
//
//    private static Visualization myInstance = null;
//    private VisualizationController visualizationController;
//    private FXMLLoader fxmlLoader;
//    private Parent root;
//
//    /*public static void main(String[] args){
//        launch(args);
//    }*/
//
//    private Visualization(){
//        //fxmlLoader = new FXMLLoader(getClass().getResource("visualization.fxml"));
//        //visualizationController =(VisualizationController) fxmlLoader.getController();
//    }
//
//    public void startGUI(String[] args){
//        launch(args);
//    }
//
//    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("visualization.fxml"));
//
//
//
//
//            //root = fxmlLoader.load();
//
//
//        primaryStage.setTitle("Visualization");
//        primaryStage.setScene(new Scene(root, 1024, 768));
//        primaryStage.show();
//    }
//
//    public VisualizationController getVisualizationController() {
//        return visualizationController;
//    }
//
//    public String getVisualizationTest() {
//        return "test erfolgreich";
//    }
//
//    public void setValue(String value) {
//        visualizationController.setValue(value);
//    }
//
//    public static Visualization getInstance() {
//        if(myInstance == null)
//            myInstance = new Visualization();
//
//        return myInstance;
//    }
    private static final String FILE_URI = "visualization.fxml";
    private VisualizationController rootController;
    private Parent rootLayout;
    private Stage primaryStage;
    private FXMLLoader rootLoader;

    public Visualization() {
    }

    public Visualization(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;
        initRootLayout();
    }

    public void initRootLayout() throws Exception{

        //rootLayout = FXMLLoader.load(getClass().getClassLoader().getResource(FILE_URI))
        rootLoader = new FXMLLoader();
        rootLoader.setLocation(Visualization.class.getResource(FILE_URI));

        rootLayout = rootLoader.load();

        initRootController();

        primaryStage.setTitle("This is my title");
        primaryStage.setScene(new Scene(rootLayout, 820, 768));
        primaryStage.show();
    }

    public void initRootController()
    {
        rootController = rootLoader.getController();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public VisualizationController getVisualizationController() {
        return rootController;
    }
}