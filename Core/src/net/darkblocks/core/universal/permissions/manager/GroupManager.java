package net.darkblocks.core.universal.permissions.manager;

import lombok.Getter;
import net.darkblocks.core.universal.permissions.utils.Group;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.universal.messages.ChatColor;
import net.md_5.bungee.BungeeCord;

import java.sql.SQLException;
import java.util.*;

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
		System.out.println("Register groups...");
		System.out.println("Try to create the groups table");
		mySQL.update("CREATE TABLE IF NOT EXISTS " + tableName + "(`saveid` INT, `sortid` INT, `name` VARCHAR(16), `prefix` VARCHAR(16), `suffix` VARCHAR(16), `color` VARCHAR(13), `inherit` TEXT, PRIMARY KEY(saveid))", () -> {
			System.out.println("Tried to create the groups table");
			System.out.println("Select * from the groups table");
			mySQL.query("SELECT * FROM `" + finalTableName + "`", result ->
			{
				System.out.println("Selected * from the groups table");
				try
				{
					System.out.println("Add groups to the collection");
					while (result.next())
					{
						System.out.println("Add group to the collection");
						try
						{
							Set<Integer> inherit = new HashSet<>();
							if (result.getString("inherit") != null)
							{
								Set<String> rawInherit = new HashSet<>(Arrays.asList(result.getString("inherit").split(", ")));
								System.out.println("Add inherits to the collection");
								for (String s : rawInherit)
								{
									System.out.println("Add inherit to the collection");
									inherit.add(Integer.valueOf(s));
									System.out.println("Added inherit to the collection");
								}
								System.out.println("Added inherits to the collection");
							}
							getGroups().add(new Group(new HashSet<>(), inherit, result.getString("name"), result.getString("prefix"), result.getString("suffix"), ChatColor.valueOf(result.getString("color")), result.getInt("saveid"), result.getInt("sortid")));
							System.out.println("Added group to the collection");
						} catch (IllegalArgumentException ex)
						{
							ex.printStackTrace();
						}
					}
					System.out.println("Added groups to the collection");
					System.out.println("Select * from the permissions table");
					mySQL.query("SELECT * FROM `Permissions`", result1 ->
					{
						System.out.println("Selected * from the permissions table");
						try
						{
							System.out.println("Add permissions to the groups");
							while (result1.next())
							{
								if (result1.getInt("type") == 0)
								{
									for (Group group : getGroups())
									{
										if (Integer.valueOf(result1.getString("name")) == group.getSaveID())
										{
											System.out.println("Add permissions to the group " + group);
											group.getPermissions().add(result1.getString("permission"));
											System.out.println("Added permissions to the group " + group);
										}
									}
								}
							}
							System.out.println("Added permissions to the groups");
							System.out.println("Add the inherit permissions to the groups");
							List<Group> groups = new ArrayList<>();
							int highestGroup = Integer.MAX_VALUE;
							System.out.println("Add groups to the collection for the inherit");
							while (groups.size() != this.groups.size())
							{
								System.out.println("Add group to the collection for the inherit");
								highestGroup = addHigherGroup(highestGroup, groups);
								System.out.println("Added group to the collection for the inherit (saveid = " + highestGroup + ")");
							}
							System.out.println("Added groups to the collection for the inherit");
							for (Group group : groups)
							{
								for (Integer integer : group.getInherit())
								{
									for (Group all : getGroups())
									{
										if (all.getSortID() == integer)
										{
											System.out.println("Add the inherit permissions to the group " + group);
											group.getPermissions().addAll(all.getPermissions());
											System.out.println("Added the inherit permissions to the group " + group);
										}
									}
								}
							}
							System.out.println("Added the inherit permissions to the groups");
							System.out.println("Calculate the default group");
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
							System.out.println("Calculated the default group");
						} catch (SQLException ex)
						{
							ex.printStackTrace();
						}
					});
				} catch (SQLException ex)
				{
					ex.printStackTrace();
				}
			});
		});
		System.out.println("Groups " + getGroups());
		System.out.println("Registered groups");
	}
	
	private int addHigherGroup(int highestGroup, List<Group> groups)
	{
		for (Group group : getGroups())
		{
			if (highestGroup < group.getSaveID())
			{
				highestGroup = group.getSaveID();
				groups.add(group);
			}
		}
		return highestGroup;
	}
}
