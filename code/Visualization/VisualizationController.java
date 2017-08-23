package Visualization;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Created by Carlo on 04.08.2017.
 */
public class VisualizationController {

//    Visualization visualization;
//
//    public VisualizationController(Visualization visualization){
//        this.visualization = visualization;
//    }

//    public VisualizationController() {
//        this.virtualGameboardTB = new TextField();
//    }

    private VisualizationController visualizationController;

    @FXML
    Button btn_test;

    @FXML
    TextArea ta_virtualGameboard;

    @FXML
    TextField tf_virtualGameboard;

    @FXML
    TextField virtualGameboardTB;

    @FXML
    private TextField getVirtualGameboardTB;

    @FXML
    Label label;

    public VisualizationController() {

    }


//    public VisualizationController(){
//        setTestValue();
//    }

//    @FXML
//    protected void buttonPressed(){
//        String text = textArea.getText();
//        label.setText(text);
//        textArea.clear();
//    }


    public void setTestValue() {
        virtualGameboardTB.setText("TEST");
    }

    public void setValue(String value) {
//        this.virtualGameboardTB = new TextField();
////        this.virtualGameboardTB.setText(virtualGameboardTB.getText() + value);
//        this.virtualGameboardTB.setText("123");
        //this.ta_virtualGameboard = new TextArea();
        ta_virtualGameboard.setText(value);
    }
}