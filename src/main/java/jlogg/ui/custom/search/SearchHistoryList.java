package jlogg.ui.custom.search;

import java.util.LinkedList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jlogg.shared.SearchCriteria;

public class SearchHistoryList {

	private final LinkedList<SearchCriteria> searchCriterias;
	private final ObservableList<SearchCriteria> observableList;

	public SearchHistoryList() {
		searchCriterias = new LinkedList<>();

		observableList = FXCollections.observableArrayList();
	}

	public void add(SearchCriteria criteria) {
		searchCriterias.remove(criteria);
		searchCriterias.addFirst(criteria);

		Platform.runLater(() -> {
			observableList.setAll(searchCriterias);
		});
	}

	public ObservableList<SearchCriteria> get() {
		return observableList;
	}

}
