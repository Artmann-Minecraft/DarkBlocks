package net.darkblocks.core.spigot.permissions.listener;

import lombok.Getter;
import net.darkblocks.core.universal.permissions.manager.GroupManager;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.core.universal.permissions.utils.UserUtils;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 08.02.2018  01:44.
 */
@Getter
public class UserListener implements Listener
{
	private final UserManager userManager;
	private final GroupManager groupManager;
	private final MySQL mySQL;
	
	public UserListener(JavaPlugin javaPlugin, MySQL mySQL, UserManager userManager, GroupManager groupManager)
	{
		this.userManager = userManager;
		this.groupManager = groupManager;
		this.mySQL = mySQL;
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
	
	@EventHandler
	public void onPlayerLoginEvent(PlayerLoginEvent event)
	{
		UserUtils.onLogin(this.mySQL, event.getPlayer().getUniqueId(), this.userManager, this.groupManager);
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
	{
		UserUtils.onDisconnect(this.mySQL, event.getPlayer().getUniqueId(), this.userManager);
	}
}
