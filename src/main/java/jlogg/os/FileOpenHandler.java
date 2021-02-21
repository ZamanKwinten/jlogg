package jlogg.os;

import java.io.File;
import java.util.List;

import javafx.application.Platform;
import jlogg.ui.MainStage;

public abstract class FileOpenHandler {
	public static List<File> initialFiles;

	protected final MainStage stage;

	public FileOpenHandler(MainStage stage) {
		this.stage = stage;
	}

	protected void addTabs(List<File> files) {
		Platform.runLater(() -> {
			stage.getMainPane().addTabs(files);
		});
	}

	public abstract void release();
}
