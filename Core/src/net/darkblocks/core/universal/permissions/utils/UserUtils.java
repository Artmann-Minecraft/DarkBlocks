package net.darkblocks.core.universal.permissions.utils;

import net.darkblocks.core.universal.permissions.manager.GroupManager;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.java.utils.ClearCallback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by LartyHD on 09.02.2018  04:03.
 */
public class UserUtils
{
	public static void onLogin(MySQL mySQL, UUID uniqueId, UserManager userManager, GroupManager groupManager)
	{
		try
		{
			ResultSet result = MySQLUtils.get(mySQL, "Userdata", "*", "uuid", uniqueId);
			if (result.next())
			{
				User user = getUser(groupManager, result, uniqueId);
				if (user != null)
				{
					userManager.getUser().add(user);
					for (Group group : user.getGroups())
					{
						user.getPermissions().addAll(group.getPermissions());
					}
					result = mySQL.querySync("SELECT * FROM `Permissions`");
					while (result.next())
					{
						if (result.getInt("type") == 1)
						{
							if (result.getString("name").equalsIgnoreCase(uniqueId.toString()))
							{
								user.getPermissions().add(result.getString("permission"));
							}
						}
					}
				}
			}
			else
			{
				Group defaultGroup = groupManager.getDefaultGroup();
				userManager.getUser().add(new User(Collections.singleton(defaultGroup), defaultGroup.getPermissions(), uniqueId, defaultGroup.getPrefix(), defaultGroup.getSuffix(), defaultGroup.getSortID()));
				String date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
				mySQL.updateSync("INSERT INTO Userdata(`uuid`, `groups`, `firstonline`, `lastonline`) VALUES ('" + uniqueId + "','" + defaultGroup.getSaveID() + "','" + date + "','" + date + "')");
			}
		} catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void onLogin(MySQL mySQL, UUID uniqueId, UserManager userManager, GroupManager groupManager, ClearCallback callback)
	{
		MySQLUtils.get(mySQL, "Userdata", "*", "uuid", uniqueId, (ResultSet result) -> {
			try
			{
				if (result.next())
				{
					User user = getUser(groupManager, result, uniqueId);
					if (user != null)
					{
						userManager.getUser().add(user);
						for (Group group : user.getGroups())
						{
							user.getPermissions().addAll(group.getPermissions());
						}
						mySQL.query("SELECT * FROM `Permissions`", result1 -> {
							while (result1.next())
							{
								if (result1.getInt("type") == 1)
								{
									if (result1.getString("name").equalsIgnoreCase(uniqueId.toString()))
									{
										user.getPermissions().add(result1.getString("permission"));
									}
								}
							}
							if (callback != null)
							{
								callback.call();
							}
						});
					}
				}
				else
				{
					Group defaultGroup = groupManager.getDefaultGroup();
					userManager.getUser().add(new User(Collections.singleton(defaultGroup), defaultGroup.getPermissions(), uniqueId, defaultGroup.getPrefix(), defaultGroup.getSuffix(), defaultGroup.getSortID()));
					String date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
					mySQL.update("INSERT INTO Userdata(`uuid`, `groups`, `firstonline`, `lastonline`) VALUES ('" + uniqueId + "','" + defaultGroup.getSaveID() + "','" + date + "','" + date + "')", () -> {
						if (callback != null)
						{
							callback.call();
						}
					});
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
	
	private static User getUser(GroupManager groupManager, ResultSet result, UUID uuid)
	{
		try
		{
			Set<Group> groups = new HashSet<>();
			Set<String> groupIDs = new HashSet<>(Arrays.asList(result.getString("groups").split(", ")));
			int lowestSortID = Integer.MAX_VALUE;
			Group lowestGroup = groupManager.getDefaultGroup();
			for (String groupID : groupIDs)
			{
				for (Group group : groupManager.getGroups())
				{
					int sortID = Integer.valueOf(groupID);
					if (sortID == group.getSortID())
					{
						if (sortID < lowestSortID)
						{
							lowestSortID = sortID;
							lowestGroup = group;
						}
						groups.add(group);
					}
				}
			}
			return new User(groups, new HashSet<>(), uuid, result.getString("prefix") == null ? (lowestGroup.getPrefix() == null ? "" : lowestGroup.getPrefix()) : result.getString("prefix"), result.getString("suffix") == null ? (lowestGroup.getPrefix() == null ? "" : lowestGroup.getSuffix()) : result.getString("suffix"), lowestSortID);
		} catch (SQLException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public static void onDisconnect(MySQL mySQL, UUID uniqueId, UserManager userManager)
	{
		for (User user : userManager.getUser())
		{
			if (user.getUuid() == uniqueId)
			{
				MySQLUtils.set(mySQL, "Userdata", "lastonline", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()), "uuid", uniqueId);
				userManager.getUser().remove(user);
				return;
			}
		}
	}
}
