package net.darkblocks.core.bungee.tablist;

import net.darkblocks.core.bungee.tablist.listener.TabListListener;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created by LartyHD on 22.01.2018  01:17.
 */
public class TabList
{
	public TabList(Plugin plugin)
	{
		new TabListListener(plugin);
	}
}
