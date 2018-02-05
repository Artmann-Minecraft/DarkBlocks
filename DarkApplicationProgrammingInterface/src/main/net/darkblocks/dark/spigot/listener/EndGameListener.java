package net.darkblocks.dark.spigot.listener;

import lombok.Getter;
import net.darkblocks.dark.java.utils.ServerState;
import net.darkblocks.dark.spigot.controller.GameController;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import net.darkblocks.dark.spigot.events.ServerStateChangeEvent;
import net.darkblocks.dark.universal.messages.Colors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by LartyHD on 29.11.2017  14:06.
 */
@Getter
public class EndGameListener implements Listener
{
	private final String prefix;
	private final GameController gameController;
	private final Location lobbyLocation;
	
	public EndGameListener(String prefix, GameController gameController, Location lobbyLocation)
	{
		this.prefix = prefix;
		this.gameController = gameController;
		this.lobbyLocation = lobbyLocation;
		getGameController().registerListener(this);
	}
	
	@EventHandler
	public void onServerStateChange(ServerStateChangeEvent event)
	{
		if (event.getNewServerState() != ServerState.ENDGAME)
		{
			HandlerList.unregisterAll(this);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		event.setJoinMessage(this.prefix + Colors.IMPORTANT + event.getPlayer().getDisplayName() + Colors.TEXT + " hat die Runde betreten");
		event.getPlayer().teleport(getLobbyLocation());
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
	{
		if (Bukkit.getOnlinePlayers().size() - 1 <= 0)
		{
			Bukkit.shutdown();
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		event.setQuitMessage(this.prefix + Colors.IMPORTANT + event.getPlayer().getDisplayName() + Colors.TEXT + " hat die Runde verlassen");
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event)
	{
		event.setLeaveMessage(this.prefix + Colors.IMPORTANT + event.getPlayer().getDisplayName() + Colors.TEXT + " hat die Runde verlassen");
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		if (player.getLocation().getBlockY() < 0 && getLobbyLocation() != null)
		{
			player.teleport(getLobbyLocation());
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		event.setDeathMessage(null);
		event.setKeepInventory(true);
	}
	
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
	{
		event.setMessage(event.getPlayer().getDisplayName() + Colors.IMPORTANT + ": §f" + event.getMessage());
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		event.setRespawnLocation(getLobbyLocation());
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event)
	{
		ItemStack item = event.getItem();
		if (item != null)
		{
			Player player = event.getPlayer();
			if (item.getType() == Material.SKULL_ITEM)
			{
				ItemMeta itemMeta = item.getItemMeta();
				if (itemMeta != null)
				{
					String displayName = itemMeta.getDisplayName();
					if (displayName != null && displayName.equalsIgnoreCase(Colors.SECONDARY + "Zurück zur Lobby"))
					{
						player.kickPlayer("LEAVE");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBurn(BlockBurnEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockForm(BlockFormEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockFromTo(BlockFromToEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockGrow(BlockGrowEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event)
	{
		event.setCancelled(true);
	}
}
