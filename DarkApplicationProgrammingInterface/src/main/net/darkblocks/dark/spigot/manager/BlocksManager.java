/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.dark.spigot.manager;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

/**
 * Created by LartyHD on 06.07.2017  04:35.
 */
@Getter
public class BlocksManager
{
	private final ArrayList<Location> locations;
	private final ArrayList<Material> allowedTypes;
	
	public BlocksManager(ArrayList<Material> allowedTypes)
	{
		this.locations = new ArrayList<>();
		this.allowedTypes = allowedTypes;
	}
	
	public boolean isBreakable(Block block)
	{
		return isBreakable(block.getLocation(), block.getType());
	}
	
	public boolean isBreakable(Location location, Material type)
	{
		if (this.allowedTypes != null)
		{
			for (Material types : this.allowedTypes)
			{
				if (type == types)
				{
					return true;
				}
			}
		}
		for (Location locations : this.locations)
		{
			if (location.getWorld().getName().equals(locations.getWorld().getName()) && location.getBlockX() == locations.getBlockX() && location.getBlockY() == locations.getBlockY() && location.getBlockZ() == locations.getBlockZ())
			{
				return true;
			}
		}
		return false;
	}
}
