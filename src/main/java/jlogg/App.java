package jlogg;

import java.awt.Desktop;
import java.awt.desktop.OpenFilesHandler;

import javafx.application.Application;
import javafx.stage.Stage;
import jlogg.datahandlers.ThreadGroups;
import jlogg.os.FileOpenHandler;
import jlogg.os.mac.MacFileOpenHandler;
import jlogg.os.windows.WindowsFileOpenHandler;
import jlogg.ui.MainStage;
import jlogg.version.VersionUtil;

public class App extends Application {
	private FileOpenHandler handler;

	@Override
	public void start(Stage stage) throws Exception {
		// initialize the constant manager
		ConstantMgr constantMGR = ConstantMgr.instance();
		constantMGR.setupGlobalConstants();
		constantMGR.loadPlugins();
		var updateCheckingThread = new Thread(VersionUtil::checkForUpdates, "check-for-updates-thread");
		updateCheckingThread.setDaemon(true);
		updateCheckingThread.start();

		stage.hide();
		MainStage mainStage = MainStage.getInstance();
		if (Launcher.isMac) {
			handler = new MacFileOpenHandler(mainStage);
			Desktop.getDesktop().setOpenFileHandler((OpenFilesHandler) handler);
		} else {
			handler = new WindowsFileOpenHandler(mainStage);
		}
		mainStage.show();
		mainStage.getMainPane().openTabs(FileOpenHandler.initialFiles);
	}

	@Override
	public void stop() throws Exception {
		ThreadGroups.forceStop();
		handler.release();
	}

}