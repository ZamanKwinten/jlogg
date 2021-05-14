package jlogg.plugin;

import jlogg.datahandlers.FileLineReader;
import jlogg.datahandlers.PluginFileIterator;
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
}
