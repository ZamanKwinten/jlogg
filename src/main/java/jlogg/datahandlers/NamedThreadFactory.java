package jlogg.datahandlers;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

	private final String prefix;
	private final AtomicInteger number = new AtomicInteger(0);

	public NamedThreadFactory(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, prefix + number.incrementAndGet());
		t.setDaemon(true);
		return t;
	}

}
