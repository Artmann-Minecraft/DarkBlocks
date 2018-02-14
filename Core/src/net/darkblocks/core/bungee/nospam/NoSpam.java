package net.darkblocks.core.bungee.nospam;

import net.darkblocks.core.bungee.nospam.listener.NoSpamListener;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created by LartyHD on 13.02.2018  15:00.
 */
public class NoSpam
{
	public NoSpam(Plugin plugin)
	{
		new NoSpamListener(plugin);
	}
}
