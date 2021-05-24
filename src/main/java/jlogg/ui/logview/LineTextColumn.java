package jlogg.ui.logview;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import jlogg.datahandlers.FileLineReader;
import jlogg.plugin.LogLine;
import jlogg.ui.table.ColumnDefinition;
import jlogg.ui.table.JLoggLogFileView;

public class LineTextColumn extends ColumnDefinition<LogLine> {
	private final JLoggLogFileView parent;
	private final Consumer<LogLine> clickHandler;

	public LineTextColumn(JLoggLogFileView parent, Consumer<LogLine> clickHandler) {
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
