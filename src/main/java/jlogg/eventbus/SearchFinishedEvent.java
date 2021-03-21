package jlogg.eventbus;

import java.util.List;

import jlogg.shared.LogLine;

public class SearchFinishedEvent extends SearchResultEvent {

	public SearchFinishedEvent(SearchEvent searchEvent, List<LogLine> loglines) {
		super(searchEvent, loglines, 1.0);
	}
}
