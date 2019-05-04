package jlogg.eventbus;

import com.google.common.eventbus.EventBus;

/**
 * Sole purpose of this class is to have a singelton around a guava eventbus
 * 
 * @author KWZA
 *
 */
public class EventBusFactory {

	private static final EventBusFactory INSTANCE = new EventBusFactory();

	public static EventBusFactory getInstance() {
		return INSTANCE;
	}

	private final EventBus eventbus;

	private EventBusFactory() {
		eventbus = new EventBus();
		eventbus.register(new EventListener());
	}

	public EventBus getEventBus() {
		return eventbus;
	}

}
