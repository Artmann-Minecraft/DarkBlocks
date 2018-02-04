package net.darkblocks.core.bungee.autoban.listener;

import lombok.Getter;
import net.darkblocks.dark.universal.messages.Messages;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 09.01.2018  08:35.
 */
@Getter
public class ChatBanListener implements Listener
{
	private final List<String> block;
	
	public ChatBanListener(Plugin plugin, List<String> block)
	{
		this.block = block;
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
	}
	
	@EventHandler
	public void onChatEvent(ChatEvent event)
	{
		if (!event.isCommand() && event.getSender() instanceof ProxiedPlayer)
		{
			ProxiedPlayer sender = (ProxiedPlayer) event.getSender();
			for (String blocked : getBlock())
			{
				char[] chars = blocked.toCharArray();
				StringBuilder match = new StringBuilder();
				for (char c : chars)
				{
					match.append(" + [").append(Character.toUpperCase(c)).append(Character.toLowerCase(c)).append("]");
				}
				if (event.getMessage().replaceAll(" ", "").matches(match.substring(3)))
				{
					event.setCancelled(true);
					sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Du darfst du nicht schreiben"));
					ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "ban " + sender.getName() + " 2");
				}
			}
		}
	}
}
