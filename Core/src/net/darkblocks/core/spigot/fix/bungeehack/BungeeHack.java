package net.darkblocks.core.spigot.fix.bungeehack;

import net.craftplugin.craftpluginapi.java.mysql.MySQL;
import net.darkblocks.core.spigot.fix.bungeehack.listener.BungeeHackListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 13.01.2018  00:48.
 */
public class BungeeHack
{
	public BungeeHack(MySQL mySQL, JavaPlugin javaPlugin)
	{
		new BungeeHackListener(mySQL, javaPlugin);
	}
}
