package jlogg.ui.table;

import java.util.Optional;
import java.util.function.Function;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public abstract class ColumnDefinition<T> {

	protected abstract Function<T, String> contentProducer();

	protected Optional<Callback<TableColumn<T, String>, TableCell<T, String>>> getCellFactory() {
		return Optional.empty();
	}

	protected void applyExtra(TableColumn<T, String> column) {

	}
}
