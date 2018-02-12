package net.darkblocks.core.bungee.permissions.listener;

import lombok.Getter;
import net.darkblocks.core.bungee.permissions.events.PermissionsLoadedEvent;
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
			UserUtils.onLogin(this.mySQL, connection.getUniqueId(), this.userManager, this.groupManager, () -> {
				for (User user : this.userManager.getUser())
				{
					if (user.getUuid() == connection.getUniqueId())
					{
						BungeeCord.getInstance().getPluginManager().callEvent(new PermissionsLoadedEvent(connection, user));
						return;
					}
				}
			});
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
			this.userManager.hasPermission(((ProxiedPlayer) sender).getUniqueId(), event.getPermission());
		}
	}
}
