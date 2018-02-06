package net.darkblocks.core.bungee.onlinetime.listener;

import lombok.Getter;
import net.darkblocks.core.bungee.onlinetime.OnlineTime;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by LartyHD on 02.10.2017  22:16.
 */
@Getter
public class OnlineTimeListener implements Listener
{
	private final OnlineTime onlineTime;
	
	public OnlineTimeListener(Plugin plugin, OnlineTime onlineTime)
	{
		this.onlineTime = onlineTime;
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
	}
	
	@EventHandler
	public void onPostLoginEvent(PostLoginEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		String playerName = player.getName();
		String ip = player.getAddress().getHostString();
		getOnlineTime().getTime().put(uuid, System.currentTimeMillis() / 1000);
		getOnlineTime().getMySQL().query("SELECT `uuid` FROM OnlineTime WHERE `uuid` = '" + uuid + "'", result -> {
			try
			{
				if (!result.next())
				{
					getOnlineTime().getMySQL().update("INSERT INTO OnlineTime(`uuid`, `name`, `ip`, `time`) VALUES ('" + uuid + "','" + playerName + "','" + ip + "','" + 0 + "')");
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		getOnlineTime().getMySQL().query("SELECT `uuid` FROM OnlineTime WHERE `uuid` = '" + player.getUniqueId() + "'", result -> {
			try
			{
				if (result.next())
				{
					getOnlineTime().updateTime(player.getUniqueId(), player.getName(), player.getAddress().getHostString());
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
}
