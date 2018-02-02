package net.darkblocks.core.bungee.otherversionblocker;

import net.darkblocks.core.bungee.otherversionblocker.listener.OtherVersionBlockerListener;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created by LartyHD on 19.01.2018  23:15.
 */
public class OtherVersionBlocker
{
	public OtherVersionBlocker(Plugin plugin)
	{
		new OtherVersionBlockerListener(plugin);
	}
}
