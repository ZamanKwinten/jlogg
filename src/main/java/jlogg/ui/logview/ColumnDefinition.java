package jlogg.ui.logview;

import java.util.Optional;
import java.util.function.Function;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public abstract class ColumnDefinition<T> {

	protected abstract Function<T, String> contentProducer();

	protected abstract Optional<Callback<TableColumn<T, String>, TableCell<T, String>>> getCellFactory();

	protected void applyExtra(TableColumn<T, String> column) {

	}
}
