package jlogg.ui.logview;

import java.util.Optional;
import java.util.function.BiConsumer;

import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableRow;
import javafx.scene.layout.Pane;
import jlogg.datahandlers.FileLineReader;
import jlogg.plugin.LogLine;
import jlogg.ui.FileTab;
import jlogg.ui.GlobalConstants;
import jlogg.ui.interfaces.DragSelectableContent;
import jlogg.ui.table.AutoFillingTable;
import jlogg.ui.utils.FXUtils;

/**
 * The actual main view in which all the text will be displayed
 * 
 * @author KWZA
 *
 */
public class LogFileView extends AutoFillingTable<LogLine> implements DragSelectableContent {
	private final FileTab mainPane;

	private int dragStart;

	private LineNumberColumn lineNumberColumn;
	private LineTextColumn lineTextColumn;

	private String internalSelection;

	public LogFileView(FileTab mainPane, ObservableList<LogLine> lines) {
		this(mainPane, lines, null);
	}

	public LogFileView(FileTab mainPane, ObservableList<LogLine> lines,
			BiConsumer<LogFileView, Integer> mouseClickHandler) {
		setItems(lines);

		lineNumberColumn = new LineNumberColumn(this);
		lineTextColumn = new LineTextColumn(this, mouseClickHandler);

		addColumn(lineNumberColumn);
		addColumn(lineTextColumn);

		this.mainPane = mainPane;

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
	}

	public int getDragStart() {
		return dragStart;
	}

	public void setDragStart(int startIndex) {
		clearSelection();
		this.dragStart = startIndex;
	}

	private void selectLine(int index) {
		getSelectionModel().select(index, getColumn(lineTextColumn));
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
	public void resize(double width, double height) {
		super.resize(width, height);

		Pane header = (Pane) lookup("TableHeaderRow");
		header.setMaxHeight(0);
		header.setMinHeight(0);
		header.setPrefHeight(0);
		header.setVisible(false);
	}
}
