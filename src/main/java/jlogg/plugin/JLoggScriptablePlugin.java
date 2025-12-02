package jlogg.plugin;

import java.io.File;
import java.io.PrintStream;
import java.util.List;

import jlogg.plugin.JLoggScriptablePlugin.IScriptablePluginOptions;

public interface JLoggScriptablePlugin<T extends IScriptablePluginOptions> {

	public interface IScriptablePluginOptions {
		File outputFolder();

		List<File> inputFiles();
	}

	public void writePluginHelp(PrintStream out);

	public void run(T details);

	public T parseArguments(String[] args);
}
