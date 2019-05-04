package jlogg.eventbus;

import com.google.common.eventbus.Subscribe;

import aaatemporary.LoremIpsum;
import jlogg.shared.LogLine;
import jlogg.shared.SearchCriteria;
import jlogg.ui.GlobalConstants;

public class EventListener {
	@Subscribe
	private void onSearchRequest(SearchEvent searchEvent) {
		GlobalConstants.searchResults.clear();

		// Should go to the back end but for now we will create a very dumb
		// implementation using what we've got

		SearchCriteria criteria = searchEvent.getCriteria();

		for (int i = 0; i < LoremIpsum.getLines().size(); i++) {
			if (criteria.matches(LoremIpsum.getLines().get(i))) {
				EventBusFactory.getInstance().getEventBus().post(new SearchResultEvent(new LogLine(i)));
			}
		}
	}

	@Subscribe
	private void onSearchResult(SearchResultEvent searchResult) {
		GlobalConstants.searchResults.add(searchResult.getLogLine());
	}
}
