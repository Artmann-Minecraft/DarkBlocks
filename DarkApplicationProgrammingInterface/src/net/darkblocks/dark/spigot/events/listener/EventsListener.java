/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.events.listener;

import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 * Created by LartyHD on 22.01.2018  00:14.
 */
public class EventsListener implements Listener
{
	public EventsListener(JavaPlugin javaPlugin)
	{
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		event.getEntity().spigot().respawn();
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event)
	{
		event.getPlayer().setVelocity(new Vector(0, 0, 0));
		event.getPlayer().setFireTicks(0);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuitEvent(PlayerQuitEvent event)
	{
		Bukkit.getPluginManager().callEvent(new PlayerDisconnectEvent(event.getPlayer()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKickEvent(PlayerKickEvent event)
	{
		if (!event.isCancelled())
		{
			Bukkit.getPluginManager().callEvent(new PlayerDisconnectEvent(event.getPlayer()));
		}
	}
}