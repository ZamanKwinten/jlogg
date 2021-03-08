package jlogg.ui.utils;

import javafx.application.Platform;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Utility class containing some methods to do fx calculations
 * 
 * @author KWZA
 *
 */
public class FXUtils {

	public static double caculateTextControlHeight(Font font) {
		Text tmp = new Text("t");
		tmp.setFont(font);
		return tmp.getLayoutBounds().getHeight();
	}

	public static double calculateTextControlWidth(Font font, String text, double padding) {
		Text tmp = new Text(text);
		tmp.setFont(font);
		return tmp.getLayoutBounds().getWidth() + padding;
	}

	/**
	 * A hack which makes it possible to freeze a column in place in a tableview
	 * https://www.linkedin.com/pulse/love-javafx-missing-frozen-column-table-grid-worries-here-biswa-das/
	 * 
	 * @param cell
	 */
	public static void columnFreezeHack(TableCell<?, ?> cell) {
		Platform.runLater(() -> {
			ScrollBar sc = (ScrollBar) cell.getTableView()
					.queryAccessibleAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR);
			sc.valueProperty().addListener((obs, o, n) -> {
				cell.setTranslateX(n.doubleValue());
				cell.toFront();
			});
		});
	}
}
