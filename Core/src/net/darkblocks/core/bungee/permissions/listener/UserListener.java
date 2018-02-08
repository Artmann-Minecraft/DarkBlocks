package net.darkblocks.core.bungee.permissions.listener;

import lombok.Getter;
import net.darkblocks.core.bungee.permissions.manager.GroupManager;
import net.darkblocks.core.bungee.permissions.manager.UserManager;
import net.darkblocks.core.bungee.permissions.utils.Group;
import net.darkblocks.core.bungee.permissions.utils.MySQLUtils;
import net.darkblocks.core.bungee.permissions.utils.User;
import net.darkblocks.dark.java.mysql.MySQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by LartyHD on 08.02.2018  01:44.
 */
@Getter
public class UserListener implements Listener
{
	private final UserManager userManager;
	private final GroupManager groupManager;
	private final MySQL mySQL;
	
	public UserListener(Plugin plugin, MySQL mySQL, UserManager userManager, GroupManager groupManager)
	{
		this.userManager = userManager;
		this.groupManager = groupManager;
		this.mySQL = mySQL;
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
	}
	
	@EventHandler
	public void onLoginEvent(LoginEvent event)
	{
		PendingConnection connection = event.getConnection();
		if (!event.isCancelled() && connection.isOnlineMode())
		{
			UUID uniqueId = connection.getUniqueId();
			MySQLUtils.get(this.mySQL, "Userdata", "*", "uuid", uniqueId, result ->
			{
				try
				{
					if (result.next())
					{
						Set<String> groupIDs = (Set<String>) result.getObject("groups");
						Set<Group> groups = new HashSet<>();
						int lowestSortID = Integer.MAX_VALUE;
						for (String groupID : groupIDs)
						{
							for (Group group : getGroupManager().getGroups())
							{
								int sortID = Integer.valueOf(groupID);
								if (sortID == group.getSortID())
								{
									if (sortID < lowestSortID)
									{
										lowestSortID = sortID;
									}
									groups.add(group);
								}
							}
						}
						User user = new User(groups, new HashSet<>(), connection, result.getString("prefix"), result.getString("suffix"), lowestSortID);
						getUserManager().getUser().add(user);
						for (Group group : user.getGroups())
						{
							user.getPermissions().addAll(group.getPermissions());
						}
						this.mySQL.query("SELECT `*` FROM `Permissions`", result1 ->
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
						Group defaultGroup = getGroupManager().getDefaultGroup();
						Set<Group> singleton = Collections.singleton(defaultGroup);
						getUserManager().getUser().add(new User(singleton, defaultGroup.getPermissions(), connection, defaultGroup.getPrefix(), defaultGroup.getSuffix(), defaultGroup.getSortID()));
						String date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
						getMySQL().update("INSERT INTO Userdata(`uuid`, `groups`, `fireonline`, `lastonline`, `prefix`, `suffix`) VALUES ('" + uniqueId + "','" + singleton + "','" + date + "','" + date + "','" + defaultGroup.getPrefix() + "','" + defaultGroup.getSuffix() + "'");
					}
				} catch (SQLException ex)
				{
					ex.printStackTrace();
				}
			});
		}
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
	{
		UUID uniqueId = event.getPlayer().getUniqueId();
		for (User user : getUserManager().getUser())
		{
			if (user.getConnection().getUniqueId() == uniqueId)
			{
				MySQLUtils.set(getMySQL(), "Userdata", "lastonline", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()), "uuid", uniqueId);
				getUserManager().getUser().remove(user);
				return;
			}
		}
	}
}
