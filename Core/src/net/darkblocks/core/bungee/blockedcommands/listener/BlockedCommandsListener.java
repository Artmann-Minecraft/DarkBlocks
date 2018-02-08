package net.darkblocks.core.bungee.blockedcommands.listener;

import lombok.Getter;
import net.darkblocks.dark.universal.messages.Messages;
import net.md_5.bungee.BungeeCord;
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
public class BlockedCommandsListener implements Listener
{
	private final List<String> blocked;
	
	public BlockedCommandsListener(Plugin plugin, List<String> blocked)
	{
		this.blocked = blocked;
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
	}
	
	@EventHandler
	public void onChatEvent(ChatEvent event)
	{
		if (event.isCommand() && event.getSender() instanceof ProxiedPlayer)
		{
			String command = event.getMessage().split(" ")[0];
			for (String blocked : getBlocked())
			{
				String[] splits = command.toLowerCase().split(":");
				if (command.equalsIgnoreCase("/" + blocked) || (splits.length >= 2 && splits[1].equalsIgnoreCase(blocked)))
				{
					event.setCancelled(true);
					((ProxiedPlayer) event.getSender()).sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Der Command " + IMPORTANT + command + TEXT + " ist geblocked"));
				}
			}
		}
	}
}
