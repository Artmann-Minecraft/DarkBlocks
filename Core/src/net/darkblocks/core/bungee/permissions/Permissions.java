package net.darkblocks.core.bungee.permissions;

import net.darkblocks.dark.java.mysql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created by LartyHD on 06.10.2017  22:15.
 */
public class Permissions
{
	public Permissions(Plugin plugin, MySQL mySQL)
	{
		mySQL.update("CREATE TABLE IF NOT EXISTS Permissions(`id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(100), `type` INT, `permission` VARCHAR(100), PRIMARY KEY(id))");
		//new UserListener(plugin, mySQL, new UserManager(mySQL, null), new GroupManager(mySQL, null));
	}
}
