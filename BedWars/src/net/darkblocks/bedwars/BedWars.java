package net.darkblocks.bedwars;

import net.darkblocks.bedwars.listener.CountdownListener;
import net.darkblocks.core.spigot.Core;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.spigot.plugin.DarkPlugin;

/**
 * Created by LartyHD on 10.01.2018  18:38.
 */
public class BedWars extends DarkPlugin
{
	@Override
	public synchronized void onEnable()
	{
		super.onEnable();
		new Core(this, new MySQL());
		new CountdownListener(this);
	}
}
