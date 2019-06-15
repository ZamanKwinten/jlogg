package jlogg;

import java.io.File;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.stage.Stage;
import jlogg.ui.MainStage;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// initialize the constant manager
		ConstantMgr.instance();
		stage.hide();
		MainStage mainStage = new MainStage();
		mainStage.show();

		mainStage.getMainPane()
				.addTabs(getParameters().getRaw().stream().map(path -> new File(path)).collect(Collectors.toList()));
	}

}