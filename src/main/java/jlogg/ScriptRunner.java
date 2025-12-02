package jlogg;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import javafx.application.Application;
import javafx.stage.Stage;
import jlogg.plugin.JLogg;
import jlogg.plugin.JLoggScriptablePlugin;
import jlogg.plugin.JLoggScriptablePlugin.IScriptablePluginOptions;
import jlogg.type.JLoggHeadless;
import jlogg.ui.GlobalConstants;
import jlogg.ui.MainStage;

public class ScriptRunner extends Application {

	static String plugin;
	static String[] args;

	public static void main(String[] inputArgs) {
		if (inputArgs.length == 0) {
			writeHelpMessage(System.out);
			System.exit(1);
		}

		if (inputArgs.length == 1 && ("--help".equals(inputArgs[0]) || "-h".equals(inputArgs[0]))) {
			writeHelpMessage(System.out);
			System.exit(0);
		}

		plugin = inputArgs[0];
		args = Arrays.copyOfRange(inputArgs, 1, inputArgs.length);

		Application.launch(ScriptRunner.class);
	}

	private static void writeHelpMessage(PrintStream out) {
		out.println("Usage: JLoggCMD <scriptable plugin name> [plugin options]");

		System.out.println("The names of the scriptable plugins installed in this JLogg instance are:");
		ConstantMgr constantMGR = ConstantMgr.instance();
		constantMGR.setupGlobalConstants();
		constantMGR.loadPlugins();

		GlobalConstants.plugins.stream().forEach(plugin -> {
			if (plugin.plugin() instanceof JLoggScriptablePlugin scriptPlugin) {
				System.out.println("\t- " + plugin.name());
				scriptPlugin.writePluginHelp(System.out);
			}
		});

	}

	@SuppressWarnings("unchecked")
	private static <T extends IScriptablePluginOptions> Optional<JLoggScriptablePlugin<T>> loadScritablePlugin(
			String pluginName) {
		for (var pluginWithMetadata : GlobalConstants.plugins) {
			var plugin = pluginWithMetadata.plugin();
			if (Objects.equals(pluginWithMetadata.name(), pluginName)) {
				System.out.println("Found the plugin: \"%s\"".formatted(pluginName));
				if (plugin instanceof JLoggScriptablePlugin scriptPlugin) {
					return Optional.of(scriptPlugin);
				} else {
					System.out.println(String.format(
							"Found a matching plugin class, but the plugin \"%s\" is not scriptable. Please contact the plugin maintainer",
							pluginName));

					return Optional.empty();
				}
			}
		}

		System.out.println(String.format("Could not find any plugin matching the Plugin Name \"%s\"", pluginName));
		System.out.println("The names of the plugins installed in this JLogg instance are:");
		GlobalConstants.plugins.stream().forEach(plugin -> System.out.println("\t- " + plugin.name()));
		return Optional.empty();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ConstantMgr constantMGR = ConstantMgr.instance();
		constantMGR.setupGlobalConstants();
		constantMGR.loadPlugins();

		loadScritablePlugin(plugin).ifPresentOrElse(plugin -> {
			MainStage mainStage = MainStage.getInstance();
			var pluginOptions = plugin.parseArguments(args);

			var output = pluginOptions.outputFolder();
			if (!output.exists()) {
				if (!output.mkdirs()) {
					System.out.println("Unable to create the output folder");
					System.exit(1);
				}
			}

			pluginOptions.inputFiles().forEach(file -> {
				if (!file.exists()) {
					System.out.println("Unable to open file: " + file.getAbsolutePath());
					System.exit(1);
				}
			});

			JLogg.JLOGG = new JLoggHeadless(output, primaryStage);

			mainStage.getMainPane().openTabs(pluginOptions.inputFiles());

			plugin.run(pluginOptions);
		}, () -> System.exit(1));

		System.exit(0);
	}
}
