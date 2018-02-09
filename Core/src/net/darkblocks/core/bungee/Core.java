package net.darkblocks.core.bungee;

import net.darkblocks.core.bungee.autoban.ChatBan;
import net.darkblocks.core.bungee.automessage.AutoMessage;
import net.darkblocks.core.bungee.blockedcommands.BlockedCommands;
import net.darkblocks.core.bungee.coins.Coins;
import net.darkblocks.core.bungee.commands.Commands;
import net.darkblocks.core.bungee.joinme.JoinMe;
import net.darkblocks.core.bungee.motd.Motd;
import net.darkblocks.core.bungee.msg.PrivateMessage;
import net.darkblocks.core.bungee.onlinetime.OnlineTime;
import net.darkblocks.core.bungee.otherversionblocker.OtherVersionBlocker;
import net.darkblocks.core.bungee.permissions.Permissions;
import net.darkblocks.core.bungee.tablist.TabList;
import net.darkblocks.core.bungee.teamchat.TeamChat;
import net.darkblocks.core.bungee.wartungen.Wartungen;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.java.utils.ValueType;
import net.darkblocks.dark.universal.messages.Messages;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 09.01.2018  08:32.
 */
public class Core extends Plugin
{
	private OnlineTime onlineTime;
	
	@Override
	public void onEnable()
	{
		Map<String, String> messages = new HashMap<>();
		messages.put("dark.servername", "" + PRIMARY + EXTRA + "DarkBlocks§f" + EXTRA + "." + PRIMARY + EXTRA + "Net");
		messages.put("dark.prefix", "§f" + EXTRA + "[" + PRIMARY + EXTRA + "DarkBlocks§f" + EXTRA + "] §r");
		new Messages(messages);
		MySQL mySQL = new MySQL();
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
		new Coins(this, new CoinsAPI("Coins", ValueType.INTEGER, mySQL));
		new BlockedCommands(this, mySQL);
		new Permissions(this, mySQL);
		this.onlineTime = new OnlineTime(this, mySQL);
	}
	
	@Override
	public void onDisable()
	{
		for (ProxiedPlayer players : BungeeCord.getInstance().getPlayers())
		{
			players.disconnect(new TextComponent(TEXT + "Proxy Restart"));
		}
		this.onlineTime.disable();
	}
}
