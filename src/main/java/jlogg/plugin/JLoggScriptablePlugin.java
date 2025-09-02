package jlogg.plugin;

import java.util.List;

public interface JLoggScriptablePlugin {

	public List<String> possibleActions();

	public void run(String details);
}
