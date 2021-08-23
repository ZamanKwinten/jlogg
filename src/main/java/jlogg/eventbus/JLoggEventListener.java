package jlogg.eventbus;

public interface JLoggEventListener {

	void on(SearchEvent event);

	void on(SearchResultEvent event);

	void on(IndexStartEvent event);

	void on(IndexResultEvent event);

	void on(IndexFinishedEvent event);
}
