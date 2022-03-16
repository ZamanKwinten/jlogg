package jlogg.ui.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
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
							new FitContentColumn<>(GlobalConstants.defaultFont, definition.contentProducer()),
							JLoggTableCellFactories.cellWithRightBorder());
				}

				ColumnDefinition<T> definition = definitions.get(definitions.size() - 1);
				handleColumnDefinition(definition,
						new FillRemainingColumn<>(this, GlobalConstants.defaultFont, definition.contentProducer()),
						JLoggTableCellFactories.cell());
			}
		});

		addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.A && e.isShortcutDown()) {
				e.consume();
			}
		});
	}

	private void handleColumnDefinition(ColumnDefinition<T> definition, TableColumn<T, String> column,
			Callback<TableColumn<T, String>, TableCell<T, String>> defaultcellFactory) {
		column.setCellFactory(definition.getCellFactory().orElse(defaultcellFactory));
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

	public List<ColumnDefinition<T>> getColumnDefinitions() {
		return definitions;
	}

	public void show() {
		getColumns().forEach(c -> c.setVisible(true));
	}

	public void hide() {
		getColumns().forEach(c -> c.setVisible(false));
	}
}
