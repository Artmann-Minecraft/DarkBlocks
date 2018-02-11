package net.darkblocks.core.universal.permissions.manager;

import lombok.Getter;
import net.darkblocks.core.universal.permissions.utils.Group;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.universal.messages.ChatColor;
import net.md_5.bungee.BungeeCord;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

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
		mySQL.update("CREATE TABLE IF NOT EXISTS " + tableName + "(`saveid` INT, `sortid` INT, `name` VARCHAR(16), `prefix` VARCHAR(16), `suffix` VARCHAR(16), `color` VARCHAR(13), `inherit` TEXT, PRIMARY KEY(saveid))");
		mySQL.query("SELECT * FROM `" + tableName + "`", result ->
		{
			try
			{
				while (result.next())
				{
					Set<String> rawInherit = (Set<String>) result.getObject("inherit");
					Set<Integer> inherit = new HashSet<>();
					if (rawInherit != null)
					{
						for (String s : rawInherit)
						{
							inherit.add(Integer.valueOf(s));
						}
					}
					try
					{
						Group group = new Group(new HashSet<>(), inherit, result.getString("name"), result.getString("prefix"), result.getString("suffix"), ChatColor.valueOf(result.getString("color")), result.getInt("saveid"), result.getInt("sortid"));
						getGroups().add(group);
						for (Group groups : getGroups())
						{
							for (Integer integer : inherit)
							{
								if (groups.getSortID() == integer)
								{
									group.getPermissions().addAll(groups.getPermissions());
								}
							}
						}
					} catch (IllegalArgumentException ex)
					{
						ex.printStackTrace();
					}
				}
				mySQL.query("SELECT * FROM `Permissions`", result1 ->
				{
					try
					{
						while (result1.next())
						{
							if (result1.getInt("type") == 0)
							{
								for (Group group : getGroups())
								{
									if (Integer.valueOf(result1.getString("name")) == group.getSaveID())
									{
										group.getPermissions().add(result1.getString("permission"));
									}
								}
							}
						}
					} catch (SQLException ex)
					{
						ex.printStackTrace();
					}
				});
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
				System.out.println(getGroups());
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
}
