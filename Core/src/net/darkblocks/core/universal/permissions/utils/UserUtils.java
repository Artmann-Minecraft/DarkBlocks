package net.darkblocks.core.universal.permissions.utils;

import net.darkblocks.core.universal.permissions.manager.GroupManager;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.dark.java.mysql.MySQL;

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
		MySQLUtils.get(mySQL, "Userdata", "*", "uuid", uniqueId, result ->
		{
			try
			{
				if (result.next())
				{
					Set<String> groupIDs = (Set<String>) result.getObject("groups");
					Set<Group> groups = new HashSet<>();
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
					User user = new User(groups, new HashSet<>(), uniqueId, result.getString("prefix") == null ? (lowestGroup.getPrefix() == null ? "" : lowestGroup.getPrefix()) : result.getString("prefix"), result.getString("suffix") == null ? (lowestGroup.getPrefix() == null ? "" : lowestGroup.getPrefix()) : result.getString("suffix"), lowestSortID);
					userManager.getUser().add(user);
					for (Group group : user.getGroups())
					{
						user.getPermissions().addAll(group.getPermissions());
					}
					mySQL.query("SELECT * FROM `Permissions`", result1 ->
					{
						try
						{
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
						} catch (SQLException ex)
						{
							ex.printStackTrace();
						}
					});
				}
				else
				{
					Group defaultGroup = groupManager.getDefaultGroup();
					Set<Group> singleton = Collections.singleton(defaultGroup);
					userManager.getUser().add(new User(singleton, defaultGroup.getPermissions(), uniqueId, defaultGroup.getPrefix(), defaultGroup.getSuffix(), defaultGroup.getSortID()));
					String date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
					mySQL.update("INSERT INTO Userdata(`uuid`, `groups`, `firstonline`, `lastonline`) VALUES ('" + uniqueId + "','" + singleton + "','" + date + "','" + date + "'");
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
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
