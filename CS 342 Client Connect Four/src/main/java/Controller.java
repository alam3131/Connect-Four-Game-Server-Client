import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.util.Pair;

public class Controller implements Initializable {

    @FXML
    private TextField portTF, ipTF;

    @FXML
    private Button connect, playAgain, mainMenu, exit;

    @FXML
    private BorderPane startPane;

    @FXML
    private VBox vBox1, vBox2;

    @FXML
    private HBox hBox1;

    @FXML
    private ListView<String> numClients, score, gameInfo;

    @FXML
    private Label title1, title2, portLabel, ipLabel, playerTurnLabel, resultLabel;

    @FXML
    private GridPane board;

    private GameButton[][] gridPaneArray;

    Client clientConnection;

    PauseTransition pause = new PauseTransition(Duration.seconds(3));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    // Function to add buttons to grid
    public void addGrid() {
        EventHandler<ActionEvent> gameButtonHandler;

        gameButtonHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                GameButton button = (GameButton) e.getSource();

                // Checks that game is not over
                if(Client.info.gameResult.getKey() == 0) {
                    // Checks if move is valid or not
                    if (ConnectFourLogic.moveCheck(button.col, button.row, gridPaneArray)) {
                        button.clicked = true;

                        if (Client.clientNum == 1) {
                            button.setStyle("-fx-background-color: red");
                            button.color = "red";
                            Client.info.p1Plays = Integer.toString(button.col) + ", " + Integer.toString(button.row);
                            gameInfo.getItems().add("You Played " + Client.info.p1Plays);
                        } else {
                            button.setStyle("-fx-background-color: yellow");
                            button.color = "yellow";
                            Client.info.p2Plays = Integer.toString(button.col) + ", " + Integer.toString(button.row);
                            gameInfo.getItems().add("You Played " + Client.info.p2Plays);
                        }

                        // Evaluate the board for a win
                        Pair<Integer, Integer[][]> gameResult = ConnectFourLogic.evalBoard(button.color, gridPaneArray);
                        Client.info.gameResult = gameResult;

                        // Changes the button border color of winning pieces if win exist
                        if (gameResult.getKey() == 1 || gameResult.getKey() == 2) {
                            for (Integer[] piece : gameResult.getValue()) {
                                GameButton b = gridPaneArray[piece[1]][piece[0]]; // gridPaneArray[row][col]
                                b.setStyle(String.format("-fx-border-color: #00ffff; -fx-border-width: 5px; -fx-background-color: %s", b.color));
                            }
                        }

                        // Send CFourInfo back to server
                        Client.send(Client.info);

                    } else { // Move is invalid
                        title2.setText("You moved to " + button.col + "," + button.row + ". This is an invalid move. Pick again.");
                    }
                }
            }
        };

        // Adds GameButtons to board
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                GameButton temp = new GameButton(i, j);
                temp.setPrefSize(50, 50);
                temp.setOnAction(gameButtonHandler);
                board.add(temp, i, j);
            }
        }

        // Creates a gridPane array for access
        gridPaneArray = new GameButton[6][7];
        for (Node button : board.getChildren()) {
            gridPaneArray[GridPane.getRowIndex(button)][GridPane.getColumnIndex(button)] = (GameButton) button;
        }
    }

    // Updates the board given the moves played
    public void updateBoard() {
        try {
            String move;
            String buttonColor;
            String style;
            GameButton button;

            if (Client.clientNum == 1) {
                move = Client.info.p2Plays;
                buttonColor = "yellow";
                style = "-fx-background-color: yellow";
            } else {
                move = Client.info.p1Plays;
                buttonColor = "red";
                style = "-fx-background-color: red";
            }
            // Retrieves the columns and rows of the move that was played
            int col = Character.getNumericValue(move.charAt(0));
            int row = Character.getNumericValue(move.charAt(3));

            button = gridPaneArray[row][col];
            button.setStyle(style);
            button.color = buttonColor;
            button.clicked = true;
        } catch (Exception e) {}
    }

    public void disableBoard() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                gridPaneArray[i][j].setDisable(true);
            }
        }
    }

    public void enableBoard() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                gridPaneArray[i][j].setDisable(false);
            }
        }
    }

    public void toResultScene(String resultMessage) {
        pause.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/resultScene.fxml"));
                    Parent root2 = loader.load(); //load view into parent

                    Controller ctrl = loader.getController();
                    ctrl.resultLabel.setText(resultMessage);

                    System.out.println("result getScene: " + startPane.getScene());
                    root2.getStylesheets().add("/Styles/resultStyle.css");//set style
                    startPane.getScene().setRoot(root2);//update scene graph
                }
                catch(IOException ex) {}
            }
        });
        pause.play();
    }

    // Sets game scene and resets the grid
    public void resetGame() {
        pause.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/gameScene.fxml"));
                    Parent root2 = loader.load(); //load view into parent

                    Controller ctrl = loader.getController();
                    //ctrl.addGrid();

                    System.out.println(startPane.getScene());
                    root2.getStylesheets().add("/Styles/gameStyle.css");//set style
                    startPane.getScene().setRoot(root2);//update scene graph
                }
                catch(IOException ex) {}
            }
        });
        pause.play();
    }

    public void connectMethod(ActionEvent e) throws IOException {
        int portNum = Integer.parseInt(portTF.getText());
        String ipAddress = ipTF.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/gameScene.fxml"));
        Parent root2 = loader.load(); //load view into parent

        Controller ctrl = loader.getController();
        ctrl.addGrid();

        root2.getStylesheets().add("/Styles/gameStyle.css");//set style
        startPane.getScene().setRoot(root2);//update scene graph

        clientConnection = new Client(portNum, ipAddress,
                playerTurnString -> {
                    Platform.runLater(() -> {
                        ctrl.title2.setText(playerTurnString.toString());
                    });
                },

                gameInfoString -> {
                    Platform.runLater(() -> {
                        ctrl.gameInfo.getItems().add(gameInfoString.toString());
                    });
                },

                update -> {
                    Platform.runLater(ctrl::updateBoard);
                },

                disable -> {
                    Platform.runLater(ctrl::disableBoard);
                },

                enable -> {
                    Platform.runLater(ctrl::enableBoard);
                },

                endGame -> {
                    Platform.runLater(() -> {
                        ctrl.title2.setText("Game Over");

                        // Changes the button border color of winning pieces if win exist
                        Pair<Integer, Integer[][]> gameResult = Client.info.gameResult;
                        if(gameResult.getKey() == 1 || gameResult.getKey() == 2) {
                            for(Integer[] piece : gameResult.getValue()) {
                                GameButton b = ctrl.gridPaneArray[piece[1]][piece[0]]; // gridPaneArray[row][col]
                                b.setStyle(String.format("-fx-border-color: #00ffff; -fx-border-width: 5px; -fx-background-color: %s", b.color));
                            }
                        }

                        ctrl.toResultScene(endGame.toString());
                    });
                },

                resetGame -> {
                    Platform.runLater(ctrl::resetGame);
                }
        );

        clientConnection.start();
    }

    // Exits the application
    public void exitMethod(ActionEvent e) {
        Platform.exit();
    }

    // Sends to the server that client wants to play again
    public void playAgainMethod(ActionEvent e) {
        if(Client.clientNum == 1) {
            Client.info.p1PlayAgain = true;
        } else {
            Client.info.p2PlayAgain = true;
        }
        Client.send(Client.info);
    }
}
