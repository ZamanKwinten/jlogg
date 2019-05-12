package jlogg.ui.logview;

import java.util.Optional;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import jlogg.shared.LogLine;
import jlogg.ui.FileTab;
import jlogg.ui.interfaces.DragSelectableContent;

/**
 * The actual main view in which all the text will be displayed
 * 
 * @author KWZA
 *
 */
public class LogFileView extends TableView<LogLine> implements DragSelectableContent {

	private final FileTab mainPane;

	private final BookMarkColumn bookmarkColumn;
	private final LineNumberColumn lineNumberColumn;
	private final LineTextColumn lineTextColumn;

	private int dragStart;

	private String internalSelection;

	private final SimpleDoubleProperty maxTextColumnWidth = new SimpleDoubleProperty();
	private final SimpleDoubleProperty maxLineNumberColumnWidth = new SimpleDoubleProperty();

	public LogFileView(FileTab mainPane, ObservableList<LogLine> lines) {
		super(lines);
		this.mainPane = mainPane;

		bookmarkColumn = new BookMarkColumn();
		lineNumberColumn = new LineNumberColumn(maxLineNumberColumnWidth);

		lineTextColumn = new LineTextColumn(maxTextColumnWidth);

		lineNumberColumn.setCellFactory((param) -> {
			return new LineNumberDragCell(this);
		});
		lineTextColumn.setCellFactory((param) -> {
			return new LineTextDragCell(this);
		});

		getColumns().add(bookmarkColumn);
		getColumns().add(lineNumberColumn);
		getColumns().add(lineTextColumn);

		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		getSelectionModel().setCellSelectionEnabled(true);

		getStyleClass().add("logfileview");

		maxTextColumnWidth.set(Screen.getPrimary().getBounds().getWidth());
	}

	void updateTextColumnWidth(double value) {
		if (value > maxTextColumnWidth.doubleValue()) {
			maxTextColumnWidth.set(value);
		}
	}

	void updateLineNumberColumnWidth(double value) {
		if (value > maxLineNumberColumnWidth.doubleValue()) {
			maxLineNumberColumnWidth.set(value);
		}
	}

	public int getDragStart() {
		return dragStart;
	}

	public void setDragStart(int startIndex) {
		clearSelection();
		this.dragStart = startIndex;
	}

	private void selectLine(int index) {
		getSelectionModel().select(index, lineTextColumn);
		mainPane.setLastSelection(this);
	}

	public void clearSelection() {
		getSelectionModel().clearSelection();
	}

	public void selectRange(int val1, int val2) {
		clearSelection();
		for (int i = Math.min(val1, val2); i <= Math.max(val1, val2); i++) {
			selectLine(i);
		}
	}

	public void setInternalSelection(String selection) {
		this.internalSelection = selection;
	}

	public Optional<String> getSelection() {
		if (getSelectionModel().getSelectedItems().size() == 0) {
			// nothing is selected => return internal selection
			return Optional.ofNullable(internalSelection);
		} else {
			StringBuilder sb = new StringBuilder();
			for (LogLine l : getSelectionModel().getSelectedItems()) {
				sb.append(l.getLineString()).append("\n");
			}
			return Optional.of(sb.toString());
		}
	}

	public Optional<String> getSingleLineSelection() {
		if (getSelectionModel().getSelectedItems().size() == 0) {
			// nothing is selected => return internal selection
			return Optional.ofNullable(internalSelection);
		}
		return Optional.empty();
	}

	@Override
	public void resize(double arg0, double arg1) {
		super.resize(arg0, arg1);

		Pane header = (Pane) lookup("TableHeaderRow");
		header.setMaxHeight(0);
		header.setMinHeight(0);
		header.setPrefHeight(0);
		header.setVisible(false);
	}
}
