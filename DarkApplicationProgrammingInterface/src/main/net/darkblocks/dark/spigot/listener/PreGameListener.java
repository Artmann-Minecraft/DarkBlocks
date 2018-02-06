package net.darkblocks.dark.spigot.listener;

import lombok.Getter;
import lombok.Setter;
import net.darkblocks.dark.java.utils.ServerState;
import net.darkblocks.dark.spigot.controller.GameController;
import net.darkblocks.dark.spigot.events.ServerStateChangeEvent;
import net.darkblocks.dark.spigot.team.SpectatorManager;
import net.darkblocks.dark.universal.messages.Colors;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.Location;
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

/**
 * Created by LartyHD on 29.11.2017  14:06.
 */
@Getter
@Setter
public class PreGameListener implements Listener
{
	private final GameController gameController;
	private SpectatorManager spectatorManager;
	
	public PreGameListener(GameController gameController, SpectatorManager spectatorManager)
	{
		this.gameController = gameController;
		this.spectatorManager = spectatorManager;
		getGameController().registerListener(this);
	}
	
	@EventHandler
	public void onServerStateChange(ServerStateChangeEvent event)
	{
		if (event.getNewServerState() != ServerState.PREGAME)
		{
			HandlerList.unregisterAll(this);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		event.setJoinMessage(null);
		player.teleport(this.spectatorManager.getSpectators().getLocation());
		this.spectatorManager.add(player);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		event.setQuitMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.IMPORTANT + event.getPlayer().getDisplayName() + Colors.TEXT + " hat die Runde verlassen");
		//TODO: ADD TEAM INFOS
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event)
	{
		event.setLeaveMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.IMPORTANT + event.getPlayer().getDisplayName() + Colors.TEXT + " hat die Runde verlassen");
		//TODO: ADD TEAM INFOS
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Location from = event.getFrom();
		Location to = event.getTo();
		if (to.getBlockX() != from.getBlockX() || from.getBlockZ() != to.getBlockZ())
		{
			event.getPlayer().teleport(new Location(from.getWorld(), from.getX(), from.getY(), from.getZ(), to.getYaw(), to.getPitch()));
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
		event.setRespawnLocation(this.spectatorManager.getSpectators().getLocation());
		//TODO: ADD TEAM SUPPORT
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
