package jlogg.ui.logview;

import java.util.Optional;
import java.util.function.Function;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import jlogg.plugin.LogLine;
import jlogg.ui.interfaces.DragSelectableContent;
import jlogg.ui.table.ColumnDefinition;

public class LineNumberColumn extends ColumnDefinition<LogLine> {
	private final DragSelectableContent parent;

	public LineNumberColumn(DragSelectableContent parent) {
		this.parent = parent;

	}

	@Override
	protected Function<LogLine, String> contentProducer() {
		return (l) -> " " + l.getLineNumber();
	}

	@Override
	protected Optional<Callback<TableColumn<LogLine, String>, TableCell<LogLine, String>>> getCellFactory() {
		return Optional.of((param) -> new LineNumberDragCell(parent));
	}

	@Override
	protected void applyExtra(TableColumn<LogLine, String> column) {
		column.setSortable(false);
	}
}
