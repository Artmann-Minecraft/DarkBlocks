package net.darkblocks.core.universal.permissions.manager;

import lombok.Getter;
import net.darkblocks.core.universal.permissions.utils.Group;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.java.utils.Callback;
import net.darkblocks.dark.universal.messages.ChatColor;
import net.md_5.bungee.BungeeCord;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by LartyHD on 07.02.2018  21:55.
 */
@Getter
public class GroupManager
{
	private final Set<Group> groups;
	private Group defaultGroup;
	
	public GroupManager(MySQL mySQL, String tableName)
	{
		this.groups = new HashSet<>();
		tableName = tableName == null ? "Groups" : tableName;
		String finalTableName = tableName;
		mySQL.update("CREATE TABLE IF NOT EXISTS " + tableName + "(`saveid` INT, `sortid` INT, `name` VARCHAR(16), `prefix` VARCHAR(16), `suffix` VARCHAR(16), `color` VARCHAR(13), `inherit` TEXT, PRIMARY KEY(saveid))", () -> {
			mySQL.query("SELECT * FROM `" + finalTableName + "`", result ->
			{
				try
				{
					while (result.next())
					{
						try
						{
							Set<Integer> inherit = new HashSet<>();
							if (result.getString("inherit") != null)
							{
								Set<String> rawInherit = new HashSet<>(Arrays.asList(result.getString("inherit").split(", ")));
								for (String s : rawInherit)
								{
									inherit.add(Integer.valueOf(s));
								}
							}
							getGroups().add(new Group(new HashSet<>(), inherit, result.getString("name"), result.getString("prefix"), result.getString("suffix"), ChatColor.valueOf(result.getString("color")), result.getInt("saveid"), result.getInt("sortid")));
						} catch (IllegalArgumentException ex)
						{
							ex.printStackTrace();
						}
					}
					for (Group group : getGroups())
					{
						loadPermissions(mySQL, group, new HashSet<>(), null);
					}
					Group defaultGroup = null;
					for (Group group : getGroups())
					{
						if (defaultGroup == null)
						{
							defaultGroup = group;
						}
						else if (defaultGroup.getSortID() < group.getSortID())
						{
							defaultGroup = group;
						}
					}
					this.defaultGroup = defaultGroup;
					if (defaultGroup == null)
					{
						System.err.println(" ");
						System.err.println(" ");
						System.err.println("Keine DefaultGroup gefunden!");
						System.err.println(" ");
						System.err.println(" ");
						BungeeCord.getInstance().stop("Keine DefaultGroup gefunden!");
					}
				} catch (SQLException ex)
				{
					ex.printStackTrace();
				}
			});
		});
	}
	
	private void loadPermissions(MySQL mySQL, Group group, Set<String> permissions, Callback<Set<String>> callback)
	{
		mySQL.query("SELECT * FROM `Permissions`", result ->
		{
			try
			{
				while (result.next() && result.getInt("type") == 0)
				{
					if (Integer.valueOf(result.getString("name")) == group.getSaveID())
					{
						group.getPermissions().add(result.getString("permission"));
					}
				}
				int times = 0;
				for (Integer integer : group.getInherit())
				{
					for (Group groups : getGroups())
					{
						if (groups.getSaveID() == integer)
						{
							times++;
						}
					}
				}
				AtomicInteger time = new AtomicInteger();
				int finalTimes = times;
				for (Integer integer : group.getInherit())
				{
					for (Group groups : getGroups())
					{
						if (groups.getSaveID() == integer)
						{
							loadPermissions(mySQL, groups, permissions, result1 -> {
								group.getPermissions().addAll(result1);
								time.getAndIncrement();
								if (finalTimes == time.get() && callback != null)
								{
									callback.call(permissions);
								}
							});
						}
					}
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
}
