/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.utils;

import lombok.Getter;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static net.darkblocks.dark.universal.messages.Colors.SECONDARY;

/**
 * Created by LartyHD on 17.02.2018  16:20.
 */
@Getter
public enum Items
{
	LEAVE(new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setOwnerFromURL("http://textures.minecraft.net/texture/1b6f1a25b6bc199946472aedb370522584ff6f4e83221e5946bd2e41b5ca13b", "MHF_ArrowRight").setName(SECONDARY + "Zurück zur Lobby").build()),
	TEAMS(new ItemBuilder(Material.ENDER_CHEST).setName(SECONDARY + "Teams").build());
	private final ItemStack itemStack;
	
	Items(ItemStack itemStack)
	{
		this.itemStack = itemStack;
	}
}
