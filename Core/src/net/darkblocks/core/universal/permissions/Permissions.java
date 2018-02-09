package net.darkblocks.core.universal.permissions;

import net.darkblocks.dark.java.mysql.MySQL;

/**
 * Created by LartyHD on 06.10.2017  22:15.
 */
public class Permissions
{
	public Permissions(MySQL mySQL)
	{
		mySQL.update("CREATE TABLE IF NOT EXISTS Permissions(`id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(100), `type` INT, `permission` VARCHAR(100), PRIMARY KEY(id))");
	}
}
