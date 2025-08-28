package jlogg;

import java.awt.Desktop;
import java.awt.desktop.OpenFilesHandler;

import javafx.application.Application;
import javafx.stage.Stage;
import jlogg.build.BuildDetailsUtil;
import jlogg.datahandlers.ThreadGroups;
import jlogg.os.FileOpenHandler;
import jlogg.os.mac.MacFileOpenHandler;
import jlogg.os.windows.WindowsFileOpenHandler;
import jlogg.ui.MainStage;

public class App extends Application {
	private FileOpenHandler handler;

	@Override
	public void start(Stage stage) throws Exception {
		// initialize the constant manager
		ConstantMgr constantMGR = ConstantMgr.instance();
		constantMGR.setupGlobalConstants();
		var pluginLoaderThread = new Thread(constantMGR::loadPlugins, "plugin-loader-thread");
		pluginLoaderThread.setDaemon(true);
		pluginLoaderThread.start();
		var updateCheckingThread = new Thread(BuildDetailsUtil::checkForUpdates, "check-for-updates-thread");
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