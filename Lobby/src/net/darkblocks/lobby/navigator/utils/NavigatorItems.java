/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.lobby.navigator.utils;

import lombok.Getter;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static net.darkblocks.dark.universal.messages.Colors.SECONDARY;

/**
 * Created by LartyHD on 08.02.2018  06:49.
 */
@Getter
public enum NavigatorItems
{
	TEAMPVP(new ItemBuilder(Material.IRON_CHESTPLATE).setName(SECONDARY + "TeamPvP").build()),
	COOKIECLICKER(new ItemBuilder(Material.COOKIE).setName(SECONDARY + "CookieClicker").build()),
	FFA(new ItemBuilder(Material.IRON_SWORD).setName(SECONDARY + "FFA").hideItemFlags().build()),
	SPAWN(new ItemBuilder(Material.MAGMA_CREAM).setName(SECONDARY + "Spawn").build()),
	GUNGAME(new ItemBuilder(Material.WOOD_AXE).setName(SECONDARY + "GunGame").hideItemFlags().build()),
	BELOHNUNG(new ItemBuilder(Material.GOLD_INGOT).setName(SECONDARY + "Belohnung").build()),
	BEDWARS(new ItemBuilder(Material.BED).setName(SECONDARY + "BedWars").build()),
	CORES(new ItemBuilder(Material.BEACON).setName(SECONDARY + "Cores").build()),
	PLATZHALTER(new ItemBuilder(Material.IRON_FENCE).setName("§0").build());
	private final ItemStack itemStack;
	
	NavigatorItems(ItemStack itemStack)
	{
		this.itemStack = itemStack;
	}
}
