package net.darkblocks.bedwars.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 10.01.2018  18:44.
 */
public class CountdownListener implements Listener
{
	public CountdownListener(JavaPlugin javaPlugin)
	{
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
}
