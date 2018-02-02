package net.darkblocks.cores.utils;

import net.craftplugin.craftpluginapi.spigot.team.GameTeam;
import org.bukkit.Location;

/**
 * Created by LartyHD on 04.01.2018  13:58.
 */
public class Core
{
	private String name;
	private Location location;
	private GameTeam gameTeam;
	
	public Core(String name, Location location, GameTeam gameTeam)
	{
		this.name = name;
		this.location = location;
		this.gameTeam = gameTeam;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Location getLocation()
	{
		return this.location;
	}
	
	public void setLocation(Location location)
	{
		this.location = location;
	}
	
	public GameTeam getGameTeam()
	{
		return this.gameTeam;
	}
	
	public void setGameTeam(GameTeam gameTeam)
	{
		this.gameTeam = gameTeam;
	}
}
