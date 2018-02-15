package net.darkblocks.core.universal.permissions.utils;

import lombok.Getter;
import lombok.NonNull;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.java.utils.Callback;

import java.sql.ResultSet;

/**
 * Created by LartyHD on 08.02.2018  02:47.
 */
@Getter
public class MySQLUtils
{
	public static void getPermissions(@NonNull MySQL mySQL, int saveID, String tableName, String type, @NonNull Callback<ResultSet> callback)
	{
		mySQL.query("SELECT `*` FROM `" + (tableName == null ? "Permissions" : tableName) + "` WHERE `name` = '" + saveID + "' AND `type` = '" + type + "'", callback);
	}
	
	public static ResultSet get(@NonNull MySQL mySQL, String tableName, String key, Object whereKey, Object whereValue)
	{
		return mySQL.querySync("SELECT " + key.toLowerCase() + " FROM `" + tableName + "` WHERE `" + whereKey + "` = '" + whereValue + "'");
	}
	
	public static void get(@NonNull MySQL mySQL, String tableName, String key, Object whereKey, Object whereValue, Callback<ResultSet> callback)
	{
		mySQL.query("SELECT " + key.toLowerCase() + " FROM `" + tableName + "` WHERE `" + whereKey + "` = '" + whereValue + "'", callback);
	}
	
	public static void set(@NonNull MySQL mySQL, String tableName, String key, Object value, Object whereKey, Object whereValue)
	{
		mySQL.update("UPDATE " + tableName + " SET `" + key.toLowerCase() + "` = '" + value + "' WHERE `" + whereKey + "` = '" + whereValue + "'");
	}
}
