/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.dark.spigot.events.cashed;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 22.01.2018  00:19.
 */
public interface CashedEvent extends Listener
{
	default void init(JavaPlugin javaPlugin, CashedEventsManager cashedEventsManager)
	{
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
		cashedEventsManager.register(this);
	}
}
