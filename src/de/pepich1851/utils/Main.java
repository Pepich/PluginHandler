package de.pepich1851.utils;

import java.io.File;
import java.net.URISyntaxException;

import de.pepich1851.plugins.PluginContainer;
import de.pepich1851.plugins.PluginLoader;

public class Main
{
	public static File rootDirectory;
	public static final long startup = System.currentTimeMillis();
	public static final PluginContainer pc = new PluginContainer();
	
	public static void main(String[] args) throws URISyntaxException
	{
		Logger.debugLevel = 1;
		Logger.info("Starting up Server, locating resources:", "main");
		rootDirectory = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
				.getParentFile();
		Logger.info("Base server is located at: "
				+ Main.class.getProtectionDomain().getCodeSource().getLocation().toString(), "main");
		File plugins = new File(rootDirectory.getPath() + "/plugins");
		Logger.info("Core loaded, loading plugins now", "main");
		Logger.info("Plugins are located at: " + plugins.getPath(), "main");
		final PluginLoader pl = new PluginLoader();
		pl.loadUp(rootDirectory);
	}
}
