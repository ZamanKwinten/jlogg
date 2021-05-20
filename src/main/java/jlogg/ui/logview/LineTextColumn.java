package jlogg.ui.logview;

import java.util.function.BiConsumer;

import jlogg.datahandlers.FileLineReader;
import jlogg.plugin.LogLine;
import jlogg.ui.GlobalConstants;

class LineTextColumn extends FillRemainingColumn<LogLine> {

	public LineTextColumn(LogFileView parent, BiConsumer<LogFileView, Integer> clickHandler) {
		super(parent, GlobalConstants.defaultFont, FileLineReader::readLineFromFile);

		setCellFactory((param) -> {
			return new LineTextDragCell(parent, clickHandler);
		});

		setSortable(false);
	}
}
