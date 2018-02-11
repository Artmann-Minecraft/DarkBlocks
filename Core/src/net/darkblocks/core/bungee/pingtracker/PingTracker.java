package net.darkblocks.core.bungee.pingtracker;

import net.darkblocks.core.bungee.pingtracker.listener.PingTrackerListener;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created by LartyHD on 11.02.2018  19:36.
 */
public class PingTracker
{
	public PingTracker(Plugin plugin)
	{
		new PingTrackerListener(plugin);
	}
}
