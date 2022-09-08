import acsse.csc2b.handler.Client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Client root = new Client();
		Scene scene = new Scene(root,680,520,Color.BLACK);
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
}
