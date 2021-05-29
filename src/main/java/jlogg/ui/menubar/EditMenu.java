package jlogg.ui.menubar;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import jlogg.ui.FileTab;
import jlogg.ui.GlobalConstants;
import jlogg.ui.GlobalConstants.ShortCut;
import jlogg.ui.MainPane;
import jlogg.ui.popup.GoToPopup;

public class EditMenu extends Menu {
	private final MenuItem copyMenuItem;
	private final MenuItem findMenuItem;
	private final MenuItem findAllMenuItem;
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

		findMenuItem = new MenuItemWithAccelerator(ShortCut.OPEN_SEARCH);
		findMenuItem.setOnAction((event) -> {
			if (mainPane.getCurrentSelectedTab() != null) {
				FileTab filetab = mainPane.getCurrentSelectedTab();
				filetab.showSingleFileSearchView(filetab.getSingleLineSelection().orElse(null));
			}
		});

		findAllMenuItem = new MenuItemWithAccelerator(ShortCut.OPEN_ALL_SEARCH);
		findAllMenuItem.setOnAction((event) -> {
			if (mainPane.getCurrentSelectedTab() != null) {
				FileTab filetab = mainPane.getCurrentSelectedTab();
				mainPane.getCurrentSelectedTab().showMultiFileSearchView(filetab.getSingleLineSelection().orElse(null));
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

		getItems().addAll(copyMenuItem, new SeparatorMenuItem(), findMenuItem, findAllMenuItem, goToMenuItem);
	}
}
