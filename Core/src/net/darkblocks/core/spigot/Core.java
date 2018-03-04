/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.core.spigot;

import net.darkblocks.core.spigot.fix.Fix;
import net.darkblocks.core.universal.permissions.manager.GroupManager;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.segdocloud.manager.CloudManager;
import net.darkblocks.dark.spigot.events.listener.EventsListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 09.01.2018  08:32.
 */
public class Core extends JavaPlugin
{
	public Core()
	{
	}
	
	@Override
	public void onEnable()
	{
		MySQL mySQL = new MySQL();
		new EventsListener(this);
		new Fix(this);
		new net.darkblocks.core.universal.permissions.Permissions(mySQL);
		new net.darkblocks.core.spigot.permissions.Permissions(this, mySQL, new UserManager(mySQL, null), new GroupManager(mySQL, null));
		new CloudManager(this, "");
	}
	
	public Core(JavaPlugin javaPlugin, MySQL mySQL, UserManager userManager, GroupManager groupManager)
	{
		new EventsListener(javaPlugin);
		new Fix(javaPlugin);
		new net.darkblocks.core.universal.permissions.Permissions(mySQL);
		new net.darkblocks.core.spigot.permissions.Permissions(javaPlugin, mySQL, userManager, groupManager);
		new CloudManager(javaPlugin, "");
	}
}
