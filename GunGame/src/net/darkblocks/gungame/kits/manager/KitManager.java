/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.gungame.kits.manager;

import lombok.Getter;
import net.darkblocks.gungame.kits.utils.Kits;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LartyHD on 17.02.2018  16:33.
 */
@Getter
public class KitManager
{
	private final Map<String, Integer> player;
	private final JavaPlugin javaPlugin;
	
	public KitManager(JavaPlugin javaPlugin)
	{
		this.player = new HashMap<>();
		this.javaPlugin = javaPlugin;
	}
	
	public void downgrade(Player player)
	{
		int level = this.player.get(player.getName()) / 2;
		if (level > 0)
		{
			this.player.put(player.getName(), level);
		}
		else
		{
			this.player.put(player.getName(), 1);
		}
		setKit(player);
	}
	
	public void update(Player player)
	{
		player.playSound(player.getLocation(), Sound.LEVEL_UP, 2, 1);
		int level = this.player.get(player.getName());
		if (level < Kits.values().length)
		{
			this.player.put(player.getName(), level + 1);
		}
		setKit(player);
	}
	
	private void setKit(Player player)
	{
		PlayerInventory playerInventory = player.getInventory();
		Kits kit = Kits.valueOf("KIT" + this.player.get(player.getName()));
		playerInventory.setHelmet(kit.getHelmet());
		playerInventory.setChestplate(kit.getChestplate());
		playerInventory.setLeggings(kit.getLeggins());
		playerInventory.setBoots(kit.getBoots());
		playerInventory.setItem(0, kit.getItem());
		Bukkit.getScheduler().scheduleSyncDelayedTask(this.javaPlugin, () -> player.setLevel(this.player.get(player.getName())), 3);
		player.updateInventory();
	}
}
