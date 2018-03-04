/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.utils;

import net.darkblocks.dark.spigot.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by LartyHD on 04.01.2018  18:46.
 */
public class InventoryUtils
{
	public static void sortChestInventory(Inventory inventory, List<ItemStack> itemStacks, int addSlots)
	{
		switch (itemStacks.size())
		{
			case 1:
				inventory.setItem(4 + addSlots, itemStacks.get(0));
				break;
			case 2:
				inventory.setItem(2 + addSlots, itemStacks.get(0));
				inventory.setItem(6 + addSlots, itemStacks.get(1));
				break;
			case 3:
				inventory.setItem(1 + addSlots, itemStacks.get(0));
				inventory.setItem(4 + addSlots, itemStacks.get(1));
				inventory.setItem(7 + addSlots, itemStacks.get(2));
				break;
			case 4:
				inventory.setItem(1 + addSlots, itemStacks.get(0));
				inventory.setItem(3 + addSlots, itemStacks.get(1));
				inventory.setItem(5 + addSlots, itemStacks.get(2));
				inventory.setItem(7 + addSlots, itemStacks.get(3));
				break;
			case 5:
				inventory.setItem(addSlots, itemStacks.get(0));
				inventory.setItem(2 + addSlots, itemStacks.get(1));
				inventory.setItem(4 + addSlots, itemStacks.get(2));
				inventory.setItem(6 + addSlots, itemStacks.get(3));
				inventory.setItem(8 + addSlots, itemStacks.get(4));
				break;
			case 6:
				inventory.setItem(1 + addSlots, itemStacks.get(0));
				inventory.setItem(2 + addSlots, itemStacks.get(1));
				inventory.setItem(3 + addSlots, itemStacks.get(2));
				inventory.setItem(5 + addSlots, itemStacks.get(3));
				inventory.setItem(6 + addSlots, itemStacks.get(4));
				inventory.setItem(7 + addSlots, itemStacks.get(5));
				break;
			case 7:
				inventory.setItem(addSlots, itemStacks.get(0));
				inventory.setItem(1 + addSlots, itemStacks.get(1));
				inventory.setItem(2 + addSlots, itemStacks.get(2));
				inventory.setItem(4 + addSlots, itemStacks.get(3));
				inventory.setItem(6 + addSlots, itemStacks.get(4));
				inventory.setItem(7 + addSlots, itemStacks.get(5));
				inventory.setItem(8 + addSlots, itemStacks.get(6));
				break;
			case 8:
				inventory.setItem(addSlots, itemStacks.get(0));
				inventory.setItem(1 + addSlots, itemStacks.get(1));
				inventory.setItem(2 + addSlots, itemStacks.get(2));
				inventory.setItem(3 + addSlots, itemStacks.get(3));
				inventory.setItem(5 + addSlots, itemStacks.get(4));
				inventory.setItem(6 + addSlots, itemStacks.get(5));
				inventory.setItem(7 + addSlots, itemStacks.get(6));
				inventory.setItem(8 + addSlots, itemStacks.get(7));
				break;
			case 9:
				for (int i = 0; i < itemStacks.size(); i++)
				{
					inventory.setItem(i + addSlots, itemStacks.get(i));
				}
				break;
		}
	}
	
	public static int getInventorySize(int size)
	{
		return size <= 9 ? 9 : size <= 18 ? 18 : size <= 27 ? 27 : size <= 36 ? 36 : size <= 45 ? 45 : 54;
	}
	
	private static void fillGlass(Inventory inventory, short durability)
	{
		for (int i = 0; i < inventory.getSize(); i++)
		{
			if (inventory.getItem(i) == null)
			{
				setGlas(inventory, i, durability);
			}
		}
	}
	
