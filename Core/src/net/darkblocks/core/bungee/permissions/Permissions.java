package net.darkblocks.core.bungee.permissions;

import net.darkblocks.core.bungee.permissions.listener.UserListener;
import net.darkblocks.core.universal.permissions.manager.GroupManager;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.dark.java.mysql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created by LartyHD on 09.02.2018  03:56.
 */
public class Permissions
{
	public Permissions(Plugin plugin, MySQL mySQL)
	{
		new UserListener(plugin, mySQL, new UserManager(mySQL, null), new GroupManager(mySQL, null));
	}
}
