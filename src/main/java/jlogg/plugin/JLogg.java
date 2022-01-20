package jlogg.plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jlogg.datahandlers.FileLineReader;
import jlogg.ui.FileTab;
import jlogg.ui.MainPane;
import jlogg.ui.MainStage;

public class JLogg {

	public interface FileWriterCallback {
		public void callback(FileWriter fw) throws IOException;
	}

	public static jlogg.type.JLogg JLOGG = new jlogg.type.JLogg();

	public static void doItOnCurrentFile(PluginAction action) {
		JLOGG.doItOnCurrentFile(action);
	}

	public static void doItOnAllFiles(PluginAction action) {
		JLOGG.doItOnAllFiles(action);
	}

	public static String readLine(LogLine line) {
		return FileLineReader.readLineFromFile(line);
	}

	public static void selectLine(LogLine line) {
		selectLine(line, (filetab) -> {
		});
	}

	public static void selectLine(LogLine line, Consumer<PluginViewWrapper> callback) {
		MainPane pane = MainStage.getInstance().getMainPane();
		if (Objects.equals(pane.getCurrentSelectedTab().getFile(), line.getFile())) {
			FileTab fileTab = pane.getCurrentSelectedTab();
			fileTab.selectLogLine(line.getLineNumber());
			callback.accept(fileTab.getPluginViewWrapper());
		} else {
			pane.findFileTab(line.getFile()).ifPresent(filetab -> {
				pane.selectTab(filetab);
				filetab.selectLogLine(line.getLineNumber());
				callback.accept(filetab.getPluginViewWrapper());
			});
		}
	}

	public static Optional<File> saveLogLinesToFile(String fileName, LogLine[] lines) {
		if (lines.length > 0) {
			return saveToFile(fileName, fw -> {
				for (List<LogLine> linesBatch : getBatches(lines, 100)) {
					for (String textLines : FileLineReader.readLinesFromFile(linesBatch)) {
						fw.write(textLines);
						fw.write(System.lineSeparator());
					}
				}
			});
		} else {
			return Optional.empty();
		}
	}

	private static List<List<LogLine>> getBatches(LogLine[] loglines, int batchSize) {
		List<LogLine> lines = Arrays.asList(loglines);
		return IntStream.iterate(0, i -> i < lines.size(), i -> i + batchSize)
				.mapToObj(i -> lines.subList(i, Math.min(i + batchSize, lines.size()))).collect(Collectors.toList());

	}

	public static Optional<File> saveToFile(String fileName, FileWriterCallback callback) {
		return JLOGG.saveToFile(fileName, callback);
	}

	public static void closePluginView() {
		MainStage.getInstance().getMainPane().getCurrentSelectedTab().closePlugin();
	}
}
