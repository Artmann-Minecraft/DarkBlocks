package net.darkblock.lobby.extras;

import net.darkblock.lobby.extras.cookieclicker.CookieClicker;
import net.darkblocks.dark.java.mysql.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 13.02.2018  00:22.
 */
public class Extras
{
	public Extras(JavaPlugin javaPlugin, MySQL mySQL)
	{
		new CookieClicker(javaPlugin, mySQL);
	}
}
