package Visualization;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Created by Carlo on 04.08.2017.
 */
public class VisualizationController {


    @FXML
    TextField virtualGameboardTB;

    @FXML
    Label label;

//    @FXML
//    protected void buttonPressed(){
//        String text = textArea.getText();
//        label.setText(text);
//        textArea.clear();
//    }

    @FXML
    public void setValue(String value){
        virtualGameboardTB.setText(virtualGameboardTB.getText()+value);
    }
}