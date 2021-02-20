package jlogg;

import java.io.File;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.stage.Stage;
import jlogg.os.FileOpenHandler;
import jlogg.os.windows.RMIHandler;
import jlogg.ui.MainStage;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private FileOpenHandler handler;

	@Override
	public void start(@SuppressWarnings("exports") Stage stage) throws Exception {
		// initialize the constant manager
		ConstantMgr.instance();
		stage.hide();
		MainStage mainStage = new MainStage();

		String os = System.getProperty("os.name");
		if (os.startsWith("Windows")) {
			handler = new RMIHandler(mainStage);
			handler.open(getParameters().getRaw().stream().map(path -> new File(path)).collect(Collectors.toList()));
		}

		mainStage.show();

	}

	@Override
	public void stop() throws Exception {
		handler.release();
	}

}