package jlogg.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import jlogg.plugin.LogLine;
import jlogg.ui.utils.Observable;

public class SearchHighlightBar extends Pane {

	enum HighlightMode {
		Single, Multi, None;
	}

	private class HighlightLine extends Rectangle {
		private LogLine firstHitLine;

		public HighlightLine(int yCoord) {
			super(15, 1);
			setY(yCoord);

			setOnMouseClicked((event) -> {
				if (firstHitLine != null) {
					filetab.selectLogLine(firstHitLine.getLineNumber());
				}
			});
		}

		public void setHit(LogLine firstHitLine) {
			if (firstHitLine != null) {
				getStyleClass().setAll("searchhightlightbar-hit");
				if (this.firstHitLine == null) {
					this.firstHitLine = firstHitLine;
				}
			} else {
				getStyleClass().setAll("searchhightlightbar-missing");
				this.firstHitLine = null;
			}
		}

	}

	private Observable<HighlightMode> mode;
	private final FileTab filetab;
	private final ObservableList<LogLine> lines;
	private List<HighlightLine> children;

	public SearchHighlightBar(FileTab filetab, ObservableList<LogLine> lines) {
		this.mode = new Observable<>(HighlightMode.None);
		this.filetab = filetab;
		this.lines = lines;
		this.children = new ArrayList<>();

		setMinWidth(15);

		setVisible(false);

		managedProperty().bind(visibleProperty());

		visibleProperty().addListener((obs, o, n) -> {
			children.clear();
			if (!n) {
				getChildren().clear();
			} else {
				fillBar();
				repaint();
			}
		});

		heightProperty().addListener((obs, o, n) -> {
			if (o == n) {
				return;
			}
			children.clear();
			fillBar();
			repaint();
		});

		lines.addListener((InvalidationListener) (e) -> {
			repaint();
		});

		GlobalConstants.singleFileSearchResults
				.computeIfAbsent(filetab.getFile(), (key) -> FXCollections.observableArrayList())
				.addListener((InvalidationListener) e -> {
					repaint();
				});

		GlobalConstants.multiFileSearchResults.addListener((InvalidationListener) e -> {
			repaint();
		});

		mode.addListener((obs, o, n) -> {
			repaint();
		});
	}

	private void fillBar() {
		for (int i = 0; i < (int) getHeight(); i++) {
			HighlightLine rect = new HighlightLine(i);
			children.add(rect);
		}
		getChildren().setAll(children);
	}

	@Override
	public void requestLayout() {
	}

	public void openSingleView() {
		mode.setValue(HighlightMode.Single);
		setVisible(true);
	}

	public void openMultiView() {
		mode.setValue(HighlightMode.Multi);
		setVisible(true);
	}

	public void hide() {
		mode.setValue(HighlightMode.None);
	}

	private void repaint() {
		Platform.runLater(() -> {
			if (children.size() > 0 && lines.size() > 0) {
				for (HighlightLine rect : children) {
					rect.setHit(null);
				}

				int amountPerPlace = lines.size() / children.size();
				if (amountPerPlace > 0) {
					switch (mode.getValue()) {
					case Single -> {
						for (LogLine line : GlobalConstants.singleFileSearchResults.get(filetab.getFile())) {
							children.get(line.getLineNumber() / amountPerPlace).setHit(line);
						}
					}
					case Multi -> {
						for (LogLine line : GlobalConstants.multiFileSearchResults) {
							if (Objects.equals(line.getFile(), filetab.getFile())) {
								children.get(line.getLineNumber() / amountPerPlace).setHit(line);
							}
						}
					}
					default -> {
						setVisible(false);
					}
					}
				} else {
					double placesPerLine = 1. * children.size() / lines.size();

					// TODO fix this mess
					switch (mode.getValue()) {
					case Single -> {
						for (LogLine line : GlobalConstants.singleFileSearchResults.get(filetab.getFile())) {
							for (int i = (int) Math.round(line.getLineNumber() * placesPerLine); i < Math
									.round(line.getLineNumber() * placesPerLine + placesPerLine); i++) {
								children.get(i).setHit(line);
							}
						}
					}
					case Multi -> {
						for (LogLine line : GlobalConstants.multiFileSearchResults) {
							if (Objects.equals(line.getFile(), filetab.getFile())) {
								for (int i = (int) Math.round(line.getLineNumber() * placesPerLine); i < Math
										.round(line.getLineNumber() * placesPerLine + placesPerLine); i++) {
									children.get(i).setHit(line);
								}
							}
						}
					}
					default -> {
						setVisible(false);
					}
					}
				}
			}
		});
	}

}
