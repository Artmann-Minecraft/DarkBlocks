package net.darkblocks.core.universal.permissions.manager;

import lombok.Getter;
import net.darkblocks.core.universal.permissions.utils.Group;
import net.darkblocks.dark.java.debug.Debug;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.universal.messages.ChatColor;
import net.md_5.bungee.BungeeCord;

import java.sql.SQLException;
import java.util.Arrays;
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
		Debug.print(" ");
		Debug.print(" ");
		Debug.print("Register Constructor " + getClass().getSimpleName() + "(" + getClass().getName() + ")");
		Debug.print(" ");
		this.groups = new HashSet<>();
		tableName = tableName == null ? "Groups" : tableName;
		String finalTableName = tableName;
		Debug.print("Register groups...");
		Debug.print("Try to create the groups table");
		mySQL.update("CREATE TABLE IF NOT EXISTS " + tableName + "(`saveid` INT, `sortid` INT, `name` VARCHAR(16), `prefix` VARCHAR(16), `suffix` VARCHAR(16), `color` VARCHAR(13), `inherit` TEXT, PRIMARY KEY(saveid))", () -> {
			Debug.print("Tried to create the groups table");
			Debug.print("Select * from the groups table");
			mySQL.query("SELECT * FROM `" + finalTableName + "`", result ->
			{
				Debug.print("Selected * from the groups table");
				try
				{
					Debug.print("Add groups to the collection");
					while (result.next())
					{
						Debug.print("Add group to the collection");
						try
						{
							Set<Integer> inherit = new HashSet<>();
							if (result.getString("inherit") != null)
							{
								Set<String> rawInherit = new HashSet<>(Arrays.asList(result.getString("inherit").split(", ")));
								Debug.print("Add inherits to the collection");
								for (String s : rawInherit)
								{
									Debug.print("Add inherit to the collection");
									inherit.add(Integer.valueOf(s));
									Debug.print("Added inherit to the collection");
								}
								Debug.print("Added inherits to the collection");
							}
							getGroups().add(new Group(new HashSet<>(), inherit, result.getString("name"), result.getString("prefix"), result.getString("suffix"), ChatColor.valueOf(result.getString("color")), result.getInt("saveid"), result.getInt("sortid")));
							Debug.print("Added group to the collection");
						} catch (IllegalArgumentException ex)
						{
							ex.printStackTrace();
						}
					}
					Debug.print("Added groups to the collection");
					Debug.print("Select * from the permissions table");
					mySQL.query("SELECT * FROM `Permissions`", result1 ->
					{
						Debug.print("Selected * from the permissions table");
						try
						{
							Debug.print("Add permissions to the groups");
							while (result1.next() && result1.getInt("type") == 0)
							{
								for (Group group : getGroups())
								{
									if (Integer.valueOf(result1.getString("name")) == group.getSaveID())
									{
										Debug.print("Add permissions to the group " + group);
										group.getPermissions().add(result1.getString("permission"));
										Debug.print("Added permissions to the group " + group);
									}
								}
							}
							Debug.print("Added permissions to the groups");
							Debug.print("Add the inherit permissions to the groups");
							mySQL.query("SELECT * FROM `Permissions`", result2 -> {
								try
								{
									while (result2.next() && result2.getInt("type") == 0)
									{
										for (Group group : getGroups())
										{
											Set<Integer> inherit = group.getInherit();
											for (Integer integer : inherit)
											{
												if (Integer.valueOf(result2.getString("name")).equals(integer))
												{
													Debug.print("Add the inherit permissions to the group " + group);
													group.getPermissions().add(result2.getString("permission"));
													Debug.print("Added the inherit permissions to the group " + group);
												}
											}
										}
									}
									Debug.print("Added the inherit permissions to the groups");
									Debug.print("Calculate the default group");
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
									Debug.print("Calculated the default group");
									System.out.println(getGroups());
									Debug.print("Groups " + getGroups());
									Debug.print("Registered groups");
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
				} catch (SQLException ex)
				{
					ex.printStackTrace();
				}
			});
		});
		Debug.print(" ");
		Debug.print("Registered Constructor " + getClass().getSimpleName() + "(" + getClass().getName() + ")");
		Debug.print(" ");
		Debug.print(" ");
	}
}
