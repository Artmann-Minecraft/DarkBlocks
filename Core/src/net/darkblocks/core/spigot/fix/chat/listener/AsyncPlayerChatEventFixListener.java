package net.darkblocks.core.spigot.fix.chat.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 10.01.2018  18:27.
 */
public class AsyncPlayerChatEventFixListener implements Listener
{
	public AsyncPlayerChatEventFixListener(JavaPlugin javaPlugin)
	{
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(AsyncPlayerChatEvent event)
	{
		event.setCancelled(true);
		for (Player players : event.getRecipients())
		{
			players.sendMessage(event.getMessage());
		}
	}
}
