package jlogg.ui.logview;

import java.util.regex.PatternSyntaxException;

import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import jlogg.shared.Filter;
import jlogg.ui.GlobalConstants;
import jlogg.ui.utils.FXUtils;

/**
 * The line in the UI representing the actual text. Has its own implementation
 * on how selection styles are being applied to the cells. Uses an TextField
 * control without custom styling to allow selection of text within a row
 * 
 * @author KWZA
 *
 */
class LineTextDragCell extends DragSelectionCell {

	private final TextField textfield;

	public LineTextDragCell(LogFileView logFileView) {
		super(logFileView);
		setOnMouseDragOver(this::onDragOver);

		textfield = new TextField();
		textfield.setEditable(false);
		textfield.getStyleClass().clear();
		super.initDragAction(textfield);
		textfield.setOnMouseDragOver(this::onDragOver);
		textfield.setOnKeyPressed(this::onKeyEvent);
		textfield.setOnKeyReleased(this::onKeyEvent);

		// Register the listener responsible for managing the selection css
		selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				// This cell is now selected
				applySelectedStyle();
			} else {
				// This cell is no longer selected
				applyDefaultStyle();
			}
		});

		GlobalConstants.filters.addListener(new ListChangeListener<Filter>() {
			@Override
			public void onChanged(Change<? extends Filter> c) {
				// The filters were changed => reiterate over the filter list and apply the
				// filter style
				applyDefaultStyle();
			}
		});

		textfield.setFont(GlobalConstants.defaultFont);
		setAlignment(Pos.CENTER_LEFT);

		setGraphic(textfield);
	}

	public void updateItem(String value, boolean empty) {
		super.updateItem(value, empty);
		if (empty) {
			setText(null);
			textfield.setText(null);
		} else {
			textfield.setText(value);
			setPrefWidth();
		}
		applyDefaultStyle();
	}

	private void setPrefWidth() {
		// 15 px of padding
		double width = FXUtils.calculateTextControlWidth(textfield.getFont(), textfield.getText(), 15);

		textfield.setPrefWidth(width);
		textfield.setMinWidth(width);
		textfield.setMaxWidth(width);

		((LogFileView) selectableContent).updateTextColumnWidth(width);
	}

	/**
	 * Apply the selection CSS to the cell
	 * 
	 * @return boolean value indicating whether the style was applied or not
	 */
	private boolean applySelectedStyle() {
		setBackground(new Background(new BackgroundFill(Color.DODGERBLUE, null, null)));
		textfield.setStyle("-fx-text-fill:rgb(255,255,255)");
		return true;
	}

	/**
	 * Apply the normal CSS to the cell. First checks the filter styles to see
	 * whether one of those have to be applied
	 * 
	 * @return boolean value indicating whether the style was applied or not
	 */
	private boolean applyDefaultStyle() {
		if (!applyFilterStyle()) {
			setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
			textfield.setStyle("-fx-text-fill:rgb(0,0,0)");
		}
		return true;
	}

	/**
	 * Apply any Filter CSS to the cell that happens to match
	 * 
	 * @return boolean value indicating whether any filter style was applied or not
	 */
	private boolean applyFilterStyle() {
		if (textfield.getText() != null && !textfield.getText().isEmpty()) {
			for (Filter f : GlobalConstants.filters) {
				try {
					if (f.matches(textfield.getText())) {
						setBackground(new Background(new BackgroundFill(f.getBackGroundColor(), null, null)));
						textfield.setStyle("-fx-text-fill:" + f.getForeGroundRGB());
						return true;
					}
				} catch (PatternSyntaxException e) {
					e.printStackTrace();
				}
			}

		}
		return false;
	}

	/**
	 * Handling of dragging within the node itself => It should be possible to
	 * select text within the cell.
	 * 
	 * @param event
	 */
	private void onDragOver(MouseDragEvent event) {
		if (getIndex() == selectableContent.getDragStart()) {
			// we are dragging in the line of the node itself => we are trying to select
			// something within the
			// line
			selectableContent.clearSelection();
			selectableContent.setInternalSelection(textfield.getSelectedText());
		}
	}

	/**
	 * Swallow any KeyEvent (The line is readonly anyway) this also prevent the copy
	 * event (ctrl+c) from normal handling
	 * 
	 * @param event
	 */
	private void onKeyEvent(KeyEvent event) {
		// prevent normal handling of copy
		event.consume();
		// fire the event to anyone else interested
		this.getParent().fireEvent(event);
	}
}
