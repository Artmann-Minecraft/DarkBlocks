package net.darkblocks.core.bungee.pingtracker;

import net.darkblocks.core.bungee.pingtracker.listener.PingTrackerListener;
import net.darkblocks.dark.universal.messages.Messages;
import net.md_5.bungee.api.plugin.Plugin;

import static net.darkblocks.dark.universal.messages.Colors.EXTRA;
import static net.darkblocks.dark.universal.messages.Colors.PRIMARY;

/**
 * Created by LartyHD on 11.02.2018  19:36.
 */
public class PingTracker
{
	public PingTracker(Plugin plugin)
	{
		Messages.getInstance().add("dark.core.bungee.pingtracker.listener.pingtrackerlistener.prefix", "§f" + EXTRA + "[" + PRIMARY + EXTRA + "Ping§f" + EXTRA + "] §r");
		new PingTrackerListener(plugin);
	}
}
