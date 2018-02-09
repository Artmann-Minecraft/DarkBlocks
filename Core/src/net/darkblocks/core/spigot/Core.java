package net.darkblocks.core.spigot;

import net.darkblocks.core.spigot.fix.Fix;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.spigot.events.listener.EventsListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 09.01.2018  08:32.
 */
public class Core
{
	public Core(JavaPlugin javaPlugin, MySQL mySQL)
	{
		new EventsListener(javaPlugin);
		new Fix(javaPlugin);
		new net.darkblocks.core.spigot.permissions.Permissions(javaPlugin, mySQL);
		new net.darkblocks.core.universal.permissions.Permissions(mySQL);
	}
}
