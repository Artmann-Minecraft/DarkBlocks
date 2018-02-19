/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblock.lobby.extras.belohnung.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by LartyHD on 16.02.2018  13:49.
 */
@Data
@AllArgsConstructor
public abstract class CaseOpeningItem
{
	private String name;
	private ItemStack displayItem;
	
	public abstract void execute(Player player);
}
