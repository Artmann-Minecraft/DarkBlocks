package net.darkblocks.dark.java.utils;

import java.util.Arrays;

/**
 * Created by LartyHD on 03.01.2018  17:38.
 */
@SuppressWarnings("ALL")
public class Utils
{
	private static char nummerToHexa(short nummber)
	{
		return Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F').get(nummber);
	}
}
