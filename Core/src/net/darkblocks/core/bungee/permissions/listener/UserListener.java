package net.darkblocks.core.bungee.permissions.listener;

import lombok.Getter;
import net.darkblocks.core.universal.permissions.manager.GroupManager;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.core.universal.permissions.utils.User;
import net.darkblocks.core.universal.permissions.utils.UserUtils;
import net.darkblocks.dark.java.mysql.MySQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.Set;

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
			UserUtils.onLogin(this.mySQL, connection.getUniqueId(), this.userManager, this.groupManager, null);
		}
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
	{
		UserUtils.onDisconnect(this.mySQL, event.getPlayer().getUniqueId(), this.userManager);
	}
	
	@EventHandler
	public void onPermissionCheckEvent(PermissionCheckEvent event)
	{
		CommandSender sender = event.getSender();
		if (sender instanceof ProxiedPlayer)
		{
			ProxiedPlayer player = (ProxiedPlayer) sender;
			for (User user : getUserManager().getUser())
			{
				if (user.getUuid() == player.getUniqueId())
				{
					Set<String> permisions = user.getPermissions();
					if (permisions.contains("*"))
					{
						event.setHasPermission(true);
						return;
					}
					else
					{
						String permision = event.getPermission();
						if (permision != null && permision.endsWith("*"))
						{
							permision = permision.substring(0, permision.length() - 1);
							for (String s : permisions)
							{
								if (s.startsWith(permision))
								{
									event.setHasPermission(true);
									return;
								}
							}
						}
					}
				}
			}
		}
	}
}
