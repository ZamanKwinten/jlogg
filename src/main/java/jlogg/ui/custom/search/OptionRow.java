package jlogg.ui.custom.search;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import jlogg.plugin.LogLine;
import jlogg.shared.SearchOptions;
import jlogg.ui.GlobalConstants;

public class OptionRow extends HBox {

	private final ProgressBar progressBar;
	private CheckBox ignoreCaseCheck;

	public OptionRow(SearchBox searchBox) {
		super(5);
		progressBar = new ProgressBar(GlobalConstants.searchProgress);

		Label matchesLabel = new Label();
		searchBox.getSearchResultList().addListener(new ListChangeListener<LogLine>() {
			@Override
			public void onChanged(Change<? extends LogLine> c) {
				Platform.runLater(() -> {
					int size = c.getList().size();
					if (size == 0) {
						matchesLabel.setText("");
					} else if (size == 1) {
						matchesLabel.setText("Count: 1 Match");
					} else {
						matchesLabel.setText("Count: " + size + " Matches");
					}
				});
			}
		});

		ignoreCaseCheck = new CheckBox("Ignore Case");
		ignoreCaseCheck.setSelected(true);
		ignoreCaseCheck.setMinWidth(85);

		setHgrow(progressBar, Priority.ALWAYS);
		setPadding(new Insets(5, 5, 5, 0));
		setAlignment(Pos.CENTER_LEFT);

		getChildren().addAll(searchBox.getCurrentFileSearchFilename(), progressBar, matchesLabel, ignoreCaseCheck);
	}

	public void setSearchOptions(SearchOptions opt) {
		ignoreCaseCheck.setSelected(opt.ignoreCase());
	}

	public SearchOptions getSearchOptions() {
		return new SearchOptions(ignoreCaseCheck.isSelected());
	}
}
