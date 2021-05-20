package jlogg.ui.logview;

import jlogg.plugin.LogLine;
import jlogg.ui.GlobalConstants;

class LineNumberColumn extends FitContentColumn<LogLine> {
	public LineNumberColumn(LogFileView parent) {
		super(GlobalConstants.defaultFont, (l) -> " " + l.getLineNumber());

		setCellFactory((param) -> {
			return new LineNumberDragCell(parent);
		});

		setSortable(false);
	}
}
