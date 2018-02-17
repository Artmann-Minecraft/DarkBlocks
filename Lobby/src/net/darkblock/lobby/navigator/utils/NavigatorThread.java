/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblock.lobby.navigator.utils;

import net.darkblocks.dark.spigot.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCrafting;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static net.darkblocks.dark.universal.messages.Colors.SECONDARY;

/**
 * Created by LartyHD on 16.10.2017  01:30.
 */
public class NavigatorThread extends Thread
{
	private final Player player;
	
	public NavigatorThread(Player player)
	{
		this.player = player;
	}
	
	@Override
	public void run()
	{
		try
		{
			Inventory inventory = Bukkit.createInventory(null, 45, SECONDARY + "Navigator");
			this.player.openInventory(inventory);
			for (int i = 0; i < 45; i++)
			{
				setGlas(inventory, i, (short) 7);
				playSound();
				Thread.sleep(25);
			}
			//WEIßES GLASS OBEN
			setGlas(inventory, 0, (short) 0);
			playSound();
			Thread.sleep(25);
			for (int i = 3; i < 6; i++)
			{
				setGlas(inventory, i, (short) 0);
				playSound();
				Thread.sleep(25);
			}
			setGlas(inventory, 8, (short) 0);
			playSound();
			Thread.sleep(25);
			//WEIßES GLASS OBEN
			//WEIßES GLASS UNTEN
			setGlas(inventory, 36, (short) 0);
			playSound();
			Thread.sleep(25);
			for (int i = 39; i < 42; i++)
			{
				setGlas(inventory, i, (short) 0);
				playSound();
				Thread.sleep(25);
			}
			setGlas(inventory, 44, (short) 0);
			playSound();
			Thread.sleep(25);
			//WEIßES GLASS UNTEN
			//SCHWERZES GLASS 1
			setGlas(inventory, 1, (short) 15);
			playSound();
			Thread.sleep(25);
			setGlas(inventory, 7, (short) 15);
			playSound();
			Thread.sleep(25);
			//SCHWERZES GLASS 1
			//SCHWERZES GLASS 2
			setGlas(inventory, 9, (short) 15);
			playSound();
			Thread.sleep(25);
			setGlas(inventory, 17, (short) 15);
			playSound();
			Thread.sleep(25);
			//SCHWERZES GLASS 2
			//SCHWERZES GLASS 3
			setGlas(inventory, 18, (short) 15);
			playSound();
			Thread.sleep(25);
			setGlas(inventory, 26, (short) 15);
			playSound();
			Thread.sleep(25);
			//SCHWERZES GLASS 3
			//SCHWERZES GLASS 4
			setGlas(inventory, 27, (short) 15);
			playSound();
			Thread.sleep(25);
			setGlas(inventory, 35, (short) 15);
			playSound();
			Thread.sleep(25);
			//SCHWERZES GLASS 4
			//SCHWERZES GLASS 5
			setGlas(inventory, 37, (short) 15);
			playSound();
			Thread.sleep(25);
			setGlas(inventory, 43, (short) 15);
			playSound();
			Thread.sleep(25);
			//SCHWERZES GLASS 5
			//ADD TELEPORTS
			//1
			remove(inventory, 4);
			inventory.setItem(4, NavigatorItems.CORES.getItemStack());
			playSound();
			Thread.sleep(200);
			//1
			//2
			remove(inventory, 22);
			inventory.setItem(22, NavigatorItems.PLATZHALTER.getItemStack());
			playSound();
			Thread.sleep(200);
			//2
			//3
			remove(inventory, 19);
			inventory.setItem(19, NavigatorItems.FFA.getItemStack());
			playSound();
			Thread.sleep(200);
			//3
			//4
			remove(inventory, 25);
			inventory.setItem(25, NavigatorItems.GUNGAME.getItemStack());
			playSound();
			Thread.sleep(200);
			//4
			//5
			remove(inventory, 22);
			inventory.setItem(22, NavigatorItems.SPAWN.getItemStack());
			playSound();
			Thread.sleep(200);
			//5
			//6
			remove(inventory, 40);
			inventory.setItem(40, NavigatorItems.PLATZHALTER.getItemStack());
			playSound();
			Thread.sleep(200);
			//6
			//7
			remove(inventory, 38);
			inventory.setItem(38, NavigatorItems.COOKIECLICKER.getItemStack());
			playSound();
			Thread.sleep(200);
			//7
			//8
			remove(inventory, 42);
			inventory.setItem(42, NavigatorItems.BELOHNUNG.getItemStack());
			playSound();
			Thread.sleep(200);
			//8
			//9
			remove(inventory, 40);
			inventory.setItem(40, NavigatorItems.TEAMPVP.getItemStack());
			playSound();
			Thread.sleep(200);
			//9
			//ADD TELEPORTS
		} catch (InterruptedException ignored)
		{
		}
	}
	
	private void remove(Inventory inventory, int slot) throws InterruptedException
	{
		inventory.setItem(slot, new ItemStack(Material.AIR));
		Thread.sleep(25);
	}
	
	private void setGlas(Inventory inventory, int slot, short durability)
	{
		inventory.setItem(slot, new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").setDurability(durability).build());
	}
	
	private void playSound()
	{
		if (this.player.getOpenInventory().getTopInventory() instanceof CraftInventoryCrafting)
		{
			this.interrupt();
			return;
		}
		this.player.updateInventory();
		this.player.playSound(this.player.getLocation(), Sound.BURP, 1, 1);
	}
}
