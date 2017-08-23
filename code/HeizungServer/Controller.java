package HeizungServer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.Optional;


public class Controller {


    @FXML
    private Label lbl_Serverip;
    @FXML
    private Label lbl_Servername;
    @FXML
    private Label lbl_Serverport;
    @FXML
    private Label lbl_Serverstatus;
    @FXML
    private Label lbl_srvmsg;
    @FXML
    private Button btn_starteServer;
    @FXML
    private Button btn_stoppeServer;
    @FXML
    private TextArea ta_srvlog;


    @FXML
    private Label lbl_temp;
    @FXML
    private Label lbl_desiredtemp;

    public static PrintStream ps;



    public void BTNServerStarten(ActionEvent event) throws IOException {

/*Platform runLater => Ausführung auf JavaFX Thread, da sonst Exception*/
        ps = new PrintStream(new OutputStream() {

            @Override
            public void write(int i) throws IOException {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ta_srvlog.appendText(String.valueOf((char) i));
                    }
                });


            }
        });
        System.setOut(ps);

        /*Server wird gestartet*/

        lbl_srvmsg.setText("test");


    }

    public void BTNServerStoppen(ActionEvent event) {

        if (lbl_Serverstatus.getText() == "Gestoppt") {
            btn_stoppeServer.setDisable(true);
            btn_starteServer.setDisable(false);
        }
    }


    public void BTNSetDesTemp(ActionEvent event) throws RemoteException{
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Gewünschte Temperatur einstellen");
        dialog.setHeaderText("Gewünschte Temperatur der Heizung einstellen");
        dialog.setContentText("Bitte gewünschte Temperatur der Heizung einstellen:");
        Optional<String> result = dialog.showAndWait();

        if(result.isPresent() == true && !result.get().equals("")) {
            Double newTemp = Double.parseDouble(result.get());
            System.out.println(newTemp);

        }
        else{
            return;
        }
    }
}