	/**
	 * NUR FÜR 45 SLOT INVENTARE
	 */
	public static void setDesign(Inventory inventory, List<ItemStack> items)
	{
		switch (inventory.getType())
		{
			case HOPPER:
				switch (items.size())
				{
					case 0:
						setGlas(inventory, 0, (short) 15);
						setGlas(inventory, 1, (short) 0);
						setGlas(inventory, 2, (short) 0);
						setGlas(inventory, 3, (short) 0);
						setGlas(inventory, 4, (short) 15);
						break;
					case 1:
						setGlas(inventory, 0, (short) 15);
						setGlas(inventory, 1, (short) 0);
						inventory.setItem(2, items.get(0));
						setGlas(inventory, 3, (short) 0);
						setGlas(inventory, 4, (short) 15);
						break;
					case 2:
						setGlas(inventory, 0, (short) 15);
						inventory.setItem(1, items.get(0));
						setGlas(inventory, 2, (short) 0);
						inventory.setItem(3, items.get(1));
						setGlas(inventory, 4, (short) 15);
						break;
					case 3:
						setGlas(inventory, 0, (short) 15);
						for (int i = 1; i < items.size() + 1; i++)
						{
							inventory.setItem(i, items.get(i));
						}
						setGlas(inventory, 4, (short) 15);
						break;
					case 4:
						inventory.setItem(0, items.get(0));
						inventory.setItem(1, items.get(1));
						setGlas(inventory, 2, (short) 0);
						inventory.setItem(3, items.get(2));
						inventory.setItem(4, items.get(3));
						break;
					case 5:
						for (int i = 0; i < items.size(); i++)
						{
							inventory.setItem(i, items.get(i));
						}
						break;
				}
				break;
			case DISPENSER:
				switch (items.size())
				{
					case 0:
						setGlas(inventory, 0, (short) 0);
						setGlas(inventory, 1, (short) 7);
						setGlas(inventory, 2, (short) 0);
						setGlas(inventory, 3, (short) 7);
						setGlas(inventory, 4, (short) 7);
						setGlas(inventory, 5, (short) 7);
						setGlas(inventory, 6, (short) 0);
						setGlas(inventory, 7, (short) 7);
						setGlas(inventory, 8, (short) 0);
						break;
					case 1:
						setGlas(inventory, 0, (short) 0);
						setGlas(inventory, 1, (short) 7);
						setGlas(inventory, 2, (short) 0);
						setGlas(inventory, 3, (short) 7);
						inventory.setItem(0, items.get(0));
						setGlas(inventory, 5, (short) 7);
						setGlas(inventory, 6, (short) 0);
						setGlas(inventory, 7, (short) 7);
						setGlas(inventory, 8, (short) 0);
						break;
					case 2:
						setGlas(inventory, 0, (short) 0);
						setGlas(inventory, 1, (short) 7);
						setGlas(inventory, 2, (short) 0);
						inventory.setItem(3, items.get(0));
						setGlas(inventory, 4, (short) 7);
						inventory.setItem(5, items.get(1));
						setGlas(inventory, 6, (short) 0);
						setGlas(inventory, 7, (short) 7);
						setGlas(inventory, 8, (short) 0);
						break;
					case 3:
						setGlas(inventory, 0, (short) 0);
						setGlas(inventory, 1, (short) 7);
						setGlas(inventory, 2, (short) 0);
						inventory.setItem(3, items.get(0));
						inventory.setItem(4, items.get(1));
						inventory.setItem(5, items.get(2));
						setGlas(inventory, 6, (short) 0);
						setGlas(inventory, 7, (short) 7);
						setGlas(inventory, 8, (short) 0);
						break;
					case 4:
						setGlas(inventory, 0, (short) 0);
						inventory.setItem(1, items.get(0));
						setGlas(inventory, 2, (short) 0);
						inventory.setItem(3, items.get(1));
						setGlas(inventory, 4, (short) 7);
						inventory.setItem(5, items.get(2));
						setGlas(inventory, 6, (short) 0);
						inventory.setItem(7, items.get(3));
						setGlas(inventory, 8, (short) 0);
						break;
					case 5:
						setGlas(inventory, 0, (short) 0);
						inventory.setItem(1, items.get(0));
						setGlas(inventory, 2, (short) 0);
						inventory.setItem(3, items.get(1));
						inventory.setItem(4, items.get(2));
						inventory.setItem(5, items.get(3));
						setGlas(inventory, 6, (short) 0);
						inventory.setItem(7, items.get(4));
						setGlas(inventory, 8, (short) 0);
						break;
					case 6:
						for (int i = 0; i < 3; i++)
						{
							inventory.setItem(i, items.get(i));
						}
						setGlas(inventory, 3, (short) 7);
						setGlas(inventory, 4, (short) 0);
						setGlas(inventory, 5, (short) 7);
						for (int i = 5; i < items.size(); i++)
						{
							inventory.setItem(i, items.get(i));
						}
						break;
					case 7:
						for (int i = 0; i < 3; i++)
						{
							inventory.setItem(i, items.get(i));
						}
						setGlas(inventory, 3, (short) 7);
						inventory.setItem(4, items.get(4));
						setGlas(inventory, 5, (short) 7);
						for (int i = 5; i < items.size(); i++)
						{
							inventory.setItem(i, items.get(i));
						}
						break;
					case 8:
						for (int i = 0; i < 4; i++)
						{
							inventory.setItem(i, items.get(i));
						}
						setGlas(inventory, 4, (short) 0);
						for (int i = 4; i < items.size(); i++)
						{
							inventory.setItem(i, items.get(i));
						}
						break;
					case 9:
						for (int i = 0; i < items.size(); i++)
						{
							inventory.setItem(i, items.get(i));
						}
						break;
				}
				break;
			case CHEST:
				switch (inventory.getSize())
				{
					case 9:
						switch (items.size())
						{
							case 0:
								setGlas(inventory, 0, (short) 0);
								setGlas(inventory, 1, (short) 15);
								setGlas(inventory, 3, (short) 0);
								setGlas(inventory, 4, (short) 0);
								setGlas(inventory, 5, (short) 0);
								setGlas(inventory, 7, (short) 15);
								setGlas(inventory, 8, (short) 0);
								break;
						}
						break;
					case 18:
						switch (items.size())
						{
							case 0:
								//ZEILE 1
								setGlas(inventory, 0, (short) 0);
								setGlas(inventory, 1, (short) 15);
								setGlas(inventory, 3, (short) 0);
								setGlas(inventory, 4, (short) 0);
								setGlas(inventory, 5, (short) 0);
								setGlas(inventory, 7, (short) 15);
								setGlas(inventory, 8, (short) 0);
								//ZEILE 1
								//ZEILE 2
								setGlas(inventory, 9, (short) 0);
								setGlas(inventory, 10, (short) 15);
								setGlas(inventory, 12, (short) 0);
								setGlas(inventory, 13, (short) 0);
								setGlas(inventory, 14, (short) 0);
								setGlas(inventory, 16, (short) 15);
								setGlas(inventory, 17, (short) 0);
								//ZEILE 2
								break;
						}
						break;
					case 27:
						switch (items.size())
						{
							case 0:
								//ZEILE 1
								setGlas(inventory, 0, (short) 0);
								setGlas(inventory, 1, (short) 15);
								setGlas(inventory, 3, (short) 0);
								setGlas(inventory, 4, (short) 0);
								setGlas(inventory, 5, (short) 0);
								setGlas(inventory, 7, (short) 15);
								setGlas(inventory, 8, (short) 0);
								//ZEILE 1
								//ZEILE 2
								setGlas(inventory, 9, (short) 15);
								setGlas(inventory, 17, (short) 15);
								//ZEILE 2
								//ZEILE 3
								setGlas(inventory, 18, (short) 0);
								setGlas(inventory, 19, (short) 15);
								setGlas(inventory, 21, (short) 0);
								setGlas(inventory, 22, (short) 0);
								setGlas(inventory, 23, (short) 0);
								setGlas(inventory, 25, (short) 15);
								setGlas(inventory, 26, (short) 0);
								//ZEILE 3
								break;
						}
						break;
					case 36:
						switch (items.size())
						{
							case 0:
								//ZEILE 1
								setGlas(inventory, 0, (short) 0);
								setGlas(inventory, 1, (short) 15);
								setGlas(inventory, 3, (short) 0);
								setGlas(inventory, 4, (short) 0);
								setGlas(inventory, 5, (short) 0);
								setGlas(inventory, 7, (short) 15);
								setGlas(inventory, 8, (short) 0);
								//ZEILE 1
								//ZEILE 2
								setGlas(inventory, 9, (short) 15);
								setGlas(inventory, 17, (short) 15);
								//ZEILE 2
								//ZEILE 3
								setGlas(inventory, 18, (short) 15);
								setGlas(inventory, 26, (short) 15);
								//ZEILE 3
								//ZEILE 4
								setGlas(inventory, 27, (short) 0);
								setGlas(inventory, 28, (short) 15);
								setGlas(inventory, 30, (short) 0);
								setGlas(inventory, 31, (short) 0);
								setGlas(inventory, 32, (short) 0);
								setGlas(inventory, 34, (short) 15);
								setGlas(inventory, 35, (short) 0);
								//ZEILE 4
								break;
						}
						break;
					case 45:
						switch (items.size())
						{
							case 0:
								//ZEILE 1
								setGlas(inventory, 0, (short) 0);
								setGlas(inventory, 1, (short) 15);
								setGlas(inventory, 3, (short) 0);
								setGlas(inventory, 4, (short) 0);
								setGlas(inventory, 5, (short) 0);
								setGlas(inventory, 7, (short) 15);
								setGlas(inventory, 8, (short) 0);
								//ZEILE 1
								//ZEILE 2
								setGlas(inventory, 9, (short) 15);
								setGlas(inventory, 17, (short) 15);
								//ZEILE 2
								//ZEILE 3
								setGlas(inventory, 18, (short) 15);
								setGlas(inventory, 26, (short) 15);
								//ZEILE 3
								//ZEILE 4
								setGlas(inventory, 27, (short) 15);
								setGlas(inventory, 35, (short) 15);
								//ZEILE 4
								//ZEILE 5
								setGlas(inventory, 36, (short) 0);
								setGlas(inventory, 37, (short) 15);
								setGlas(inventory, 39, (short) 0);
								setGlas(inventory, 40, (short) 0);
								setGlas(inventory, 41, (short) 0);
								setGlas(inventory, 43, (short) 15);
								setGlas(inventory, 44, (short) 0);
								//ZEILE 5
								break;
						}
						break;
					case 54:
						switch (items.size())
						{
							case 0:
								//ZEILE 1
								setGlas(inventory, 0, (short) 0);
								setGlas(inventory, 1, (short) 15);
								setGlas(inventory, 3, (short) 0);
								setGlas(inventory, 4, (short) 0);
								setGlas(inventory, 5, (short) 0);
								setGlas(inventory, 7, (short) 15);
								setGlas(inventory, 8, (short) 0);
								//ZEILE 1
								//ZEILE 2
								setGlas(inventory, 9, (short) 15);
								setGlas(inventory, 17, (short) 15);
								//ZEILE 2
								//ZEILE 3
								setGlas(inventory, 18, (short) 15);
								setGlas(inventory, 26, (short) 15);
								//ZEILE 3
								//ZEILE 4
								setGlas(inventory, 27, (short) 15);
								setGlas(inventory, 35, (short) 15);
								//ZEILE 4
								//ZEILE 5
								setGlas(inventory, 36, (short) 15);
								setGlas(inventory, 44, (short) 15);
								//ZEILE 5
								//ZEILE 6
								setGlas(inventory, 45, (short) 0);
								setGlas(inventory, 46, (short) 15);
								setGlas(inventory, 48, (short) 0);
								setGlas(inventory, 49, (short) 0);
								setGlas(inventory, 50, (short) 0);
								setGlas(inventory, 52, (short) 15);
								setGlas(inventory, 53, (short) 0);
								//ZEILE 6
								break;
						}
						break;
				}
				break;
			case PLAYER:
				switch (items.size())
				{
					case 0:
						int i = 9;
						//ZEILE 1
						setGlas(inventory, i, (short) 0);
						setGlas(inventory, 1 + i, (short) 15);
						setGlas(inventory, 3 + i, (short) 0);
						setGlas(inventory, 4 + i, (short) 0);
						setGlas(inventory, 5 + i, (short) 0);
						setGlas(inventory, 7 + i, (short) 15);
						setGlas(inventory, 8 + i, (short) 0);
						//ZEILE 1
						//ZEILE 2
						setGlas(inventory, 9 + i, (short) 15);
						setGlas(inventory, 17 + i, (short) 15);
						//ZEILE 2
						//ZEILE 3
						setGlas(inventory, 18 + i, (short) 0);
						setGlas(inventory, 19 + i, (short) 15);
						setGlas(inventory, 21 + i, (short) 0);
						setGlas(inventory, 22 + i, (short) 0);
						setGlas(inventory, 23 + i, (short) 0);
						setGlas(inventory, 25 + i, (short) 15);
						setGlas(inventory, 26 + i, (short) 0);
						//ZEILE 3
						break;
				}
				//FILL GRAY GALSS
				for (int i = 9; i < inventory.getSize(); i++)
				{
					if (inventory.getItem(i) == null)
					{
						setGlas(inventory, i, (short) 7);
					}
				}
				//FILL GRAY GALSS
				return;
		}
		//FILL GRAY GALSS
		fillGlass(inventory, (short) 7);
		//FILL GRAY GALSS
	}
	
	private static void setGlas(Inventory inventory, int slot, short durability)
	{
		inventory.setItem(slot, new ItemBuilder(Material.STAINED_GLASS_PANE).setName("§f").setDurability(durability).build());
	}
	
	public static int hasItems(Player p, Material material)
	{
		int j = 0;
		for (ItemStack item : p.getInventory().getContents())
		{
			if (item != null && item.getType() == material)
			{
				j += item.getAmount();
			}
		}
		return j;
	}
	
	public static void removeItems(Player p, Material material, int cost)
	{
		for (ItemStack item : p.getInventory().getContents())
		{
			if (item != null && item.getType() == material)
			{
				if (item.getAmount() >= cost)
				{
					if (item.getAmount() - cost < 1)
					{
						p.getInventory().remove(item);
					}
					else
					{
						item.setAmount(item.getAmount() - cost);
					}
					break;
				}
				if (item.getAmount() < cost)
				{
					cost -= item.getAmount();
					p.getInventory().remove(item);
				}
			}
		}
		p.updateInventory();
	}
}
