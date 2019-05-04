package jlogg.ui.css;

/**
 * Loader class to ensure that all css can be in the same package
 * 
 * @author KWZA
 *
 */
public class ResourceLoader {

	public static String loadResourceFile(String fileName) {
		return ResourceLoader.class.getResource(fileName).toExternalForm();
	}
}
