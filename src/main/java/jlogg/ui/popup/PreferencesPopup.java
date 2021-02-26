package jlogg.ui.popup;

import jlogg.Preferences;
import jlogg.ui.prefences.FontSelector;

public class PreferencesPopup extends PopupWithReturn<Preferences> {

	private final FontSelector fontSetup;

	public PreferencesPopup() {
		setTitle("Preferences");

		fontSetup = new FontSelector(this);

		content.getChildren().addAll(fontSetup, getCancelableFooterBox("Save", null));
		setResizable(false);
	}

	@Override
	protected Preferences getReturnValue() {
		return new Preferences(fontSetup.getFont());
	}
}
