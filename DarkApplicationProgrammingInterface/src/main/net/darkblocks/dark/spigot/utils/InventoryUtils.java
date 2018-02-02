package net.darkblocks.dark.spigot.utils;

/**
 * Created by LartyHD on 04.01.2018  18:46.
 */
public class InventoryUtils
{
	public static int getInventorySize(int size)
	{
		if (size < 10)
		{
			return 9;
		}
		if (size < 19)
		{
			return 18;
		}
		if (size < 28)
		{
			return 27;
		}
		if (size < 37)
		{
			return 36;
		}
		if (size < 46)
		{
			return 45;
		}
		if (size < 55)
		{
			return 54;
		}
		return 0;
	}
}
