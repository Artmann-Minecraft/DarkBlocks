package net.darkblocks.core.bungee;

import net.darkblocks.core.bungee.autoban.ChatBan;
import net.darkblocks.core.bungee.commands.Commands;
import net.darkblocks.core.bungee.joinme.JoinMe;
import net.darkblocks.core.bungee.msg.PrivateMessage;
import net.darkblocks.core.bungee.otherversionblocker.OtherVersionBlocker;
import net.darkblocks.core.bungee.tablist.TabList;
import net.darkblocks.core.bungee.teamchat.TeamChat;
import net.darkblocks.core.bungee.wartungen.Wartungen;
import net.darkblocks.dark.java.config.PropertiesConfig;
import net.darkblocks.dark.java.mysql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

/**
 * Created by LartyHD on 09.01.2018  08:32.
 */
public class Core
{
	public Core(Plugin plugin)
	{
		@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
		PropertiesConfig properties = new PropertiesConfig(new File("databases"), "mysql.properties");
		MySQL mySQL = new MySQL((String) properties.get("Host"), (String) properties.get("Port"), (String) properties.get("Username"), (String) properties.get("Password"), (String) properties.get("Database"));
		new Wartungen(plugin, mySQL);
		new ChatBan(plugin, mySQL);
		new Commands(plugin);
		new PrivateMessage(plugin);
		new JoinMe(plugin);
		new OtherVersionBlocker(plugin);
		new TabList(plugin);
		new TeamChat(plugin, mySQL);
	}
}
