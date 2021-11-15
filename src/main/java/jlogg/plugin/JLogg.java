package jlogg.plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.stage.FileChooser;
import jlogg.datahandlers.FileLineReader;
import jlogg.datahandlers.PluginFileIterator;
import jlogg.ui.FileTab;
import jlogg.ui.MainPane;
import jlogg.ui.MainStage;
import jlogg.ui.popup.SearchPopup;

public class JLogg {
	private static final Logger logger = Logger.getLogger(JLogg.class.getName());

	public interface FileWriterCallback {
		public void callback(FileWriter fw) throws IOException;
	}

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

	public static void closePluginView() {
		MainStage.getInstance().getMainPane().getCurrentSelectedTab().closePlugin();
	}
}
