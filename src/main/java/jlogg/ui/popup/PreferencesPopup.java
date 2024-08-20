package jlogg.ui.popup;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import jlogg.Preferences;
import jlogg.ui.prefences.FontSelector;
import jlogg.ui.prefences.PreferencesTab;
import jlogg.ui.prefences.ShortcutSelector;
import jlogg.ui.prefences.ThemeSelector;

public class PreferencesPopup extends PopupWithReturn<Preferences> {

	private final PreferencesTab<FontSelector> fontSetup;
	private final PreferencesTab<ShortcutSelector> shortcutSetup;
	private final PreferencesTab<ThemeSelector> themeSetup;

	public PreferencesPopup() {
		super("Preferences");

		themeSetup = new PreferencesTab<>("Theme", new ThemeSelector());
		fontSetup = new PreferencesTab<>("Font", new FontSelector(this));
		shortcutSetup = new PreferencesTab<>("Shortcuts", new ShortcutSelector());

		TreeItem<String> root = new TreeItem<>("Editor");
		root.setExpanded(true);

		root.getChildren().add(themeSetup);
		root.getChildren().add(fontSetup);
		root.getChildren().add(shortcutSetup);

		TreeView<String> tree = new TreeView<>(root);

		StackPane contentPane = new StackPane();

		tree.setMaxWidth(100);
		tree.setPrefHeight(100);

		GridPane grid = new GridPane();
		grid.add(tree, 0, 0);
		grid.add(contentPane, 1, 0);

		contentPane.setPadding(new Insets(0, 0, 15, 15));

		tree.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
			if (newV instanceof PreferencesTab) {
				contentPane.getChildren().clear();
				contentPane.getChildren().add(((PreferencesTab<Node>) newV).node());
				sizeToScene();
			}
		});

		tree.getSelectionModel().select(themeSetup);

		content.getChildren().addAll(grid, getCancelableFooterBox("Save", null));
		setResizable(false);
	}

	@Override
	protected Preferences getReturnValue() {
		return new Preferences(fontSetup.node().getFont(), shortcutSetup.node().getKeyMap(),
				themeSetup.node().getTheme());
	}
}
