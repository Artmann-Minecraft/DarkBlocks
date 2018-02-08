package net.darkblocks.core.bungee.blockedcommands;

import net.darkblocks.core.bungee.blockedcommands.listener.BlockedCommandsListener;
import net.darkblocks.dark.java.mysql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LartyHD on 07.02.2018  20:20.
 */
public class BlockedCommands
{
	public BlockedCommands(Plugin plugin, MySQL mySQL)
	{
		mySQL.updateSync("CREATE TABLE IF NOT EXISTS BlockedCommands(`id` INT NOT NULL AUTO_INCREMENT, `command` VARCHAR(100), PRIMARY KEY(id))");
		mySQL.query("SELECT * FROM BlockedCommands", resultSet -> {
			try
			{
				List<String> blocked = new ArrayList<>();
				while (resultSet.next())
				{
					blocked.add(resultSet.getString("command").toLowerCase());
				}
				new BlockedCommandsListener(plugin, blocked);
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
}
