/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.dark.spigot.listener;

import net.darkblocks.dark.java.utils.ServerState;
import net.darkblocks.dark.spigot.events.ServerStateChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 29.11.2017  14:06.
 */
public class SaveTimeListener implements Listener
{
	public SaveTimeListener(JavaPlugin javaPlugin)
	{
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
	
	@EventHandler
	public void onServerStateChange(ServerStateChangeEvent event)
	{
		if (event.getNewServerState() != ServerState.SAVETIME)
		{
			HandlerList.unregisterAll(this);
		}
	}
}
