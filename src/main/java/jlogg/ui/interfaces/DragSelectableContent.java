package jlogg.ui.interfaces;

/**
 * Interface that needs to be implemented by any component that has to be selectable through a dragging motion (used int
 * he logview classes)
 * 
 * @author KWZA
 *
 */
public interface DragSelectableContent {

	/**
	 * Clear the current selection in the component
	 */
	void clearSelection();

	/**
	 * Select a range of rows
	 * 
	 * @param start
	 * @param end
	 */
	void selectRange(int start, int end);

	/**
	 * Set the row at which the drag started
	 * 
	 * @param index
	 */
	void setDragStart(int index);

	/**
	 * Get the row at which the drag started
	 * 
	 * @return
	 */
	int getDragStart();

	/**
	 * Set the content of the internal selection made within the drag start row
	 * 
	 * @param internalSelection
	 */
	void setInternalSelection(String internalSelection);
}
