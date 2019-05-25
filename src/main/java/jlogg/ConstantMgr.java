package jlogg;

/**
 * Class to define some constants used throughout the application TODO =>
 * Memento pattern
 * 
 * @author Kwinten
 *
 */
public class ConstantMgr {
	private static final ConstantMgr instance = new ConstantMgr();

	public static ConstantMgr instance() {
		return instance;
	}

	public final int indexServiceThreadCount;

	public final int searchServiceThreadCount;

	private ConstantMgr() {
		// TODO read from external location
		indexServiceThreadCount = 1;
		searchServiceThreadCount = 1;

		// See globalconstants
	}
}
