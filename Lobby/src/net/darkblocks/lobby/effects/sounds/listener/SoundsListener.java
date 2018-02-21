/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.lobby.effects.sounds.listener;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 15.02.2018  22:33.
 */
public class SoundsListener implements Listener
{
	public SoundsListener(JavaPlugin javaPlugin)
	{
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
	
	@EventHandler
	public void on(PlayerItemHeldEvent event)
	{
		if (event.getPlayer().getInventory().getItem(event.getNewSlot()) != null)
		{
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_PICKUP, 3F, 1F);
		}
		else
		{
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_PICKUP, 1F, 1F);
		}
	}
}
