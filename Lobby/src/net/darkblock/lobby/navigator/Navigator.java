package net.darkblock.lobby.navigator;

import net.darkblock.lobby.navigator.listener.NavigatorListener;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.spigot.events.cashed.CashedEventsManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

/**
 * Created by LartyHD on 08.02.2018  07:17.
 */
public class Navigator
{
	public Navigator(JavaPlugin javaPlugin, MySQL mySQL, Map<String, Boolean> navigatorAnimation, CashedEventsManager cashedEventsManager)
	{
		new NavigatorListener(javaPlugin, mySQL, navigatorAnimation, cashedEventsManager);
	}
}
