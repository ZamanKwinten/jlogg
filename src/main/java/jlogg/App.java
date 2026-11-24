package jlogg;

import java.awt.Desktop;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.desktop.OpenFilesHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
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
    var updateCheckingThread =
        new Thread(BuildDetailsUtil::checkForUpdates, "check-for-updates-thread");
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
    setupStageLocation(mainStage);
    mainStage.show();
    mainStage.getMainPane().openTabs(FileOpenHandler.initialFiles);

    mainStage.setOnCloseRequest(
        event -> {
          Platform.exit();
        });
  }

  void setupStageLocation(Stage stage) {
    Point pointer = MouseInfo.getPointerInfo().getLocation();
    ObservableList<Screen> screens = Screen.getScreens();
    Screen selected = Screen.getPrimary();
    Point2D p2d = new Point2D(pointer.getX(), pointer.getY());
    for (var screen : screens) {
      if (screen.getBounds().contains(p2d)) {
        selected = screen;
        break;
      }
    }
    Rectangle2D bounds = selected.getBounds();
    stage.setX(bounds.getMinX());
    stage.setY(bounds.getMinY());
    stage.setWidth(bounds.getWidth());
    stage.setHeight(bounds.getHeight());
  }

  @Override
  public void stop() throws Exception {
    ThreadGroups.forceStop();
    handler.release();
  }
}
