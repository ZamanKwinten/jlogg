package jlogg.plugin;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import jlogg.datahandlers.FileLineReader;
import jlogg.datahandlers.PluginFileIterator;
import jlogg.ui.FileTab;
import jlogg.ui.MainPane;
import jlogg.ui.MainStage;
import jlogg.ui.popup.SearchPopup;

public class JLogg {
	private static PluginFileIterator iterator;

	public static void doItOnCurrentFile(PluginAction action) {
		if (iterator != null) {
			iterator.stop();
		}

		iterator = new PluginFileIterator(MainStage.getInstance().getMainPane().getCurrentSelectedTab().getFile(),
				action);
		iterator.doIt();
	}

	public static void doItOnAllFiles(PluginAction action) {

		MainPane mainPane = MainStage.getInstance().getMainPane();
		List<FileTab> fileTabs = mainPane.getFileTabs();
		if (fileTabs.size() == 1) {
			doItOnCurrentFile(action);
		} else if (fileTabs.size() > 1) {
			SearchPopup popup = new SearchPopup(
					fileTabs.stream().map(fileTab -> fileTab.getFile()).collect(Collectors.toList()));
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

	public static String readLine(LogLine line) {
		return FileLineReader.readLineFromFile(line);
	}

	public static void selectLine(LogLine line) {
		MainPane pane = MainStage.getInstance().getMainPane();
		if (Objects.equals(pane.getCurrentSelectedTab().getFile(), line.getFile())) {
			pane.getCurrentSelectedTab().selectLogLine(line.getLineNumber());
		} else {
			pane.findFileTab(line.getFile()).ifPresent(filetab -> {
				pane.selectTab(filetab);

				filetab.selectLogLine(line.getLineNumber());
			});
		}
	}
}
