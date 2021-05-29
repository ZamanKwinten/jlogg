package jlogg.ui.table;

import java.util.Optional;
import java.util.function.Consumer;

import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableRow;
import javafx.scene.layout.Pane;
import jlogg.datahandlers.FileLineReader;
import jlogg.plugin.LogLine;
import jlogg.ui.GlobalConstants;
import jlogg.ui.interfaces.DragSelectableContent;
import jlogg.ui.logview.LineNumberColumn;
import jlogg.ui.logview.LineTextColumn;
import jlogg.ui.utils.FXUtils;

public class JLoggLogFileView extends AutoFillingTable<LogLine> implements DragSelectableContent {

	private int dragStart;
	protected String internalSelection;

	private LineNumberColumn lineNumberColumn;
	private LineTextColumn lineTextColumn;

	public JLoggLogFileView(ObservableList<LogLine> lines, Consumer<LogLine> mouseClickHandler) {
		setItems(lines);

		lineNumberColumn = new LineNumberColumn(this);
		lineTextColumn = new LineTextColumn(this, mouseClickHandler);

		addColumn(lineNumberColumn);
		addColumn(lineTextColumn);

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

	@Override
	public void clearSelection() {
		getSelectionModel().clearSelection();
	}

	@Override
	public void selectRange(int start, int end) {
		clearSelection();
		for (int i = Math.min(start, end); i <= Math.max(start, end); i++) {
			selectLine(i);
		}
	}

	protected void selectLine(int index) {
		getSelectionModel().select(index, getColumn(lineTextColumn));
	}

	@Override
	public void setDragStart(int index) {
		clearSelection();
		this.dragStart = index;
	}

	@Override
	public int getDragStart() {
		return dragStart;
	}

	@Override
	public void setInternalSelection(String internalSelection) {
		this.internalSelection = internalSelection;
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

	public Optional<String> getSelection() {
		if (getSelectionModel().getSelectedItems().size() == 0) {
			// nothing is selected => return internal selection
			return Optional.ofNullable(internalSelection);
		} else {
			StringBuilder sb = new StringBuilder();
			for (String line : FileLineReader.readLinesFromFile(getSelectionModel().getSelectedItems())) {
				sb.append(line).append(System.lineSeparator());
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

}
