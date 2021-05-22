package jlogg.ui.logview;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import jlogg.ui.GlobalConstants;

public class AutoFillingTable<T> extends TableView<T> {

	private final Map<ColumnDefinition<T>, TableColumn<T, String>> columnMap = new HashMap<>();
	private final ObservableList<ColumnDefinition<T>> definitions = FXCollections.observableArrayList();

	public AutoFillingTable() {
		definitions.addListener((InvalidationListener) e -> {
			getColumns().clear();
			columnMap.clear();
			if (definitions.size() > 0) {
				for (int i = 0; i < definitions.size() - 1; i++) {
					ColumnDefinition<T> definition = definitions.get(i);

					handleColumnDefinition(definition,
							new FitContentColumn<>(GlobalConstants.defaultFont, definition.contentProducer()));
				}

				ColumnDefinition<T> definition = definitions.get(definitions.size() - 1);
				handleColumnDefinition(definition,
						new FillRemainingColumn<>(this, GlobalConstants.defaultFont, definition.contentProducer()));
			}
		});

		addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.A && e.isShortcutDown()) {
				e.consume();
			}
		});

		// new KeyMapping(, e -> selectAll()),
	}

	private void handleColumnDefinition(ColumnDefinition<T> definition, TableColumn<T, String> column) {
		definition.getCellFactory().ifPresent(column::setCellFactory);
		definition.applyExtra(column);

		getColumns().add(column);
		columnMap.put(definition, column);
	}

	public void addColumn(ColumnDefinition<T> definition) {
		definitions.add(definition);
	}

	public TableColumn<T, String> getColumn(ColumnDefinition<T> definition) {
		return columnMap.get(definition);
	}
}
