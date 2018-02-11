package net.darkblocks.bedwars;

import net.darkblocks.bedwars.listener.CountdownListener;
import net.darkblocks.core.spigot.Core;
import net.darkblocks.core.universal.permissions.manager.GroupManager;
import net.darkblocks.core.universal.permissions.manager.UserManager;
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
		MySQL mySQL = new MySQL();
		new Core(this, mySQL, new UserManager(mySQL, null), new GroupManager(mySQL, null));
		new CountdownListener(this);
	}
}
