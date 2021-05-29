package jlogg.ui.logview;

import java.util.function.Consumer;

import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import jlogg.plugin.LogLine;
import jlogg.ui.FileTab;
import jlogg.ui.table.JLoggLogFileView;

/**
 * The actual main view in which all the text will be displayed
 * 
 * @author KWZA
 *
 */
public class LogFileView extends JLoggLogFileView {
	private final FileTab mainPane;

	public LogFileView(FileTab mainPane, ObservableList<LogLine> lines) {
		this(mainPane, lines, null);
	}

	public LogFileView(FileTab mainPane, ObservableList<LogLine> lines, Consumer<LogLine> mouseClickHandler) {
		super(lines, mouseClickHandler);

		this.mainPane = mainPane;
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

	@Override
	protected void selectLine(int index) {
		super.selectLine(index);

		mainPane.setLastSelection(this);
	}
}
