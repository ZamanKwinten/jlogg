package jlogg.eventbus;

import java.util.Arrays;
import java.util.List;

public class EventBus {

	private static final EventBus INSTANCE = new EventBus();

	public static EventBus get() {
		return INSTANCE;
	}

	private final List<JLoggEventListener> listeners;

	private EventBus() {
		listeners = Arrays.asList(new EventListener());
	}

	public void submit(SearchEvent event) {
		listeners.forEach(l -> l.on(event));
	}

	public void submit(SearchResultEvent event) {
		listeners.forEach(l -> l.on(event));
	}

	public void submit(IndexStartEvent event) {
		listeners.forEach(l -> l.on(event));
	}

	public void submit(IndexResultEvent event) {
		listeners.forEach(l -> l.on(event));
	}

	public void submit(IndexFinishedEvent event) {
		listeners.forEach(l -> l.on(event));
	}
}
