/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblock.lobby.extras.belohnung.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
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
	private String command;
	private ItemStack displayItem;
	
	public CaseOpeningItem(String name, ItemStack displayItem)
	{
		this.name = name;
		this.displayItem = displayItem;
	}
	
	public abstract void execute(Player player);
	
	public void executeCommand()
	{
		if (this.command == null)
		{
			return;
		}
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command);
	}
}
