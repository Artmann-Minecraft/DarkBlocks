/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.listener;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 19.02.2018  02:34.
 */
@Getter
public abstract class Listener implements org.bukkit.event.Listener
{
	private final JavaPlugin javaPlugin;
	
	public Listener(@NonNull JavaPlugin javaPlugin)
	{
		this.javaPlugin = javaPlugin;
		init();
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
	
	@SuppressWarnings("WeakerAccess")
	public void init()
	{
	}
}
