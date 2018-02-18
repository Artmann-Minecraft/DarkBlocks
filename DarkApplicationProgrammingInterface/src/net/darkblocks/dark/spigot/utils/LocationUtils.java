/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.utils;

import org.bukkit.Location;

import java.util.Random;

/**
 * Created by LartyHD on 17.02.2018  16:25.
 */
public class LocationUtils
{
	public static Location randomLook(Location location)
	{
		Random random = new Random();
		int yaw = 0;
		if (random.nextInt(3) == 0)
		{
			yaw = 0;
		}
		else if (random.nextInt(3) == 1)
		{
			yaw = 90;
		}
		else if (random.nextInt(3) == 2)
		{
			yaw = 180;
		}
		else if (random.nextInt(3) == 3)
		{
			yaw = -90;
		}
		return new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), yaw, location.getPitch());
	}
}
