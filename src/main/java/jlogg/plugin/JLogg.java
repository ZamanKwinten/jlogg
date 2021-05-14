package jlogg.plugin;

import java.util.Objects;

import jlogg.datahandlers.FileLineReader;
import jlogg.datahandlers.PluginFileIterator;
import jlogg.ui.MainPane;
import jlogg.ui.MainStage;

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
