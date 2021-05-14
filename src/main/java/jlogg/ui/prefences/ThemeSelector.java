package jlogg.ui.prefences;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import jlogg.plugin.Theme;
import jlogg.ui.GlobalConstants;

public class ThemeSelector extends GridPane {

	private final ComboBox<Theme> combo;

	public ThemeSelector() {
		add(new Label("Theme"), 0, 0);
		setHgap(15);

		combo = new ComboBox<>(FXCollections.observableArrayList(Theme.values()));
		combo.setConverter(new StringConverter<Theme>() {

			@Override
			public String toString(Theme object) {
				return object.getUIName();
			}

			@Override
			public Theme fromString(String string) {
				return null;
			}
		});

		combo.setValue(GlobalConstants.theme.getValue());

		add(combo, 1, 0);
	}

	public Theme getTheme() {
		return combo.getValue();
	}
}
