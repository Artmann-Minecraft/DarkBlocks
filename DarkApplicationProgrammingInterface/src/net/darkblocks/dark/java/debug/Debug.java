/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.dark.java.debug;

import lombok.Getter;
import net.darkblocks.dark.java.utils.Logger;

import java.io.File;
import java.util.logging.Level;

/**
 * Created by LartyHD on 12.02.2018  22:00.
 */
class Debug
{
	@Getter
	private static final boolean DEBUG;
	@Getter
	private static final Logger LOGGER;
	
	static
	{
		DEBUG = false;
		LOGGER = new Logger("Debug", "logs" + File.separator + "debug");
	}
	
	public static void print(String message)
	{
		print(Level.INFO, message);
	}
	
	@SuppressWarnings("SameParameterValue")
	private static void print(Level level, String message)
	{
		if (DEBUG)
		{
			LOGGER.log(level, message);
		}
	}
}
	