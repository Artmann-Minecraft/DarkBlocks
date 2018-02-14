package net.darkblocks.core.bungee.nospam.listener;

import lombok.Getter;
import net.darkblocks.dark.universal.messages.Messages;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 13.02.2018  15:00.
 */
@Getter
public class NoSpamListener implements Listener
{
	private final Map<String, String> lastMessage;
	private final Set<String> delay;
	
	public NoSpamListener(Plugin plugin)
	{
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
		this.lastMessage = new HashMap<>();
		this.delay = new HashSet<>();
	}
	
	@EventHandler
	public void onChatEvent(ChatEvent event)
	{
		if (!event.isCancelled() && !event.isCommand() && event.getSender() instanceof ProxiedPlayer)
		{
			ProxiedPlayer player = (ProxiedPlayer) event.getSender();
			if (getDelay().contains(player.getName()))
			{
				event.setCancelled(true);
				player.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Bitte schreibe nicht so schnell"));
			}
			else if (getLastMessage().get(player.getName()) != null && getLastMessage().get(player.getName()).equalsIgnoreCase(event.getMessage()))
			{
				event.setCancelled(true);
				player.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Bitte wieder hole dich nicht"));
			}
			else
			{
				getLastMessage().put(player.getName(), event.getMessage());
				getDelay().add(player.getName());
				new Thread(() -> {
					try
					{
						Thread.sleep(1500);
					} catch (InterruptedException ex)
					{
						ex.printStackTrace();
					} finally
					{
						getDelay().remove(player.getName());
					}
				}).start();
			}
		}
	}
}
