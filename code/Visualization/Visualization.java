package Visualization;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Carlo on 04.08.2017.
 */

public class Visualization extends Application {

    VisualizationController visualizationController;

    public static void main(String[] args){
        launch(args);
    }

    public void startGUI(String[] args){
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("visualization.fxml"));
        primaryStage.setTitle("Visualization");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }

    public Visualization(){
        this.visualizationController = new VisualizationController();
    }

    public VisualizationController getVisualizationController() {
        return visualizationController;
    }

    public String getVisualizationTest() {
        return "test erfolgreich";
    }
}