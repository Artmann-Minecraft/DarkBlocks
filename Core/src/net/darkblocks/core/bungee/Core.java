package net.darkblocks.core.bungee;

import net.darkblocks.core.bungee.autoban.ChatBan;
import net.darkblocks.core.bungee.automessage.AutoMessage;
import net.darkblocks.core.bungee.commands.Commands;
import net.darkblocks.core.bungee.joinme.JoinMe;
import net.darkblocks.core.bungee.motd.Motd;
import net.darkblocks.core.bungee.msg.PrivateMessage;
import net.darkblocks.core.bungee.otherversionblocker.OtherVersionBlocker;
import net.darkblocks.core.bungee.tablist.TabList;
import net.darkblocks.core.bungee.teamchat.TeamChat;
import net.darkblocks.core.bungee.wartungen.Wartungen;
import net.darkblocks.dark.java.config.PropertiesConfig;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.universal.messages.Messages;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static net.darkblocks.dark.universal.messages.Colors.EXTRA;
import static net.darkblocks.dark.universal.messages.Colors.PRIMARY;

/**
 * Created by LartyHD on 09.01.2018  08:32.
 */
public class Core extends Plugin
{
	@Override
	public void onEnable()
	{
		Map<String, String> messages = new HashMap<>();
		messages.put("dark.servername", "" + PRIMARY + EXTRA + "DarkBlocks§f" + EXTRA + "." + PRIMARY + EXTRA + "Net");
		messages.put("dark.prefix", "§f" + EXTRA + "[" + PRIMARY + EXTRA + "DarkBlocks§f" + EXTRA + "] §r");
		new Messages(messages);
		PropertiesConfig properties = new PropertiesConfig(new File("databases"), "mysql.properties");
		MySQL mySQL = new MySQL((String) properties.get("Host"), (String) properties.get("Port"), (String) properties.get("Username"), (String) properties.get("Password"), (String) properties.get("Database"));
		new Commands(this);
		new Wartungen(this, mySQL);
		new ChatBan(this, mySQL);
		new Motd(this, mySQL);
		new PrivateMessage(this);
		new JoinMe(this);
		new OtherVersionBlocker(this);
		new TabList(this);
		new TeamChat(this, mySQL);
		new AutoMessage(this);
	}
}
