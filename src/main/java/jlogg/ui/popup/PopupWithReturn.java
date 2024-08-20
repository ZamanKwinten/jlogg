package jlogg.ui.popup;

import java.util.Optional;

import javafx.beans.value.ObservableBooleanValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jlogg.ui.MainStage;
import jlogg.ui.utils.StyledScene;

abstract class PopupWithReturn<T> extends Stage {
	protected final VBox content;

	protected boolean isCanceled = false;

	protected PopupWithReturn(String title) {
		setTitle(title);
		content = new VBox(5.0);

		content.setPadding(new Insets(15));

		Scene s = new StyledScene(content);

		setScene(s);
		setX(MainStage.getInstance().getX());
		setY(MainStage.getInstance().getY());
		centerOnScreen();

		initModality(Modality.APPLICATION_MODAL);
	}

	public class CancelableFooter extends HBox {
		private final Button primary;

		public CancelableFooter(Button... buttons) {
			super(5.0);
			setAlignment(Pos.CENTER_RIGHT);
			this.primary = buttons[0];

			getChildren().addAll(buttons);
		}

		@Override
		public void requestFocus() {
			primary.requestFocus();
		}
	}

	/**
	 * Create a footer HBox with an action & cancel button
	 * 
	 * @param actionLabel
	 * @param actionDisabled - Allows binding a boolean which will control whether
	 *                       the action button is enabled or not
	 * @return
	 */
	public CancelableFooter getCancelableFooterBox(String actionLabel, ObservableBooleanValue actionDisabled) {
		Button search = new Button(actionLabel);
		search.setOnAction(event -> {
			// redundant
			isCanceled = false;
			super.close();
		});
		if (actionDisabled != null) {
			search.disableProperty().bind(actionDisabled);
		}

		Button cancel = new Button("Cancel");
		cancel.setOnAction(event -> {
			isCanceled = true;
			super.close();
		});

		return new CancelableFooter(search, cancel);
	}

	public CancelableFooter getCloseFooterBox() {

		Button close = new Button("Close");
		close.setOnAction(event -> {
			super.close();
		});

		return new CancelableFooter(close);
	}

	public Optional<T> open() {
		super.showAndWait();
		return isCanceled ? Optional.empty() : Optional.ofNullable(getReturnValue());
	}

	protected abstract T getReturnValue();

	@Override
	public void showAndWait() {
		throw new UnsupportedOperationException("Use open instead");
	}
}
