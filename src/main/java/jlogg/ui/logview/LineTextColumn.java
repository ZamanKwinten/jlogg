package jlogg.ui.logview;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import jlogg.datahandlers.FileLineReader;
import jlogg.plugin.LogLine;

class LineTextColumn extends ColumnDefinition<LogLine> {
	private final LogFileView parent;
	private final BiConsumer<LogFileView, Integer> clickHandler;

	public LineTextColumn(LogFileView parent, BiConsumer<LogFileView, Integer> clickHandler) {
		this.parent = parent;
		this.clickHandler = clickHandler;
	}

	@Override
	protected Function<LogLine, String> contentProducer() {
		return FileLineReader::readLineFromFile;
	}

	@Override
	protected Optional<Callback<TableColumn<LogLine, String>, TableCell<LogLine, String>>> getCellFactory() {
		return Optional.of((param) -> {
			return new LineTextDragCell(parent, clickHandler);
		});
	}

	@Override
	protected void applyExtra(TableColumn<LogLine, String> column) {
		column.setSortable(false);
	}
}
