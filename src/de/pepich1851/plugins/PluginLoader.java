package de.pepich1851.plugins;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.pepich1851.utils.Logger;
import de.pepich1851.utils.Main;

public class PluginLoader
{
	public void loadUp(File root)
	{
		File f = new File(root.getPath() + "/plugins");
		for (File g : f.listFiles())
		{
			try
			{
				Logger.info("Found file " + g.getPath(), "plloader");
				boolean skip = !g.getName().endsWith(".jar"), hasPluginYml = false;
				if (!skip)
				{
					ZipFile zip = new ZipFile(g.getPath());
					Enumeration<? extends ZipEntry> e = zip.entries();
					String main = "", author = "", version = "", name = "", depends = "", requires = "";
					
					while(e.hasMoreElements() && !skip)
					{
						ZipEntry entry = e.nextElement();
						if (entry.getName().equals("plugin.yml"))
					    {
							hasPluginYml = true;
					    	InputStream in = zip.getInputStream(entry);
					    	BufferedReader read = new BufferedReader(new InputStreamReader(in));
					    	String s = "";
					    	while ((s = read.readLine()) != null)
					    	{
					    		if (s.startsWith("name:"))
					    			name = s.replaceFirst("name:", "").trim();
					    		else if (s.startsWith("author:"))
					    			author = s.replaceFirst("author:", "").trim();
					    		else if (s.startsWith("main:"))
					    			main = s.replaceFirst("main:", "").trim();
					    		else if (s.startsWith("version:"))
					    			version = s.replaceFirst("version:", "").trim();
					    		else if (s.startsWith("depends:"))
					    			depends = s.replaceFirst("depends:", "").trim();
					    		else if (s.startsWith("requires:"))
					    			requires = s.replaceFirst("requires:", "").trim();
					    	}
					    	break;
					    }
					}
					
					if (!hasPluginYml)
					{
						Logger.warn("plugin.yml of " + g.getName() + " is missing, couldn't load plugin...", "plloader");
						continue;
					}
					
					if ((name.equals("")) || (main.equals("")))
		    		{
			    		Logger.warn("plugin.yml of " + g.getName() + " is invalid (missing name or main class). Skipping this file...", "plloader");
			    		continue;
		    		}
					
					ClassLoader loader = URLClassLoader.newInstance(new URL[] {new URL("file:" + g.getPath())}, ClassLoader.getSystemClassLoader());
					Class<? extends Plugin> pl = null;
					e = zip.entries();
					while (e.hasMoreElements())
					{
						String filename = e.nextElement().getName().replace("/", ".");
						Logger.info(filename, "me");
						if (filename.endsWith(".class"))
						{
							Class<?> clazz = loader.loadClass(filename.replace(".class", ""));
							if (filename.replace(".class", "").equals(main))
							{
								pl = clazz.asSubclass(Plugin.class);
								Main.pc.add(pl.newInstance(), name, author, version, asArray(requires), asArray(depends));
							}
						}
					}
					
					zip.close();
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
	    }
		Main.pc.enableAll();
	}
	
	public static ArrayList<String> asArray(String raw)
	{
		if (!raw.startsWith("[") || !raw.endsWith("]"))
			return new ArrayList<String>();
		raw = raw.substring(1, raw.length() - 1);
		boolean quotes = false;
		ArrayList<String> entries = new ArrayList<String>();
		String entry = "";
		for (char c : raw.toCharArray())
		{
			if (!quotes)
			{
				if (c == ',') continue;
				if (c == '\"')
					quotes = true;
				else return null;
			}
			else
			{
				if (c != '\"')
					entry += c;
				else
				{
					quotes = false;
					entries.add(entry);
					entry = "";
				}
			}
		}
		return entries;
	}
}
