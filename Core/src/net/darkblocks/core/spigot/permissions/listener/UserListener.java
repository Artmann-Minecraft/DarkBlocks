/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.core.spigot.permissions.listener;

import lombok.Getter;
import net.darkblocks.core.spigot.permissions.events.PlayerPermissionsLoadedEvent;
import net.darkblocks.core.universal.permissions.manager.GroupManager;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.core.universal.permissions.utils.User;
import net.darkblocks.core.universal.permissions.utils.UserUtils;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LartyHD on 08.02.2018  01:44.
 */
@Getter
public class UserListener implements Listener
{
	private final UserManager userManager;
	private final GroupManager groupManager;
	private final MySQL mySQL;
	private final Map<UUID, DarkPermissible> permissibles = new HashMap<>();
	private final PermissibleInjector injector;
	
	public UserListener(JavaPlugin javaPlugin, MySQL mySQL, UserManager userManager, GroupManager groupManager)
	{
		this.userManager = userManager;
		this.groupManager = groupManager;
		this.mySQL = mySQL;
		this.injector = new PermissibleInjector("org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity", "perm", false);
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
	
	private void inject(Player player)
	{
		try
		{
			this.permissibles.put(player.getUniqueId(), new DarkPermissible(player));
			this.injector.inject(player, this.permissibles.get(player.getUniqueId()));
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerLoginEvent(PlayerLoginEvent event)
	{
		final Player player = event.getPlayer();
		UserUtils.onLogin(this.mySQL, player.getUniqueId(), this.userManager, this.groupManager, () -> {
			inject(player);
			User user = UserManager.getUser(player.getUniqueId());
			if (user != null)
			{
				this.permissibles.get(player.getUniqueId()).addPermissions(user.getPermissions());
			}
			Bukkit.getPluginManager().callEvent(new PlayerPermissionsLoadedEvent(player, user));
		});
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
	{
		UserUtils.onDisconnect(this.mySQL, event.getPlayer().getUniqueId(), this.userManager);
	}
}
