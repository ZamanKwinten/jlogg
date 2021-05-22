package jlogg.ui.table;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import jlogg.ui.GlobalConstants;

public class JLoggTableCellFactories {

	public static <T> Callback<TableColumn<T, String>, TableCell<T, String>> cellWithRightBorder() {
		return (param) -> {
			return defaultCell("cellWithRightBorder");
		};
	}

	public static <T> Callback<TableColumn<T, String>, TableCell<T, String>> cell() {
		return (param) -> {
			return defaultCell("cell");
		};
	}

	private static <T> TableCell<T, String> defaultCell(String styleClass) {
		TableCell<T, String> cell = new TableCell<>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
				} else {
					setText(item);
				}
			}
		};

		cell.getStyleClass().setAll(styleClass);
		cell.fontProperty().bind(GlobalConstants.defaultFont);

		return cell;
	}
}
