package net.darkblocks.dark.spigot.team.utils;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;

/**
 * Created by LartyHD on 11.02.2018  19:26.
 */
@AllArgsConstructor
public enum Teams
{
	BLUE("Blau", ChatColor.DARK_BLUE),
	RED("Rot", ChatColor.RED),
	GREEN("Grün", ChatColor.DARK_GREEN),
	YELLOW("Gelb", ChatColor.YELLOW),
	BLACK("Schwarz", ChatColor.BLACK),
	WHITE("Weiß", ChatColor.WHITE),
	ORANGE("Orange", ChatColor.GOLD),
	AQUA("Türkis", ChatColor.AQUA),
	PURPLE("Violett", ChatColor.DARK_PURPLE),
	LIGHT_BLUE("Hellblau", ChatColor.BLUE),
	LIGHT_GREEN("Hellgrün", ChatColor.GREEN),
	LIGHT_GRAY("Hellgrau", ChatColor.GRAY),
	GRAY("Grau", ChatColor.DARK_GRAY),
	PINK("ROSA", ChatColor.LIGHT_PURPLE);
	private String name;
	private ChatColor color;
	
	public String getName()
	{
		return this.name;
	}
	
	public ChatColor getColor()
	{
		return this.color;
	}
}