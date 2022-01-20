package jlogg.type;

import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import jlogg.datahandlers.PluginFileIterator;
import jlogg.plugin.JLogg.FileWriterCallback;
import jlogg.plugin.PluginAction;
import jlogg.ui.FileTab;
import jlogg.ui.MainPane;
import jlogg.ui.MainStage;

public class JLoggHeadless extends JLogg {
	private static final Logger logger = Logger.getLogger(JLoggHeadless.class.getName());

	@Override
	public void doItOnCurrentFile(PluginAction action) {
		doItOnFiles(Collections.singleton(MainStage.getInstance().getMainPane().getCurrentSelectedTab().getFile()),
				action);
	}

	@Override
	public void doItOnAllFiles(PluginAction action) {
		MainPane mainPane = MainStage.getInstance().getMainPane();
		var files = mainPane.getFileTabs().stream().map(FileTab::getFile).collect(Collectors.toList());

		doItOnFiles(files, action);
	}

	private void doItOnFiles(Collection<File> files, PluginAction action) {
		PluginFileIterator iterator = new PluginFileIterator(files, action);
		iterator.doIt();
		iterator.waitFor();
	}

	@Override
	public Optional<File> saveToFile(String fileName, FileWriterCallback callback) {

		File file = new File(fileName);
		logger.log(Level.INFO, "Writing results to: " + file.getAbsolutePath());
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

		return Optional.empty();

	}
}
