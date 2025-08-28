package jlogg.build;

class BuildDetails{
	static final String version = "${project.version}";
	
	static final Boolean isPortable = Boolean.parseBoolean("${build.isportable}");
}