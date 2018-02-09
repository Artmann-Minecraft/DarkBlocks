package net.darkblocks.core.bungee.autoban;

import lombok.Getter;
import net.darkblocks.core.bungee.autoban.commands.AddToAutoChatBanCommand;
import net.darkblocks.core.bungee.autoban.listener.ChatBanListener;
import net.darkblocks.dark.java.mysql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LartyHD on 09.01.2018  08:35.
 */
@Getter
public class ChatBan
{
	public ChatBan(Plugin plugin, MySQL mySQL)
	{
		mySQL.updateSync("CREATE TABLE IF NOT EXISTS ChatAutoBan(`id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(100), PRIMARY KEY(id))");
		mySQL.query("SELECT * FROM ChatAutoBan", resultSet -> {
			try
			{
				List<String> block = new ArrayList<>();
				while (resultSet.next())
				{
					block.add(resultSet.getString("name").toLowerCase());
				}
				new ChatBanListener(plugin, block);
				new AddToAutoChatBanCommand(plugin, mySQL);
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
}
