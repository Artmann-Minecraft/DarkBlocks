/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.java.mysql;

import lombok.Getter;
import net.darkblocks.dark.java.config.PropertiesConfig;
import net.darkblocks.dark.java.utils.Callback;
import net.darkblocks.dark.java.utils.ClearCallback;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
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
	private final List<String> stats;
	
	public StatsAPI(String tableName, List<String> stats, MySQL statsMySQL)
	{
		this.stats = stats;
		this.mySQL = statsMySQL;
		this.tableName = tableName;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append("(`uuid` VARCHAR(100), ");
		for (String stat : this.stats)
		{
			stringBuilder.append("`").append(stat.toLowerCase()).append("` INT, ");
		}
		stringBuilder.append(" PRIMARY KEY(uuid))");
		String command = stringBuilder.toString();
		System.out.println("[STATS] MySQL Command wurde zusammen gebaut: " + command);
		getMySQL().update(command);
	}
	
	public StatsAPI(JavaPlugin javaPlugin, List<String> stats)
	{
		this(javaPlugin.getName(), stats, new MySQL(new PropertiesConfig(new File("databases"), "stats.properties")));
	}
	
	public void createAccount(UUID uuid)
	{
		this.mySQL.query("SELECT `uuid` FROM " + this.tableName + " WHERE `uuid` = '" + uuid + "'", result -> {
			try
			{
				if (!result.next())
				{
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append("INSERT INTO ").append(this.tableName).append("(`uuid`");
					for (String stat : this.stats)
					{
						stringBuilder.append(", `").append(stat.toLowerCase()).append("`");
					}
					stringBuilder.append(") VALUES ('").append(uuid).append("'");
					for (int i = 0; i < this.stats.size(); i++)
					{
						stringBuilder.append(",'" + 0 + "'");
					}
					stringBuilder.append(")");
					String command = stringBuilder.toString();
					System.out.println("[STATS] MySQL CreateAccount Command wurde zusammen gebaut: " + command);
					this.mySQL.update(command);
				}
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
		set(uuid, count, statsName, null);
	}
	
	public void set(UUID uuid, int count, String statsName, ClearCallback callback)
	{
		this.mySQL.update("UPDATE " + this.tableName + " SET " + statsName.toLowerCase() + " = '" + count + "' WHERE uuid = '" + uuid.toString() + "'", () -> {
			if (callback != null)
			{
				callback.call();
			}
		});
	}
	
	public void add(UUID uuid, int count, String statsName)
	{
		add(uuid, count, statsName, null);
	}
	
	public void add(UUID uuid, int count, String statsName, ClearCallback callback)
	{
		get(uuid, statsName, result -> {
			set(uuid, result + count, statsName)
			; if (callback != null)
			{
				callback.call();
			}
		});
	}
	
	public void remove(UUID uuid, int count, String statsName)
	{
		remove(uuid, count, statsName, null);
	}
	
	public void remove(UUID uuid, int count, String statsName, ClearCallback callback)
	{
		get(uuid, statsName, result -> {
			set(uuid, result - count, statsName)
			; if (callback != null)
			{
				callback.call();
			}
		});
	}
}
