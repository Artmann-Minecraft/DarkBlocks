/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.gungame.listener;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by LartyHD on 18.02.2018  15:36.
 */
@Getter
public class ShopListener implements Listener
{
	private final Set<String> healer;
	private final Set<String> killer;
	private final Set<String> updater;
	private final Set<String> keepInv;
	
	public ShopListener(JavaPlugin javaPlugin)
	{
		this.healer = new HashSet<>();
		this.killer = new HashSet<>();
		this.updater = new HashSet<>();
		this.keepInv = new HashSet<>();
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
}
