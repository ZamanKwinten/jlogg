package jlogg;

import javafx.application.Application;
import javafx.stage.Stage;
import jlogg.ui.MainStage;

public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {

		stage.hide();
		MainStage mainStage = new MainStage();
		mainStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}