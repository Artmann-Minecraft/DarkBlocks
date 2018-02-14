package net.darkblocks.core.bungee.pingtracker.listener;

import net.darkblocks.dark.java.utils.Logger;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 11.02.2018  19:37.
 */
public class PingTrackerListener implements Listener
{
	private final Set<String> minutePings;
	private final Set<String> hourPings;
	private final Set<String> dayPings;
	
	public PingTrackerListener(Plugin plugin)
	{
		this.minutePings = new HashSet<>();
		this.hourPings = new HashSet<>();
		this.dayPings = new HashSet<>();
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
		Logger logger = null;
		try
		{
			logger = new Logger("Ping", plugin.getDataFolder() + File.separator + "logs");
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		Logger finalLogger = logger;
		BungeeCord.getInstance().getScheduler().schedule(plugin, () -> {
			String message = "In der letzten Minute wurden " + IMPORTANT + this.minutePings.size() + TEXT + " Pings dokumentiert";
			if (finalLogger != null)
			{
				finalLogger.log(Level.INFO, message);
			}
			this.minutePings.clear();
		}, 0, 1, TimeUnit.MINUTES);
		BungeeCord.getInstance().getScheduler().schedule(plugin, () -> {
			String message = "In der letzten Stunden wurden " + IMPORTANT + this.hourPings.size() + TEXT + " Pings dokumentiert";
			if (finalLogger != null)
			{
				finalLogger.log(Level.INFO, message);
			}
			for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers())
			{
				if (player.hasPermission(CommandUtils.getPermission(getClass())))
				{
					player.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + message));
				}
			}
			this.hourPings.clear();
		}, 0, 1, TimeUnit.HOURS);
		BungeeCord.getInstance().getScheduler().schedule(plugin, () -> {
			String message = "In den letzten 24 Stunden wurden " + IMPORTANT + this.dayPings.size() + TEXT + " Pings dokumentiert";
			if (finalLogger != null)
			{
				finalLogger.log(Level.INFO, message);
			}
			for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers())
			{
				if (player.hasPermission(CommandUtils.getPermission(getClass())))
				{
					player.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + message));
				}
			}
			this.dayPings.clear();
		}, 0, 1, TimeUnit.DAYS);
	}
	
	@EventHandler
	public void onProxyPingEvent(ProxyPingEvent event)
	{
		String name = event.getConnection().getAddress().getHostString();
		this.minutePings.add(name);
		this.hourPings.add(name);
		this.dayPings.add(name);
	}
}
