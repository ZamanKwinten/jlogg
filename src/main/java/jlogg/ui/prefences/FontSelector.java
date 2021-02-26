package jlogg.ui.prefences;

import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import jlogg.ui.GlobalConstants;
import jlogg.ui.popup.PreferencesPopup;
import jlogg.ui.utils.FXUtils;

public class FontSelector extends GridPane {

	private final PreferencesPopup parent;

	private final ComboBox<String> font;
	private final ComboBox<Number> size;
	private final TextField preview;

	public FontSelector(PreferencesPopup parent) {
		this.parent = parent;
		font = new ComboBox<>(FXCollections.observableArrayList(Font.getFamilies()));
		size = new ComboBox<>(FXCollections
				.observableArrayList(Arrays.asList(8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72)));
		preview = new TextField("aaBBcc");

		size.setConverter(new StringConverter<Number>() {

			@Override
			public String toString(Number v) {
				return v == null ? "" : "" + v.doubleValue();
			}

			@Override
			public Number fromString(String string) {
				return Double.parseDouble(string);
			}
		});

		size.setPrefWidth(75);
		size.setEditable(true);

		setFont(GlobalConstants.defaultFont.getValue());

		font.valueProperty().addListener((obs, oldV, newV) -> {
			updatePreview();

		});
		size.valueProperty().addListener((obs, oldV, newv) -> {
			updatePreview();
		});

		add(new Label("Font"), 0, 0, 2, 1);

		add(font, 0, 1);
		add(size, 1, 1);

		preview.setEditable(false);
		preview.setDisable(true);
		add(preview, 0, 2, 2, 1);
		setPadding(new Insets(15));
	}

	public void setFont(Font font) {
		this.font.setValue(font.getFamily());
		size.setValue(font.getSize());
		preview.setFont(font);
	}

	private void updatePreview() {
		preview.setFont(getFont());
		preview.setPrefWidth(FXUtils.calculateTextControlWidth(preview.getFont(), preview.getText(), 15));
		parent.sizeToScene();
	}

	public Font getFont() {
		return Font.font(font.getValue(), size.getValue().doubleValue());
	}
}
