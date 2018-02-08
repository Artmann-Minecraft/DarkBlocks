package net.darkblocks.core.bungee.permissions.utils;

/**
 * Created by LartyHD on 06.10.2017  22:15.
 */
public class Permissions
{
	/*private ArrayList<String> getPermissions(int type, Object name)
	{
		try
		{
			ArrayList<String> perissions = new ArrayList<>();
			ResultSet perms = Saves.getMySQL().query("SELECT * FROM `Permissions` WHERE `name` = '" + name + "' AND `name_type` = '" + type + "'");
			while (perms.next())
			{
				perissions.add(perms.getString("permission"));
			}
			return perissions;
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	private int getInherit(int groupID)
	{
		if (!existsGroupInDatabase(groupID))
		{
			return 0;
		}
		try
		{
			ResultSet resultSet = Saves.getMySQL().query("SELECT `inheritance` FROM Groups WHERE `id` = '" + groupID + "'");
			if (resultSet.next())
			{
				return resultSet.getInt("inheritance");
			}
			return 0;
		} catch (SQLException ex)
		{
			ex.printStackTrace();
			return 0;
		}
	}
	
	private boolean existsGroupInDatabase(int id)
	{
		try
		{
			return Saves.getMySQL().query("SELECT `name` FROM Groups WHERE `id` = '" + id + "'").next();
		} catch (SQLException ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
	
	private Map<String, Boolean> loadPerms(int group)
	{
		Map<String, Boolean> oldPerms = new HashMap<>();
		int s = getInherit(group);
		if (s > 0)
		{
			oldPerms.putAll(loadPerms(s));
		}
		return sortPerms(getPermissions(0, group), oldPerms);
	}
	
	public Map<String, Boolean> loadPerms(UUID uuid)
	{
		return sortPerms(getPermissions(1, uuid), loadPerms(getGroup(uuid)));
	}
	
	private Map<String, Boolean> sortPerms(ArrayList<String> unsafePerms, Map<String, Boolean> oldPermissions)
	{
		for (String perm : unsafePerms)
		{
			if (perm.startsWith("-"))
			{
				oldPermissions.put(perm.substring(1), false);
				continue;
			}
			oldPermissions.put(perm, true);
		}
		return oldPermissions;
	}
	
	private void loadPermissions()
	{
		for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers())
		{
			loadPermissions(players);
		}
	}
	
	public void loadPermissions(ProxiedPlayer player)
	{
		Map<String, Boolean> userPermissions = loadPerms(player.getUniqueId());
		for (String permissions : userPermissions.keySet())
		{
			player.setPermission(permissions, userPermissions.get(permissions));
		}
	}
	
	private int getGroup(UUID uuid)
	{
		try
		{
			ResultSet resultSet = Saves.getMySQL().query("SELECT `Group` FROM Userdata WHERE `UUID` = '" + uuid.toString() + "'");
			if (resultSet.next())
			{
				return resultSet.getInt("Group");
			}
		} catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		return 0;
	}*/
}
