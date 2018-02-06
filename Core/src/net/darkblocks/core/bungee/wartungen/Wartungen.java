package net.darkblocks.core.bungee.wartungen;

import lombok.Getter;
import lombok.Setter;
import net.darkblocks.core.bungee.wartungen.commands.WartungenCommand;
import net.darkblocks.core.bungee.wartungen.listener.WartungenListener;
import net.darkblocks.dark.java.mysql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.SQLException;

/**
 * Created by LartyHD on 09.01.2018  08:34.
 */
@Getter
@Setter
public class Wartungen
{
	private final Plugin plugin;
	private final MySQL mySQL;
	private boolean on;
	
	public Wartungen(Plugin plugin, MySQL mySQL)
	{
		this.plugin = plugin;
		this.mySQL = mySQL;
		this.on = false;
		getMySQL().updateSync("CREATE TABLE IF NOT EXISTS Wartungen(`on` INT)");
		getMySQL().query("SELECT * FROM Wartungen", resultSet -> {
			try
			{
				if (resultSet.next())
				{
					setOn(resultSet.getInt("on") == 1);
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
		new WartungenListener(this);
		new WartungenCommand(this);
	}
}
