
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.application.Platform;

public class Controller implements Initializable {

    @FXML
    private TextField portTF;

    @FXML
    private Button startServer;

    @FXML
    private BorderPane startPane;

    @FXML
    private VBox vBox1, vBox2;

    @FXML
    private HBox hBox1;

    @FXML
    private ListView<String> numClients, score, gameInfo;

    @FXML
    private Label title1, title2, portLabel;

    private static int portNum;

    Server serverConnection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void startServerMethod(ActionEvent e) throws IOException {
        portNum = Integer.parseInt(portTF.getText());
        // Checks port number is valid
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/runningServer.fxml"));
        Parent root2 = loader.load(); //load view into parent

        Controller ctrl = loader.getController();
        ctrl.gameInfo.getItems().add("Game Info");
        ctrl.numClients.getItems().add("Client Count");

        root2.getStylesheets().add("/Styles/styles2.css");//set style
        startPane.getScene().setRoot(root2);//update scene graph

        serverConnection = new Server(portNum,
            numClientString -> {Platform.runLater(()->{
                ctrl.numClients.getItems().clear();
                ctrl.numClients.getItems().add("Client Count");
                ctrl.numClients.getItems().add(numClientString.toString());
                });
            },

            gameInfoString -> {Platform.runLater(()->{
                ctrl.gameInfo.getItems().add(gameInfoString.toString());
                });
            }
        );
    };

}
