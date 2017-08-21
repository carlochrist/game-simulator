package Visualization;

import javafx.fxml.FXML;
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

    @FXML
    TextField virtualGameboardTB;


    @FXML
    Label label;


//    public VisualizationController(){
//        setTestValue();
//    }

//    @FXML
//    protected void buttonPressed(){
//        String text = textArea.getText();
//        label.setText(text);
//        textArea.clear();
//    }

    @FXML
    public void setTestValue(){
        virtualGameboardTB.setText("TEST");
    }

    @FXML
    public void setValue(String value){
        virtualGameboardTB.setText(virtualGameboardTB.getText()+value);
    }
}