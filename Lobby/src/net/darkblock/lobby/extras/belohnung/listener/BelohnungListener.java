package net.darkblock.lobby.extras.belohnung.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 16.02.2018  13:44.
 */
public class BelohnungListener implements Listener
{
	public BelohnungListener(JavaPlugin javaPlugin)
	{
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
}
