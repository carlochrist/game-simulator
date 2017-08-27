package Visualization;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Carlo on 04.08.2017.
 */
public class VisualizationController implements Initializable {

    URL location;
    ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.location = location;
        this.resources = resources;
    }

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
        System.out.println("first");
    }

    @FXML
    public void initialize() {
        System.out.println("second");
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
        ta_virtualGameboard.setText("TEST");
    }

    public void setValue(String value) {
//        this.virtualGameboardTB = new TextField();
////        this.virtualGameboardTB.setText(virtualGameboardTB.getText() + value);
//        this.virtualGameboardTB.setText("123");
        //this.ta_virtualGameboard = new TextArea();
        tf_virtualGameboard.setText(value);
    }

}