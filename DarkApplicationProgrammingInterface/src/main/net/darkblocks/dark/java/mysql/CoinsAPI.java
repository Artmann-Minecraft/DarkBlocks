/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.dark.java.mysql;

import net.darkblocks.dark.java.utils.Callback;
import net.darkblocks.dark.spigot.events.PlayerUpdateCoinsEvent;
import net.darkblocks.dark.universal.messages.Colors;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by LartyHD on 02.08.2017  13:28.
 */
@SuppressWarnings("ALL")
public class CoinsAPI
{
	private final Messages messages;
	private final String tableName;
	private final MySQL mySQL;
	
	/**
	 * Erstellt eine neuen Table mit dem Name den man über "tableName" angeben kann
	 */
	public CoinsAPI(Messages messages, String tableName, MySQL mySQL)
	{
		this.messages = messages;
		this.tableName = tableName;
		this.mySQL = mySQL;
		this.mySQL.update("CREATE TABLE IF NOT EXISTS " + tableName + "(`uuid` VARCHAR(100), `coins` INT, `name` VARCHAR(20), PRIMARY KEY(uuid))");
	}
	
	/**
	 * Erstellt ein neuen Account für die übergebene UUID und mit dem übergebenen Startguthaben
	 */
	public void createAccount(UUID uuid, int startguthaben)
	{
		getAccount(uuid, (ResultSet result) -> {
			try
			{
				if (result.next())
				{
					return;
				}
				this.mySQL.update("INSERT INTO " + this.tableName + " (uuid, coins) VALUES ('" + uuid + "', '" + startguthaben + "')");
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
	
	/**
	 * Gibt den Account der übergebenen UUID zurück
	 */
	public void getAccount(UUID uuid, Callback<ResultSet> callback)
	{
		this.mySQL.query("SELECT uuid FROM " + this.tableName + " WHERE uuid = '" + uuid + "'", callback);
	}
	
	/**
	 * Gibt die Coins des Accounts der übergebenen UUID
	 */
	public void getCoins(UUID uuid, Callback<Integer> callback)
	{
		this.mySQL.query("SELECT coins FROM " + this.tableName + " WHERE `uuid` = '" + uuid + "'", result -> {
			try
			{
				if (!result.next())
				{
					return;
				}
				callback.call(result.getInt("coins"));
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
	
	/**
	 * Setzt dem Account mit der übergebenen UUID die Coins auf die angegebenen Coins
	 */
	public void setCoins(UUID uuid, int coins)
	{
		Player player = Bukkit.getPlayer(uuid);
		if (player != null)
		{
			player.sendMessage(this.messages.getMessage("dark.coins.prefix") + Colors.TEXT + "Deine " + Colors.IMPORTANT + "Coins" + Colors.TEXT + " wurden auf " + Colors.IMPORTANT + coins + Colors.TEXT + " gesetzt");
		}
		this.mySQL.update("UPDATE " + this.tableName + " SET `coins` = '" + coins + "' WHERE `uuid` = '" + uuid + "'");
		Bukkit.getPluginManager().callEvent(new PlayerUpdateCoinsEvent(player, coins));
	}
	
	/**
	 * Fügt dem Account mit der übergebenen UUID die angegebenen Coins hinzu
	 */
	public void addCoins(UUID uuid, int coins)
	{
		Player player = Bukkit.getPlayer(uuid);
		if (player != null)
		{
			player.sendMessage(this.messages.getMessage("dark.coins.prefix") + Colors.TEXT + "Dir wurden " + Colors.IMPORTANT + coins + " Coins" + Colors.TEXT + " hinzugefügt");
		}
		getCoins(uuid, result -> setCoins(uuid, result + coins));
	}
	
	/**
	 * Entfernt dem Account mit der übergebenen UUID die angegebenen Coins
	 */
	public void removeCoins(UUID uuid, int coins)
	{
		Player player = Bukkit.getPlayer(uuid);
		if (player != null)
		{
			player.sendMessage(this.messages.getMessage("dark.coins.prefix") + Colors.TEXT + "Dir wurden " + Colors.IMPORTANT + coins + " Coins" + Colors.TEXT + " gelöscht");
		}
		getCoins(uuid, result -> setCoins(uuid, result - coins));
	}
}
