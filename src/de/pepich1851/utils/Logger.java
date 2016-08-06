package de.pepich1851.utils;
import java.util.Calendar;

public class Logger
{
	public static int debugLevel = 1;
	
	public static void info(String message, String caller)
	{
		System.out.println("[INFO" + (debugLevel >= 1 ? " - " + getDateTime() : "") + (debugLevel >= 2 ? " - Thread " + caller + "" : "") + "]: " + message);
	}
	
	public static void warn(String message, String caller)
	{
		System.out.println("[WARN" + (debugLevel >= 1 ? " - " + getDateTime() : "") + (debugLevel >= 2 ? " - Thread " + caller + "" : "") + "]: " + message);
	}
	
	@SuppressWarnings("deprecation")
	public static String getDateTime()
	{
		Calendar c = Calendar.getInstance();
		return c.getTime().toGMTString();
	}
}
