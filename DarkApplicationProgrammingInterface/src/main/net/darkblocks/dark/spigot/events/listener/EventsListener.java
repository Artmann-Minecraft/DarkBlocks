package net.darkblocks.dark.spigot.events.listener;

import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
		Player entity = event.getEntity();
		entity.spigot().respawn();
		entity.setVelocity(new Vector(0, 0, 0));
		entity.setFireTicks(0);
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event)
	{
		Bukkit.getPluginManager().callEvent(new PlayerDisconnectEvent(event.getPlayer()));
	}
	
	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent event)
	{
		Bukkit.getPluginManager().callEvent(new PlayerDisconnectEvent(event.getPlayer()));
	}
}