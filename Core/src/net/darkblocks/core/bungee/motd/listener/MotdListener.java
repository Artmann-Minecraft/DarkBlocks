package net.darkblocks.core.bungee.motd.listener;

import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.universal.messages.Messages;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;

/**
 * Created by LartyHD on 06.02.2018  10:07.
 */
public class MotdListener implements Listener
{
	private String motd;
	private int maxPlayers;
	
	public MotdListener(Plugin plugin, MySQL mySQL)
	{
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
		mySQL.updateSync("CREATE TABLE IF NOT EXISTS Motd(`motd` VARCHAR(250), `maxplayers` INT)");
		mySQL.query("SELECT * FROM Motd", result -> {
			try
			{
				if (result.next())
				{
					this.motd = result.getString(1);
					this.maxPlayers = result.getInt("maxplayers");
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
	
	@EventHandler
	public void onProxyPingEvent(ProxyPingEvent event)
	{
		ServerPing ping = event.getResponse();
		ServerPing.Players players = ping.getPlayers();
		ServerPing.Protocol version = ping.getVersion();
		version.setName(Messages.getInstance().getShortMessage(getClass(), "servername") + " System 1.8.X");
		version.setProtocol(version.getProtocol());
		players.setOnline(BungeeCord.getInstance().getOnlineCount());
		players.setMax(this.maxPlayers);
		ping.setPlayers(players);
		ping.setVersion(version);
		ping.setDescriptionComponent(new TextComponent(this.motd));
	}
}
