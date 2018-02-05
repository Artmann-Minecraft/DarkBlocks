package net.darkblocks.core.spigot.fix.bungeehack;

import net.darkblocks.core.spigot.fix.bungeehack.listener.BungeeHackListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 13.01.2018  00:48.
 */
public class BungeeHack
{
	public BungeeHack(JavaPlugin javaPlugin)
	{
		new BungeeHackListener(javaPlugin);
	}
}
