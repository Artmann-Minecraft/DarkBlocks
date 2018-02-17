/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.dark.spigot.builder;

import lombok.ToString;
import net.darkblocks.dark.java.builder.Builder;
import net.darkblocks.dark.spigot.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@ToString
public class InventoryBuilder implements Builder<Inventory>
{
	private final Inventory inventory;
	
	public InventoryBuilder(InventoryHolder holder, InventoryType type)
	{
		this.inventory = Bukkit.createInventory(holder, type);
	}
	
	public InventoryBuilder(InventoryHolder holder, InventoryType type, String name)
	{
		this.inventory = Bukkit.createInventory(holder, type, name);
	}
	
	public InventoryBuilder(InventoryHolder holder, int slots)
	{
		this.inventory = Bukkit.createInventory(holder, slots);
	}
	
	public InventoryBuilder(InventoryHolder holder, int slots, String name)
	{
		this.inventory = Bukkit.createInventory(holder, slots, name);
	}
	
	public InventoryBuilder setItem(int slot, ItemStack item)
	{
		this.inventory.setItem(slot, item);
		return this;
	}
	
	public InventoryBuilder setDesign(List<ItemStack> items)
	{
		InventoryUtils.setDesign(this.inventory, items);
		return this;
	}
	
	public InventoryBuilder addItem(ItemStack item)
	{
		this.inventory.addItem(item);
		return this;
	}
	
	public InventoryBuilder fillWith(ItemStack item)
	{
		for (int i = 0; i < this.inventory.getSize(); i++)
		{
			if (this.inventory.getItem(i) != null)
			{
				continue;
			}
			this.inventory.setItem(i, item);
		}
		return this;
	}
	
	@Override
	public Inventory build()
	{
		return this.inventory;
	}
}
