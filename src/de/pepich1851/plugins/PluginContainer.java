package de.pepich1851.plugins;

import java.util.ArrayList;
import java.util.HashMap;

import de.pepich1851.exceptions.MissingRequirementException;
import de.pepich1851.exceptions.ReferenceLoopException;
import de.pepich1851.utils.Logger;

public class PluginContainer
{
	private static HashMap<String, Plugin> plugins = new HashMap<String, Plugin>();
	
	public void loadPlugin(Plugin p) throws ReferenceLoopException, MissingRequirementException
	{
		if (p.enabled) return;
		for (String s : p.getHardDependencies())
		{
			Plugin p2 = plugins.get(s);
			if (p2 != null && !p2.enabled)
			{
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(p.getName());
				loadPlugin(p2, temp);
			}
			else if (p2 == null)
			{
				throw new MissingRequirementException("Plugin " + p.getName() + "is missing required plugin " + s + "! Please make sure you installed all required plugins!");
			}
		}
		Logger.info("Enabling " + p.getName(), "PluginContainer");
		p.enable();
	}
	
	public void loadPlugin(Plugin p, ArrayList<String> caller) throws ReferenceLoopException, MissingRequirementException
	{
		if (p.enabled) return;
		if (caller.contains(p.getName()))
			throw new ReferenceLoopException("The plugins " + caller.toArray() + " are referenzing each other as dependencies. Please make sure you installed the right versions of those plugins!");
		
		for (String s : p.getHardDependencies())
		{
			Logger.info("Found dependencie " + s, "PluginContainer");
			Plugin p2 = plugins.get(s);
			if (p2 != null && !p2.enabled)
			{
				caller.add(p.getName());
				loadPlugin(p2, caller);
			}
			else if (p2 == null)
			{
				throw new MissingRequirementException("Plugin " + p.getName() + "is missing required plugin " + s + "! Please make sure you installed all required plugins!");
			}
		}
		Logger.info("Enabling " + p.getName(), "PluginContainer");
		p.enable();
	}
	
	public Plugin getPlugin(String name)
	{
		return plugins.get(name);
	}

	public void add(Plugin p, String name, String author, String version, ArrayList<String> requires, ArrayList<String> depends)
	{
		Logger.info("Adding plugin " + name, "pcontainer");
		p.setAuthor(author);
		p.setVersion(version);
		p.setName(name);
		p.setHardDependencies(requires);
		p.setSoftDependencies(depends);
		plugins.put(p.getName(), p);
	}
	
	public void enableAll()
	{
		Logger.info("Enabling all plugins...", "PluginContainer");
		for (Plugin p : plugins.values())
			try
			{
				loadPlugin(p);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	}
}
