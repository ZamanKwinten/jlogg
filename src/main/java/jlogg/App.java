package jlogg;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.stage.Stage;
import jlogg.rmi.RMIHandler;
import jlogg.ui.MainStage;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private static RMIHandler rmiHandler;

	@Override
	public void start(@SuppressWarnings("exports") Stage stage) throws Exception {
		List<File> filesToOpen = getFilesFromParameters();

		// initialize the constant manager
		ConstantMgr.instance();
		stage.hide();
		MainStage mainStage = new MainStage();
		rmiHandler = new RMIHandler(mainStage, filesToOpen);

		mainStage.show();
	}

	@Override
	public void stop() throws Exception {
		rmiHandler.shutdown();
	}

	private List<File> getFilesFromParameters() {
		return getParameters().getRaw().stream().map(path -> new File(path)).collect(Collectors.toList());
	}

}