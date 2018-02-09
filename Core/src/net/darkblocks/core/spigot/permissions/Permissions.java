package net.darkblocks.core.spigot.permissions;

import net.darkblocks.core.spigot.permissions.listener.UserListener;
import net.darkblocks.core.universal.permissions.manager.GroupManager;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.dark.java.mysql.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 09.02.2018  03:56.
 */
public class Permissions
{
	public Permissions(JavaPlugin javaPlugin, MySQL mySQL, UserManager userManager, GroupManager groupManager)
	{
		new UserListener(javaPlugin, mySQL, userManager, groupManager);
	}
}
