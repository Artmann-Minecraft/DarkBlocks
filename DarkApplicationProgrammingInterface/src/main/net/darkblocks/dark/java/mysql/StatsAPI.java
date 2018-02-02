/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.dark.java.mysql;

import lombok.Getter;
import net.darkblocks.dark.java.utils.Callback;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by LartyHD on 18.08.2017  13:49.
 * Project: RedStone
 */
@Getter
public class StatsAPI
{
	private final String tableName;
	private final MySQL mySQL;
	private final ArrayList<String> stats;
	
	public StatsAPI(String tableName, ArrayList<String> stats, MySQL statsMySQL)
	{
		this.stats = stats;
		this.mySQL = statsMySQL;
		this.tableName = tableName;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append("(`uuid` VARCHAR(100), `name` VARCHAR(100), `ip` VARCHAR(100), ");
		for (String stat : this.stats)
		{
			stringBuilder.append("`").append(stat.toLowerCase()).append("` INT, ");
		}
		stringBuilder.append(" PRIMARY KEY(uuid))");
		String command = stringBuilder.toString();
		System.out.println("[STATS] MySQL Command wurde zusammen gebaut: " + command);
		getMySQL().update(command);
	}
	
	public void createAccount(UUID uuid, String name, String ip)
	{
		this.mySQL.query("SELECT `uuid` FROM " + this.tableName + " WHERE `uuid` = '" + uuid + "'", result -> {
			try
			{
				if (result.next())
				{
					return;
				}
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("INSERT INTO ").append(this.tableName).append("(`uuid`, `name`, `ip`");
				for (String stat : this.stats)
				{
					stringBuilder.append(", `").append(stat.toLowerCase()).append("`");
				}
				stringBuilder.append(") VALUES ('").append(uuid).append("','").append(name).append("','").append(ip).append("'");
				for (int i = 0; i < this.stats.size(); i++)
				{
					stringBuilder.append(",'" + 0 + "'");
				}
				stringBuilder.append(")");
				String command = stringBuilder.toString();
				System.out.println("[STATS] MySQL CreateAccount Command wurde zusammen gebaut: " + command);
				this.mySQL.update(command);
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
	
	public void get(UUID uuid, String statsName, Callback<Integer> callback)
	{
		this.mySQL.query("SELECT " + statsName.toLowerCase() + " FROM " + this.tableName + " WHERE `uuid` = '" + uuid + "'", result -> {
			try
			{
				if (result.next())
				{
					callback.call(result.getInt(1));
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
	
	public void getRank(UUID uuid, String statsName, Callback<Integer> callback)
	{
		this.mySQL.query("SELECT rn.row, rn." + statsName.toLowerCase() + " FROM ( SELECT @row := @row + 1 as row, n." + statsName.toLowerCase() + ", n.name, n.uuid FROM " + this.tableName + " n, (SELECT @row := 0) r ORDER BY n." + statsName.toLowerCase() + " DESC ) rn WHERE `uuid` = '" + uuid + "'", result -> {
			try
			{
				if (result.next())
				{
					callback.call(result.getInt("row"));
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
	
	public void set(UUID uuid, int count, String statsName)
	{
		this.mySQL.update("UPDATE " + this.tableName + " SET " + statsName.toLowerCase() + " = '" + count + "' WHERE uuid = '" + uuid.toString() + "'");
	}
	
	public void add(UUID uuid, int count, String statsName)
	{
		get(uuid, statsName, result -> set(uuid, result + count, statsName));
	}
	
	public void remove(UUID uuid, int count, String statsName)
	{
		get(uuid, statsName, result -> set(uuid, result - count, statsName));
	}
}
