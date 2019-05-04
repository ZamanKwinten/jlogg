package jlogg.ui.interfaces;

/**
 * Interface allowing components that can dynamically hide/show themselves to be defined in a generic way
 * 
 * @author KWZA
 *
 */
public interface HideableNode {

	/**
	 * Hide the component
	 */
	void hide();

	/**
	 * Show the component
	 */
	void show();
}
