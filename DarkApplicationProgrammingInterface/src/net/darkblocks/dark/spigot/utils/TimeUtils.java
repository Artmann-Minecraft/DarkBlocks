/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.utils;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 09.11.2017  23:54.
 */
@SuppressWarnings("ALL")
public class TimeUtils
{
	public static String getZeit(long time)
	{
		String remainingTime = "";
		long minutes = 0;
		long hours = 0;
		while (time >= 60)
		{
			time -= 60;
			minutes++;
		}
		while (minutes >= 60)
		{
			minutes -= 60;
			hours++;
		}
		if (hours == 1)
		{
			remainingTime = remainingTime + "" + IMPORTANT + "eine" + TEXT + " Stunde ";
		}
		else if (hours != 0)
		{
			remainingTime = remainingTime + "" + IMPORTANT + hours + TEXT + " Stunden ";
		}
		if (minutes == 1)
		{
			remainingTime = remainingTime + "" + IMPORTANT + "eine" + TEXT + " Minute ";
		}
		else if (minutes != 0)
		{
			remainingTime = remainingTime + "" + IMPORTANT + minutes + TEXT + " Minuten ";
		}
		if (time == 1)
		{
			remainingTime = remainingTime + "" + IMPORTANT + time + TEXT + " Sekunde ";
		}
		else if (time != 0)
		{
			remainingTime = remainingTime + "" + IMPORTANT + time + TEXT + " Sekunden ";
		}
		if (remainingTime.equalsIgnoreCase(""))
		{
			return IMPORTANT + "0 " + TEXT + "Sekunden ";
		}
		return remainingTime.substring(0, remainingTime.length() - 1);
	}
}
