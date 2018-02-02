package net.darkblocks.core.bungee.teamchat;

import lombok.Getter;
import net.craftplugin.craftpluginapi.java.mysql.MySQL;
import net.darkblocks.core.bungee.teamchat.listener.TeamChatListener;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by LartyHD on 19.01.2018  23:13.
 */
@Getter
public class TeamChat
{
	private final MySQL mySQL;
	private final Set<String> players;
	
	public TeamChat(Plugin plugin, MySQL mySQL)
	{
		this.mySQL = mySQL;
		this.players = new HashSet<>();
		new TeamChatListener(plugin, this);
	}
}
