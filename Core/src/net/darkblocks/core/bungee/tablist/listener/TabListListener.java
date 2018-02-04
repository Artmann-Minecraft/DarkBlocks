package net.darkblocks.core.bungee.tablist.listener;

import lombok.Getter;
import lombok.NonNull;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import net.darkblocks.dark.universal.messages.Messages;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 22.01.2018  01:18.
 */
@Getter
public class TabListListener implements Listener
{
	public TabListListener(Plugin plugin)
	{
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
	}
	
	@EventHandler
	public void onLoginEvent(LoginEvent event)
	{
		sendTab();
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
	{
		sendTab();
	}
	
	@EventHandler
	public void onServerConnectedEvent(ServerConnectedEvent event)
	{
		sendTab();
	}
	
	@EventHandler
	public void onServerDisconnectEvent(ServerDisconnectEvent event)
	{
		sendTab();
	}
	
	@EventHandler
	public void onServerSwitchEvent(ServerSwitchEvent event)
	{
		sendTab();
		new Thread(() ->
		{
			try
			{
				Thread.sleep(15);
				sendTab(event.getPlayer());
			} catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
		}).start();
	}
	
	private void sendTab()
	{
		for (ProxiedPlayer players : BungeeCord.getInstance().getPlayers())
		{
			sendTab(players);
		}
	}
	
	private void sendTab(@NonNull ProxiedPlayer player)
	{
		String header = "\n" + TEXT + "│  " + Messages.getInstance().getMessage(Messages.getInstance().getPathPrefix() + "servername") + TEXT + " │\n\n" + TEXT + "│  " + IMPORTANT + player.getServer().getInfo().getName() + TEXT + " │\n";
		String footer = "\n" + TEXT + "│  " + IMPORTANT + BungeeCord.getInstance().getOnlineCount() + " Spieler " + TEXT + "│\n";
		player.setTabHeader(new TextComponent(header), new TextComponent(footer));
	}
}
