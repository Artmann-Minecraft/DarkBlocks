package net.darkblocks.core.bungee.permissions.manager;

import lombok.Getter;
import net.darkblocks.core.bungee.permissions.utils.Group;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.universal.messages.ChatColor;

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
	private final Group defaultGroup;
	
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
					for (String s : rawInherit)
					{
						inherit.add(Integer.valueOf(s));
					}
					getGroups().add(new Group(new HashSet<>(), inherit, result.getString("name"), result.getString("prefix"), result.getString("suffix"), ChatColor.valueOf(result.getString("color")), result.getInt("saveid"), result.getInt("sortid")));
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
		mySQL.query("SELECT * FROM `Permissions`", result ->
		{
			try
			{
				while (result.next())
				{
					if (result.getInt("type") == 0)
					{
						for (Group group : getGroups())
						{
							if (Integer.valueOf(result.getString("name")) == group.getSaveID())
							{
								group.getPermissions().add(result.getString("permission"));
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
	}
}
