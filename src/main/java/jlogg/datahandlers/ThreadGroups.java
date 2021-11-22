package jlogg.datahandlers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jlogg.ConstantMgr;

public class ThreadGroups {

	/**
	 * Define the Executor service used for searching
	 * 
	 */
	static final ExecutorService searchService = Executors.newFixedThreadPool(
			ConstantMgr.instance().searchServiceThreadCount, new NamedThreadFactory("search-thread-"));

	/**
	 * Define the Executor service used for indexing
	 */
	static final ExecutorService indexService = Executors.newFixedThreadPool(
			ConstantMgr.instance().indexServiceThreadCount, new NamedThreadFactory("index-thread-"));

	/**
	 * Define the Executor service for plugin
	 */
	static final ExecutorService pluginIterationService = Executors.newFixedThreadPool(1,
			new NamedThreadFactory("plugin-iterate-"));

	/**
	 * Shutdown all thread pools, prevents blocking the shutdown of the application
	 */
	public static void forceStop() {
		searchService.shutdownNow();
		indexService.shutdownNow();
		pluginIterationService.shutdownNow();
	}
}
