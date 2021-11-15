package jlogg.plugin;

import javafx.scene.Node;

/**
 * A simple interface to make it possible for plugin views to implement the save
 * method.
 * 
 * @author Kwinten
 *
 */
public interface PluginView {

	/**
	 * Method that is being called when JLogg triggers a save command
	 */
	public abstract void save();

	/**
	 * Typically this should just return <this>, this construct is needed since you
	 * can't extend two classes
	 * 
	 * @return
	 */
	public abstract Node getView();
}
