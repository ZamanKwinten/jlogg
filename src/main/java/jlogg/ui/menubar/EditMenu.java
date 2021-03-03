package jlogg.ui.menubar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import jlogg.ui.GlobalConstants;
import jlogg.ui.GlobalConstants.ShortCut;
import jlogg.ui.MainPane;
import jlogg.ui.popup.GoToPopup;

public class EditMenu extends Menu {
	private final MenuItem copyMenuItem;
	private final MenuItem selectAllMenuItem;
	private final MenuItem findMenuItem;
	private final MenuItem goToMenuItem;

	public EditMenu(MainPane mainPane) {
		super("Edit");

		copyMenuItem = new MenuItemWithAccelerator(ShortCut.COPY);
		copyMenuItem.setOnAction((event) -> {
			mainPane.getCurrentSelectedTab().getSelection().ifPresent((selection) -> {
				final ClipboardContent content = new ClipboardContent();
				content.putString(selection);
				Clipboard.getSystemClipboard().setContent(content);
				event.consume();
			});
		});

		selectAllMenuItem = new MenuItemWithAccelerator(ShortCut.SELECT_ALL);

		findMenuItem = new MenuItemWithAccelerator(ShortCut.OPEN_SEARCH);
		findMenuItem.setOnAction((event) -> {
			if (mainPane.getCurrentSelectedTab() != null) {
				mainPane.getCurrentSelectedTab().showFilteredView();
			}
		});

		goToMenuItem = new MenuItemWithAccelerator(ShortCut.OPEN_GO_TO_LINE);
		goToMenuItem.setOnAction((event) -> {
			if (mainPane.getCurrentSelectedTab() != null) {

				new GoToPopup(GlobalConstants.fileLogLines.get(mainPane.getCurrentSelectedTab().getFile()).size() - 1)
						.open().ifPresent(index -> {
							mainPane.getCurrentSelectedTab().selectLogLine(index);
						});
			}
		});

		getItems().addAll(copyMenuItem, selectAllMenuItem, new SeparatorMenuItem(), findMenuItem, goToMenuItem);
	}
}
