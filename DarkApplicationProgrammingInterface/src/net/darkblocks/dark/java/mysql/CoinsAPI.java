/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.java.mysql;

import lombok.Getter;
import net.darkblocks.dark.java.utils.Callback;
import net.darkblocks.dark.java.utils.ValueType;
import net.darkblocks.dark.universal.messages.Messages;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 02.08.2017  13:28.
 */
@Getter
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
	private void getAccount(UUID uuid, Callback<ResultSet> callback)
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
				if (callback != null)
				{
					if (result.next())
					{
						callback.call(result.getInt(1));
					}
					else
					{
						callback.call(-1);
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
	@SuppressWarnings("UnusedReturnValue")
	private String setCoins(UUID uuid, String coins, Callback<String> callback)
	{
		this.mySQL.update("UPDATE " + this.tableName + " SET `coins` = '" + coins + "' WHERE `uuid` = '" + uuid + "'", () -> {
			if (callback != null)
			{
				try
				{
					callback.call(coins);
				} catch (SQLException ex)
				{
					ex.printStackTrace();
				}
			}
		});
		return Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Deine " + PRIMARY + IMPORTANT + "Coins" + TEXT + " wurden auf " + IMPORTANT + coins + TEXT + " gesetzt";
	}
	
	/**
	 * Fügt dem Account mit der übergebenen UUID die angegebenen Coins hinzu
	 */
	public String addCoins(UUID uuid, String coins, Callback<String> callback)
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
		return Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Dir wurden " + PRIMARY + coins + IMPORTANT + " Coins" + TEXT + " hinzugefügt";
	}
	
	/**
	 * Entfernt dem Account mit der übergebenen UUID die angegebenen Coins
	 */
	@SuppressWarnings("UnusedReturnValue")
	public String removeCoins(UUID uuid, String coins, Callback<String> callback)
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
		return Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Dir wurden " + PRIMARY + coins + IMPORTANT + " Coins" + TEXT + " gelöscht";
	}
}
