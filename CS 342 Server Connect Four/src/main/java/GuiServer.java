import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;

public class GuiServer extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			// Read file fxml and draw interface.
			URL url = getClass().getResource("/FXML/startScene.fxml");
			if (url == null) {
				throw new IllegalStateException("FXML not found in classpath");
			}
			Parent root = FXMLLoader.load(url);

			primaryStage.setTitle("Connect Four Server");
			Scene s1 = new Scene(root, 600,600);
			s1.getStylesheets().add("/Styles/styles1.css");
			primaryStage.setScene(s1);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

	}

	public static void main(String[] args) {
		launch(args);
	}
}
