package jlogg.ui.logview;

import java.util.function.Function;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Font;
import jlogg.ui.utils.FXUtils;
import jlogg.ui.utils.Observable;

/**
 * A Column that automatically resizes itself to fit the content
 * 
 * @author Kwinten
 *
 * @param <T>
 */
public class FitContentColumn<T> extends TableColumn<T, String> {
	protected final SimpleDoubleProperty maxContentSize = new SimpleDoubleProperty();

	public FitContentColumn(Observable<Font> font, Function<T, String> contentProducer) {
		setCellValueFactory((cell) -> {
			String content = contentProducer.apply(cell.getValue());
			// 15 px of padding
			double width = FXUtils.calculateTextControlWidth(font.getValue(), content, 15);
			if (width > maxContentSize.doubleValue()) {
				maxContentSize.setValue(width);
			}

			return new SimpleStringProperty(content);
		});

		font.addListener((obs, o, n) -> {
			maxContentSize.set(maxContentSize.get() * n.getSize() / o.getSize());
		});

		minWidthProperty().bind(maxContentSize);
		maxWidthProperty().bind(maxContentSize);
		prefWidthProperty().bind(maxContentSize);
	}
}
