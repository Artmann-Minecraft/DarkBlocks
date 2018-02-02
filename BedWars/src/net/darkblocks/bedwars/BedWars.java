package net.darkblocks.bedwars;

import net.craftplugin.craftpluginapi.spigot.plugin.CraftPlugin;
import net.darkblocks.bedwars.listener.CountdownListener;
import net.darkblocks.core.spigot.Core;

/**
 * Created by LartyHD on 10.01.2018  18:38.
 */
public class BedWars extends CraftPlugin
{
	@Override
	public synchronized void onEnable()
	{
		super.onEnable();
		new Core(this);
		new CountdownListener(this);
	}
}
