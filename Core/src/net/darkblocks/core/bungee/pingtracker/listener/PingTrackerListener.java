package net.darkblocks.core.bungee.pingtracker.listener;

import net.darkblocks.core.universal.logger.Logger;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 11.02.2018  19:37.
 */
public class PingTrackerListener implements Listener
{
	private final List<String> pings;
	
	public PingTrackerListener(Plugin plugin)
	{
		this.pings = new ArrayList<>();
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
			if (finalLogger != null)
			{
				finalLogger.log(Level.INFO, "In der letzten Minute wurde der Server " + this.pings.size() + " mal an gepingt");
			}
			System.out.println("In der letzten Minute wurde der Server " + this.pings.size() + " mal an gepingt");
			for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers())
			{
				if (player.hasPermission(CommandUtils.getPermission(getClass())))
				{
					player.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "In der letzten Minute wurde der Server " + IMPORTANT + this.pings.size() + TEXT + " mal an gepingt"));
				}
			}
			this.pings.clear();
		}, 0, TimeUnit.MINUTES);
	}
	
	@EventHandler
	public void onProxyPingEvent(ProxyPingEvent event)
	{
		this.pings.add(event.getConnection().getName());
	}
}
