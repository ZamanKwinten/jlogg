package jlogg.ui.logview;

import java.util.Optional;
import java.util.function.BiConsumer;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import jlogg.datahandlers.FileLineReader;
import jlogg.plugin.LogLine;
import jlogg.ui.FileTab;
import jlogg.ui.GlobalConstants;
import jlogg.ui.interfaces.DragSelectableContent;
import jlogg.ui.utils.FXUtils;

/**
 * The actual main view in which all the text will be displayed
 * 
 * @author KWZA
 *
 */
public class LogFileView extends TableView<LogLine> implements DragSelectableContent {
	private final FileTab mainPane;

	private final LineNumberColumn lineNumberColumn;
	private final LineTextColumn lineTextColumn;

	private int dragStart;

	private String internalSelection;

	private final SimpleDoubleProperty maxTextColumnWidth = new SimpleDoubleProperty();
	private final SimpleDoubleProperty maxLineNumberColumnWidth = new SimpleDoubleProperty();

	public LogFileView(FileTab mainPane, ObservableList<LogLine> lines) {
		this(mainPane, lines, null);
	}

	public LogFileView(FileTab mainPane, ObservableList<LogLine> lines,
			BiConsumer<LogFileView, Integer> mouseClickHandler) {
		super(lines);
		this.mainPane = mainPane;

		lineNumberColumn = new LineNumberColumn(maxLineNumberColumnWidth);
		lineTextColumn = new LineTextColumn(maxTextColumnWidth);

		lineNumberColumn.setCellFactory((param) -> {
			return new LineNumberDragCell(this);
		});

		lineTextColumn.setCellFactory((param) -> {
			return new LineTextDragCell(this, mouseClickHandler);
		});

		getColumns().add(lineNumberColumn);
		getColumns().add(lineTextColumn);

		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		getSelectionModel().setCellSelectionEnabled(true);

		getStyleClass().add("logfileview");

		setRowFactory(ts -> {
			TableRow<LogLine> row = new TableRow<>();
			row.getStyleClass().clear();
			double size = FXUtils.caculateTextControlHeight(GlobalConstants.defaultFont.getValue());
			row.setMaxHeight(size);
			row.setPrefHeight(size);
			row.setMinHeight(size);

			GlobalConstants.defaultFont.addListener((obs, o, n) -> {
				double fontSize = FXUtils.caculateTextControlHeight(n);
				row.setMaxHeight(fontSize);
				row.setPrefHeight(fontSize);
				row.setMinHeight(fontSize);
			});

			return row;
		});

		maxTextColumnWidth.set(Screen.getPrimary().getBounds().getWidth());
	}

	void updateTextColumnWidth(double value) {
		if (value > maxTextColumnWidth.doubleValue()) {
			maxTextColumnWidth.set(value);
		}
	}

	void resetLineNumberColumnWidth() {
		maxLineNumberColumnWidth.set(0);
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
				sb.append(FileLineReader.readLineFromFile(l)).append("\n");
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
