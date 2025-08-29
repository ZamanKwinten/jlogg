package jlogg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javafx.application.Application;
import javafx.stage.Stage;
import jlogg.plugin.JLogg;
import jlogg.plugin.JLoggScriptablePlugin;
import jlogg.type.JLoggHeadless;
import jlogg.ui.GlobalConstants;
import jlogg.ui.MainStage;

public class ScriptRunner extends Application {

	static String pluginClass;
	static String pluginDetails;
	static File outputFolder;
	static List<File> files;

	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println(
					"Unexpected amount of parameters expected: <plugin name> <plugin action> <output folder> <list of files>");
			System.exit(1);
		}

		pluginClass = args[0];
		pluginDetails = args[1];
		outputFolder = new File(args[2]);
		outputFolder.mkdirs();
		if (!outputFolder.exists()) {
			System.out.println("Output folder does not exist and cannot be created");
			System.exit(1);
		}

		files = new ArrayList<>();
		for (int i = 3; i < args.length; i++) {
			files.add(new File(args[i]));
		}

		Application.launch(ScriptRunner.class);
	}

	private static Optional<JLoggScriptablePlugin> loadScritablePlugin(String pluginName) {
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

		JLogg.JLOGG = new JLoggHeadless(outputFolder);

		loadScritablePlugin(pluginClass).ifPresentOrElse(plugin -> {
			MainStage mainStage = MainStage.getInstance();
			mainStage.getMainPane().openTabs(files);

			plugin.run(pluginDetails);
		}, () -> System.exit(1));

		System.exit(0);
	}
}
