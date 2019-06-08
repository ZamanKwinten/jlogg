package jlogg.eventbus;

import java.util.List;

import jlogg.shared.LogLine;

public class SearchFinishedEvent extends SearchResultEvent {

	public SearchFinishedEvent(List<LogLine> loglines) {
		super(loglines, 1.0);
	}
}
