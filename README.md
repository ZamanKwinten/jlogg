# JLogg

JLogg is a JavaFX based port of [glogg](https://glogg.bonnefon.org/)
In essence it's a log file searching tool made for searching in large log files.

## Features
- [x] Searching in the log files (using ctrl/command+F) 
- [x] Highlighting lines in the log file using regular expressions (using ctrl/command+H) 
- [ ] Make it possible to have custom plugins to make it easier to investigate your log files

### Searching 
Searches can be performed by pressing ctrl/command+F. 
This opens the search pane in this pane you can setup multiple options: 
- The text to search for: JLogg will interpret this text as regular expression by default. If the regex is invalid it will fall back on exact matching 
- Ignore Case: whether the search should ignore the casing or not 
- Search all files: defines whether the search scope should be limited to the current tab or whether it should search in all tabs. When using this option you are prompted to define the order in which JLogg should search through the tabs. 

### Highlighting
Highlighting of interesting lines can be done by pressing ctrl/command +H.
This opens the Filter pane. 
This pane allows you to add, reorder or remove filters. These filters are persisted when JLogg is stopped so that they can be reused in your next session. 
- Adding a filter: this can be done by clicking the add button. 
	- The Matching Pattern defines the pattern to which lines should match to apply the highlight 
	- Ignore Case: whether casing should be ignored or not 
	- Fore/Background Color: the styling to apply onto a highlighted line 
- Reordering filters: Filters will be applied from top to bottom. If a line is matched by multiple filters the first filter styling will be applied 
- Removing filters: remove a filter from the list

## Installation 
### Windows
On windows it's just as easy as downloading the msi of one of the releases and running the msi. 

### MacOS
On apple devices, you'll have to disable the quaratine flag before installing the dmg (using: xattr -d com.apple.quarantine path_to_file). 
I'm not part of the apple developer program nor do I have the intention to sign up for this app. 
