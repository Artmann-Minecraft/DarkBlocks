package net.darkblocks.core.bungee.teamchat.listener;

import lombok.Getter;
import net.darkblocks.core.bungee.teamchat.TeamChat;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 19.01.2018  23:33.
 */
@Getter
public class TeamChatListener implements Listener
{
	private final TeamChat teamChat;
	
	public TeamChatListener(Plugin plugin, TeamChat teamChat)
	{
		this.teamChat = teamChat;
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
	}
	
	@EventHandler
	public void onChatEvent(ChatEvent event)
	{
		String message = event.getMessage();
		Connection sender = event.getSender();
		if (message.startsWith("#") && !message.substring(1).replaceAll(" ", "").equalsIgnoreCase("") && sender instanceof ProxiedPlayer)
		{
			ProxiedPlayer player = (ProxiedPlayer) sender;
			if (getTeamChat().getPlayers().contains(player.getName()))
			{
				event.setCancelled(true);
				for (ProxiedPlayer players : BungeeCord.getInstance().getPlayers())
				{
					players.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + player.getName() + PRIMARY + EXTRA + " -> " + TEXT + message.substring(1)));
				}
			}
		}
	}
	
	@EventHandler
	public void onLoginEvent(PostLoginEvent event)
	{
		if (event.getPlayer().hasPermission(CommandUtils.getPermission(getClass())))
		{
			getTeamChat().getPlayers().add(event.getPlayer().getName());
		}
	}
}
