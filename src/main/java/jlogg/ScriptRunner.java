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
	static List<File> files;

	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println(
					"Unexpected amount of parameters expected: <plugin class> <plugin details> <list of files>");
		}

		pluginClass = args[0];
		pluginDetails = args[1];

		files = new ArrayList<>();
		for (int i = 2; i < args.length; i++) {
			files.add(new File(args[i]));
		}

		Application.launch(ScriptRunner.class);
	}

	private static Optional<JLoggScriptablePlugin> loadScritablePlugin(String pluginClass) {

		for (var plugin : GlobalConstants.plugins) {

			if (Objects.equals(plugin.getClass().getCanonicalName(), pluginClass)) {
				System.out.println("Found a class!");

				if (plugin instanceof JLoggScriptablePlugin) {
					return Optional.of((JLoggScriptablePlugin) plugin);
				} else {
					System.out.println(String.format(
							"Found a matching plugin class, but the plugin {%s} is not scriptable. Please contact the plugin maintainer",
							pluginClass));

					return Optional.empty();
				}
			}
		}

		System.out.println(String.format("Could not find any plugin matching the Plugin Class {%s}", pluginClass));
		return Optional.empty();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		ConstantMgr constantMGR = ConstantMgr.instance();
		constantMGR.setupGlobalConstants();
		constantMGR.loadPlugins();

		JLogg.JLOGG = new JLoggHeadless();

		loadScritablePlugin(pluginClass).ifPresent(plugin -> {
			MainStage mainStage = MainStage.getInstance();
			mainStage.getMainPane().openTabs(files);

			plugin.run(pluginDetails);
		});

		System.exit(0);
	}
}
