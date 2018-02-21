/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.lobby.navigator;

import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.spigot.events.cashed.CashedEventsManager;
import net.darkblocks.lobby.navigator.listener.NavigatorListener;
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
