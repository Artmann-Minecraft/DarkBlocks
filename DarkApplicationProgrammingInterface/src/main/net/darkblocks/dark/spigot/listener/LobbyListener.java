package net.darkblocks.dark.spigot.listener;

import lombok.Getter;
import net.darkblocks.dark.java.utils.ServerState;
import net.darkblocks.dark.spigot.controller.GameController;
import net.darkblocks.dark.spigot.countdowns.LobbyCountdown;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import net.darkblocks.dark.spigot.events.ServerStateChangeEvent;
import net.darkblocks.dark.universal.messages.Colors;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.*;
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
public abstract class LobbyListener implements Listener
{
	private final GameController gameController;
	private final Location lobbyLocation;
	
	public LobbyListener(GameController gameController, Location lobbyLocation)
	{
		this.gameController = gameController;
		this.lobbyLocation = lobbyLocation;
		getGameController().registerListener(this);
		World world = lobbyLocation.getWorld();
		world.setSpawnLocation(lobbyLocation.getBlockX(), lobbyLocation.getBlockY(), lobbyLocation.getBlockZ());
		world.setTime(6000);
		world.setGameRuleValue("spawnRadius", "0");
		world.setGameRuleValue("doDaylightCycle", "false");
		world.setGameRuleValue("doMobSpawning", "false");
		world.setGameRuleValue("doFireTick", "false");
		world.setWeatherDuration(-1);
		world.setThundering(false);
		world.setStorm(false);
		world.setAutoSave(false);
		world.setDifficulty(Difficulty.PEACEFUL);
	}
	
	@EventHandler
	public void onServerStateChange(ServerStateChangeEvent event)
	{
		ServerState serverState = event.getNewServerState();
		if (serverState != ServerState.LOBBY && serverState != ServerState.LOBBYFULL)
		{
			HandlerList.unregisterAll(this);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		event.setJoinMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.IMPORTANT + player.getDisplayName() + Colors.TEXT + " hat die Runde betreten");
		player.teleport(getLobbyLocation());
		setJoinItems(player);
		for (LobbyCountdown lobbyCountdown : getGameController().getLobbyCountdowns())
		{
			lobbyCountdown.idle();
		}
		if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers() && getGameController().getServerState() == ServerState.LOBBY)
		{
			getGameController().setServerState(ServerState.LOBBYFULL);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		event.setQuitMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.IMPORTANT + event.getPlayer().getDisplayName() + Colors.TEXT + " hat die Runde verlassen");
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event)
	{
		event.setLeaveMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.IMPORTANT + event.getPlayer().getDisplayName() + Colors.TEXT + " hat die Runde verlassen");
	}
	
	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent event)
	{
		int size = Bukkit.getOnlinePlayers().size();
		if (size <= Bukkit.getMaxPlayers() && getGameController().getServerState() == ServerState.LOBBYFULL)
		{
			getGameController().setServerState(ServerState.LOBBY);
		}
		else if (size <= 0)
		{
			for (LobbyCountdown lobbyCountdown : getGameController().getLobbyCountdowns())
			{
				lobbyCountdown.stopIdle();
			}
		}
//		Utils.refreshPing(Bukkit.getOnlinePlayers().size() - 1);TODO: CLOUD!
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
	public void onPlayerInteractEvent(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
		{
			ItemStack item = event.getItem();
			if (item != null)
			{
				Player player = event.getPlayer();
				if (item.getType() == Material.ENDER_CHEST)
				{
					player.updateInventory();
					this.gameController.getLobbyCountdowns().forEach(countdown -> {
						if (countdown.getSeconds() > 10)
						{
							player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Du kannst erst in den letzten " + Colors.IMPORTANT + "10 Sekunden" + Colors.TEXT + " dein " + Colors.IMPORTANT + "Team" + Colors.TEXT + " wählen");
						}
					});
				}
				else if (item.getType() == Material.SKULL_ITEM)
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
	
	protected abstract void setJoinItems(Player player);
}