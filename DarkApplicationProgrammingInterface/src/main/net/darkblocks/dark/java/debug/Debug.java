package net.darkblocks.dark.java.debug;

import lombok.Getter;
import lombok.Setter;
import net.darkblocks.dark.java.utils.Logger;

import java.io.File;
import java.util.logging.Level;

/**
 * Created by LartyHD on 12.02.2018  22:00.
 */
public class Debug
{
	@Getter
	@Setter
	private static boolean DEBUG;
	private static Logger LOGGER;
	
	static
	{
		DEBUG = false;
		try
		{
			LOGGER = new Logger("Debug", "logs" + File.separator + "debug");
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void print(String message)
	{
		print(Level.INFO, message);
	}
	
	public static void print(Level level, String message)
	{
		if (DEBUG)
		{
			LOGGER.log(level, message);
		}
	}
}