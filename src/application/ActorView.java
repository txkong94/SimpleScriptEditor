package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;



import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class ActorView implements Initializable{
	
	private static Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	
	public static void start(Stage stage, Window owner, ArrayList<TranslateableActor> actorList) throws IOException{
		ActorView.stage = stage;

		AnchorPane editorView = (AnchorPane)FXMLLoader.load(ActorView.class.getResource("ActorView.fxml"));
		Scene scene = new Scene(editorView);
		stage.setResizable(false);
		stage.initStyle(StageStyle.UTILITY);
		if(owner != null) {
			stage.initOwner(owner);
			stage.initModality(Modality.WINDOW_MODAL);
		}
		stage.setScene(scene);
		stage.show();
	}
		

}
