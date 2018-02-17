/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.team;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by LartyHD on 03.01.2018  11:43.
 */
@Getter
@Setter
@ToString
public class GameTeam
{
	private final Set<Player> players;
	private final Team team;
	private final String name;
	private final Color leatherColor;
	private final ChatColor chatColor;
	private Location location;
	private int size;
	
	GameTeam(String name, ChatColor chatcolor, int size, boolean colored)
	{
		this.players = new HashSet<>();
		this.name = name;
		this.chatColor = chatcolor;
		this.leatherColor = this.getLeatherColor(chatcolor);
		this.size = size;
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		if (scoreboard.getTeam(name) == null)
		{
			this.team = scoreboard.registerNewTeam(name);
		}
		else
		{
			this.team = scoreboard.getTeam(name);
		}
		this.team.setAllowFriendlyFire(false);
		this.team.setNameTagVisibility(NameTagVisibility.ALWAYS);
		this.team.setDisplayName(getChatColor() + getName());
		if (!colored)
		{
			this.team.setPrefix(getChatColor() + name + " §7| " + getChatColor());
		}
		else
		{
			this.team.setPrefix(getChatColor() + "");
		}
	}
	
	@SuppressWarnings("deprecation")
	public boolean add(Player player)
	{
		if (this.players.size() >= this.size)
		{
			return false;
		}
		else
		{
			this.players.add(player);
			this.team.addPlayer(player);
			return true;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void remove(Player player)
	{
		this.players.remove(player);
		this.team.removePlayer(player);
	}
	
	private Color getLeatherColor(ChatColor chatColor)
	{
		switch (chatColor.getChar())
		{
			case '0':
				return Color.fromRGB(0, 0, 0);
			case '1':
				return Color.fromRGB(0, 0, 170);
			case '2':
				return Color.fromRGB(0, 170, 0);
			case '3':
				return Color.fromRGB(0, 170, 170);
			case '4':
				return Color.fromRGB(170, 0, 0);
			case '5':
				return Color.fromRGB(170, 0, 170);
			case '6':
				return Color.fromRGB(255, 170, 0);
			case '7':
				return Color.fromRGB(170, 170, 170);
			case '8':
				return Color.fromRGB(85, 85, 85);
			case '9':
				return Color.fromRGB(85, 85, 255);
			case 'a':
				return Color.fromRGB(85, 255, 85);
			case 'b':
				return Color.fromRGB(85, 255, 255);
			case 'c':
				return Color.fromRGB(255, 85, 85);
			case 'd':
				return Color.fromRGB(255, 85, 255);
			case 'e':
				return Color.fromRGB(255, 255, 85);
			case 'f':
				return Color.fromRGB(255, 255, 255);
			default:
				return Color.WHITE;
		}
	}
}
