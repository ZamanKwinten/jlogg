package jlogg.type;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.stage.FileChooser;
import jlogg.datahandlers.PluginFileIterator;
import jlogg.plugin.JLogg.FileWriterCallback;
import jlogg.plugin.PluginAction;
import jlogg.ui.FileTab;
import jlogg.ui.MainPane;
import jlogg.ui.MainStage;
import jlogg.ui.popup.SearchPopup;

public class JLogg {
	private static final Logger logger = Logger.getLogger(JLogg.class.getName());

	private PluginFileIterator iterator;

	public void doItOnCurrentFile(PluginAction action) {
		if (iterator != null) {
			iterator.stop();
		}

		iterator = new PluginFileIterator(MainStage.getInstance().getMainPane().getCurrentSelectedTab().getFile(),
				action);
		iterator.doIt();
	}

	public void doItOnSelectedFiles(PluginAction action, List<File> files) {
		if (iterator != null) {
			iterator.stop();
		}

		iterator = new PluginFileIterator(files, action);
		iterator.doIt();
	}

	public void doItOnAllFiles(PluginAction action) {

		var files = getCurrentOpenFiles();
		if (files.size() == 1) {
			doItOnCurrentFile(action);
		} else if (files.size() > 1) {
			SearchPopup popup = new SearchPopup(files);
			Optional<List<File>> result = popup.open();

			if (result.isPresent()) {
				if (iterator != null) {
					iterator.stop();
				}

				iterator = new PluginFileIterator(result.get(), action);
				iterator.doIt();
			}
		}
	}

	public List<File> getCurrentOpenFiles() {
		MainPane mainPane = MainStage.getInstance().getMainPane();
		return mainPane.getFileTabs().stream().map(FileTab::getFile).toList();
	}

	public Optional<File> saveToFile(String fileName, FileWriterCallback callback) {
		FileChooser fc = new FileChooser();

		FileTab currentTab = MainStage.getInstance().getMainPane().getCurrentSelectedTab();

		fc.setInitialDirectory(currentTab.getFile().getParentFile());
		fc.setInitialFileName(fileName);
		File file = fc.showSaveDialog(null);
		if (file != null) {
			try {
				file.delete();
				file.createNewFile();
				try (FileWriter fw = new FileWriter(file)) {
					callback.callback(fw);
				}

				return Optional.of(file);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error during saving", e);
			}
		}
		return Optional.empty();
	}
}
