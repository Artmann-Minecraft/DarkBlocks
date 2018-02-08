package net.darkblocks.core.bungee.onlinetime;

import lombok.Getter;
import net.darkblocks.core.bungee.onlinetime.commands.OnlineTimeCommand;
import net.darkblocks.core.bungee.onlinetime.commands.StatsOnlineTimeCommand;
import net.darkblocks.core.bungee.onlinetime.listener.OnlineTimeListener;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.java.utils.ClearCallback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class OnlineTime
{
	private final HashMap<UUID, Long> time;
	private final MySQL mySQL;
	
	public OnlineTime(Plugin plugin, MySQL mySQL)
	{
		this.time = new HashMap<>();
		this.mySQL = mySQL;
		getMySQL().update("CREATE TABLE IF NOT EXISTS OnlineTime(`uuid` VARCHAR(100), `name` VARCHAR(16), `ip` VARCHAR(100), `time` INT, PRIMARY KEY(uuid))");
		new OnlineTimeListener(plugin, this);
		new OnlineTimeCommand(plugin, this);
		new StatsOnlineTimeCommand(plugin, this);
	}
	
	public void disable()
	{
		for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers())
		{
			updateTime(players.getUniqueId(), players.getName(), players.getAddress().getHostString(), null);
		}
	}
	
	public HashMap<UUID, Long> getTime()
	{
		return this.time;
	}
	
	public void updateTime(UUID uuid, String playerName, String ip, ClearCallback callback)
	{
		getMySQL().query("SELECT `time` FROM OnlineTime WHERE `uuid` = '" + uuid + "'", result -> {
			try
			{
				if (result.next())
				{
					getMySQL().update("UPDATE OnlineTime SET `name` = '" + playerName + "', `ip` = '" + ip + "', `time` = '" + ((System.currentTimeMillis() / 1000) - getTime().get(uuid) + result.getLong(1)) + "' WHERE `uuid` = '" + uuid + "'");
					getTime().put(uuid, System.currentTimeMillis() / 1000);
					if (callback != null)
					{
						callback.call();
					}
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
}
