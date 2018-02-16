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

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 09.01.2018  08:35.
 */
@Getter
public class ChatBanListener implements Listener
{
	private final List<String> matches;
	
	public ChatBanListener(Plugin plugin, List<String> block)
	{
		this.matches = block;
//		for (String blocked : block)
//		{
//			char[] chars = blocked.toCharArray();
//			StringBuilder match = new StringBuilder();
//			for (char c : chars)
//			{
//				match.append("+[").append(Character.toUpperCase(c)).append(Character.toLowerCase(c)).append("]");
//			}
//			this.matches.add(match.substring(1));
//		}
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
	}
	
	@EventHandler
	public void onChatEvent(ChatEvent event)
	{
		String s = event.getMessage().toLowerCase().replaceAll(" ", "");
		for (String match : getMatches())
		{
			if (s.contains(match))
			{
				ProxiedPlayer sender = (ProxiedPlayer) event.getSender();
				event.setCancelled(true);
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Du darfst " + IMPORTANT + match + TEXT + " nicht schreiben"));
				ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "ban " + sender.getName() + " 2");
			}
		}
	}
}
