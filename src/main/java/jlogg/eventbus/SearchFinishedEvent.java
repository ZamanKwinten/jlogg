package jlogg.eventbus;

import java.util.List;

import jlogg.plugin.LogLine;

public class SearchFinishedEvent extends SearchResultEvent {

	public SearchFinishedEvent(SearchEvent searchEvent, List<LogLine> loglines) {
		super(searchEvent, loglines, 1.0);
	}
}
