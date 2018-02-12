package net.darkblocks.core.universal.permissions.manager;

import lombok.Getter;
import net.darkblocks.core.universal.permissions.utils.User;
import net.darkblocks.dark.java.mysql.MySQL;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by LartyHD on 08.02.2018 01:40.
 */
@Getter
public class UserManager
{
	private final Set<User> user;
	
	public UserManager(MySQL mySQL, String tableName)
	{
		this.user = new HashSet<>();
		mySQL.update("CREATE TABLE IF NOT EXISTS " + (tableName == null ? "Userdata" : tableName) + "(`uuid` VARCHAR(36), `groups` TEXT, `firstonline` VARCHAR(19), `lastonline` VARCHAR(19), `prefix` VARCHAR(16), `suffix` VARCHAR(16), PRIMARY KEY(uuid))");
	}
	
	public boolean hasPermission(UUID uuid, String permision)
	{
		for (User user : getUser())
		{
			if (user.getUuid() == uuid)
			{
				Set<String> permisions = user.getPermissions();
				if (permision != null)
				{
					if (permisions.contains("*"))
					{
						return true;
					}
					else if (permision.endsWith("*"))
					{
						permision = permision.substring(0, permision.length() - 1);
						for (String s : permisions)
						{
							if (s.startsWith(permision))
							{
								return true;
							}
						}
					}
					else if (permisions.contains(permision))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}
