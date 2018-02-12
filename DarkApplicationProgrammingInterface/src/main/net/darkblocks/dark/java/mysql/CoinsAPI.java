/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.dark.java.mysql;

import net.darkblocks.dark.java.utils.Callback;
import net.darkblocks.dark.java.utils.ClearCallback;
import net.darkblocks.dark.java.utils.ValueType;
import net.darkblocks.dark.universal.messages.Messages;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 02.08.2017  13:28.
 */
public class CoinsAPI
{
	private final String tableName;
	private final MySQL mySQL;
	private final ValueType valueType;
	
	/**
	 * Erstellt eine neuen Table mit dem Name den man über "tableName" angeben kann
	 */
	public CoinsAPI(String tableName, ValueType valueType, MySQL mySQL)
	{
		this.tableName = tableName;
		this.valueType = valueType;
		this.mySQL = mySQL;
		this.mySQL.update("CREATE TABLE IF NOT EXISTS " + tableName + "(`uuid` VARCHAR(100), `coins` " + (valueType == ValueType.LONG ? "BIGINT" : valueType == ValueType.INTEGER ? "INT" : valueType == ValueType.DOUBLE ? ValueType.DOUBLE : valueType == ValueType.FLOAT) + ", `name` VARCHAR(20), PRIMARY KEY(uuid))");
	}
	
	/**
	 * Erstellt ein neuen Account für die übergebene UUID und mit dem übergebenen Startguthaben
	 */
	public void createAccount(UUID uuid, String startguthaben)
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
				if (result.next() && callback != null)
				{
					try
					{
						System.out.println(result);
						System.out.println(result.getInt(1));
						callback.call(result.getInt(1));
					} catch (SQLException ex)
					{
						ex.printStackTrace();
					}
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
	
	/**
	 * Setzt dem Account mit der übergebenen UUID die Coins auf die angegebenen Coins
	 */
	public String setCoins(UUID uuid, String coins, ClearCallback callback)
	{
		this.mySQL.update("UPDATE " + this.tableName + " SET `coins` = '" + coins + "' WHERE `uuid` = '" + uuid + "'", () -> {
			if (callback != null)
			{
				callback.call();
			}
		});
		return Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Deine " + IMPORTANT + "Coins" + TEXT + " wurden auf " + IMPORTANT + coins + TEXT + " gesetzt";
	}
	
	/**
	 * Fügt dem Account mit der übergebenen UUID die angegebenen Coins hinzu
	 */
	public String addCoins(UUID uuid, String coins, ClearCallback callback)
	{
		getCoins(uuid, result -> {
			switch (this.valueType)
			{
				case LONG:
					setCoins(uuid, String.valueOf(result + Long.valueOf(coins)), callback);
					break;
				case INTEGER:
					setCoins(uuid, String.valueOf(result + Integer.valueOf(coins)), callback);
					break;
				case FLOAT:
					setCoins(uuid, String.valueOf(result + Float.valueOf(coins)), callback);
					break;
				case DOUBLE:
					setCoins(uuid, String.valueOf(result + Double.valueOf(coins)), callback);
					break;
			}
		});
		return Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Dir wurden " + IMPORTANT + coins + " Coins" + TEXT + " hinzugefügt";
	}
	
	/**
	 * Entfernt dem Account mit der übergebenen UUID die angegebenen Coins
	 */
	public String removeCoins(UUID uuid, String coins, ClearCallback callback)
	{
		getCoins(uuid, result -> {
			switch (this.valueType)
			{
				case LONG:
					setCoins(uuid, String.valueOf(result - Long.valueOf(coins)), callback);
					break;
				case INTEGER:
					setCoins(uuid, String.valueOf(result - Integer.valueOf(coins)), callback);
					break;
				case FLOAT:
					setCoins(uuid, String.valueOf(result - Float.valueOf(coins)), callback);
					break;
				case DOUBLE:
					setCoins(uuid, String.valueOf(result - Double.valueOf(coins)), callback);
					break;
			}
		});
		return Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Dir wurden " + IMPORTANT + coins + " Coins" + TEXT + " gelöscht";
	}
}
