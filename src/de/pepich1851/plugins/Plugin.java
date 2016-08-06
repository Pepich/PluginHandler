package de.pepich1851.plugins;

import java.util.ArrayList;

public abstract class Plugin
{
	private String version, name, author;
	public boolean enabled = false;
	private ArrayList<String> requires = new ArrayList<String>(), depends = new ArrayList<String>();
	
	public void init(String version, String name, String author)
	{
		this.version = version;
		this.name = name;
		this.author = author;
	}
	
	public void enable()
	{
		onEnable();
		enabled = true;
	}
	
	public abstract void onEnable();
	public abstract void onDisable();
	
	public String getVersion()
	{
		return version;
	}
	public void setVersion(String version)
	{
		this.version = version;
	}
	public boolean isEnabled()
	{
		return enabled;
	}
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	public String getAuthor()
	{
		return author;
	}
	public void setAuthor(String author)
	{
		this.author = author;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}

	public void setHardDependencies(ArrayList<String> requires)
	{
		this.requires = requires;
	}
	
	public void setSoftDependencies(ArrayList<String> depends)
	{
		this.depends = depends;
	}
	
	public ArrayList<String> getHardDependencies()
	{
		return requires;
	}
	
	public ArrayList<String> getSoftDependencies()
	{
		return depends;
	}
}
