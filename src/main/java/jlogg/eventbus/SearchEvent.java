package jlogg.eventbus;

import jlogg.shared.SearchCriteria;

public class SearchEvent {

	private final SearchCriteria criteria;

	public SearchEvent(SearchCriteria criteria) {
		this.criteria = criteria;
	}

	public SearchCriteria getCriteria() {
		return criteria;
	}
}
