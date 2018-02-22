/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.gungame.listener;

import lombok.Getter;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.core.universal.permissions.utils.User;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.mysql.StatsAPI;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import net.darkblocks.dark.spigot.utils.Items;
import net.darkblocks.dark.spigot.utils.LocationUtils;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.gungame.kits.manager.KitManager;
import net.darkblocks.gungame.shop.manager.ShopManager;
import net.darkblocks.gungame.utils.ScoreBoardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 17.02.2018  15:32.
 */
@Getter
public class InGameListener extends net.darkblocks.dark.spigot.listener.InGameListener
{
	private final Map<String, Integer> killStreak;
	private final Set<String> useHealer;
	private final KitManager kitManager;
	private final Location location;
	private final StatsAPI statsAPI;
	private final CoinsAPI coinsAPI;
	private final Random random;
	private final String map;
	private final JavaPlugin javaPlugin;
	private final boolean allowTeams;
	
	public InGameListener(JavaPlugin javaPlugin, KitManager kitManager, Location location, StatsAPI statsAPI, CoinsAPI coinsAPI, String map)
	{
		super(javaPlugin);
		this.javaPlugin = javaPlugin;
		this.killStreak = new HashMap<>();
		this.useHealer = new HashSet<>();
		this.kitManager = kitManager;
		this.location = location;
		this.statsAPI = statsAPI;
		this.coinsAPI = coinsAPI;
		this.random = new Random();
		this.map = map;
		this.allowTeams = this.random.nextBoolean();
		new ShopManager(javaPlugin, this);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		Material type = event.getTo().getBlock().getType();
		if (type == Material.WATER || type == Material.STATIONARY_WATER || type == Material.LAVA || type == Material.STATIONARY_LAVA)
		{
			player.damage(player.getMaxHealth() + 100);
		}
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		UUID uniqueId = player.getUniqueId();
		getStatsAPI().createAccount(uniqueId);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.getInventory().setItem(8, Items.LEAVE.getItemStack());
		player.setHealth(player.getMaxHealth());
		player.teleport(LocationUtils.randomLook(this.location));
		getKillStreak().put(player.getName(), 0);
		getKitManager().getPlayer().put(player.getName(), 0);
		getKitManager().update(player);
		User user = UserManager.getUser(uniqueId);
		ScoreBoardUtils.sendInGameScoreBoard(player, getMap(), getStatsAPI(), this.allowTeams);
		if (user != null)
		{
			ScoreBoardUtils.sendTab(user, player);
			event.setJoinMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + IMPORTANT + user.getPrefix() + player.getDisplayName() + user.getSuffix() + TEXT + " hat die Runde betreten");
		}
		for (Player players : Bukkit.getOnlinePlayers())
		{
			if (!(player.getName().equalsIgnoreCase(players.getName())))
			{
				players.hidePlayer(player);
				players.showPlayer(player);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
	{
		getKitManager().getPlayer().remove(event.getPlayer().getName());
		getKillStreak().remove(event.getPlayer().getName());
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event)
	{
		User user = UserManager.getUser(event.getPlayer().getUniqueId());
		if (user != null)
		{
			event.setQuitMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + IMPORTANT + user.getPrefix() + event.getPlayer().getDisplayName() + user.getSuffix() + event.getPlayer().getDisplayName() + TEXT + " hat die Runde verlassen");
		}
	}
	
	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent event)
	{
		User user = UserManager.getUser(event.getPlayer().getUniqueId());
		if (user != null)
		{
			event.setLeaveMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + IMPORTANT + user.getPrefix() + event.getPlayer().getDisplayName() + user.getSuffix() + event.getPlayer().getDisplayName() + TEXT + " hat die Runde verlassen");
		}
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		event.setKeepInventory(true);
	}
	
	@EventHandler
	public void on(PlayerInteractEvent event)
	{
		event.setCancelled(true);
		if (event.getItem() != null && event.getItem().equals(Items.LEAVE.getItemStack()))
		{
			event.getPlayer().kickPlayer("LEAVE");
		}
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event)
	{
		event.setRespawnLocation(LocationUtils.randomLook(this.location));
		Player player = event.getPlayer();
		Player killer = player.getKiller();
		player.playSound(player.getLocation(), Sound.GHAST_DEATH, 2, 1);
		getKillStreak().put(player.getName(), 0);
		getStatsAPI().add(player.getUniqueId(), 1, "tode", () -> getStatsAPI().remove(player.getUniqueId(), 5, "Punkte", () -> getStatsAPI().get(player.getUniqueId(), "Punkte", result -> {
			if (result < 0)
			{
				getStatsAPI().set(player.getUniqueId(), 0, "Punkte");
			}
			ScoreBoardUtils.sendInGameScoreBoard(player, getMap(), getStatsAPI(), this.allowTeams);
			if (killer != null && !killer.getUniqueId().equals(player.getUniqueId()))
			{
				int killStreak = getKillStreak().get(killer.getName());
				getKillStreak().put(killer.getName(), killStreak + 1);
				getStatsAPI().get(killer.getUniqueId(), "MaxKillStreak", result1 -> {
					if (killStreak + 1 > result1)
					{
						getStatsAPI().set(killer.getUniqueId(), killStreak + 1, "MaxKillStreak");
					}
					getStatsAPI().add(killer.getUniqueId(), 1, "Kills");
					getStatsAPI().add(killer.getUniqueId(), 10, "Punkte");
					killer.sendMessage(getCoinsAPI().addCoins(killer.getUniqueId(), "5", result2 -> {
						broadcastKillStreak(getKillStreak().get(killer.getName()), killer);
						ScoreBoardUtils.sendInGameScoreBoard(killer, getMap(), getStatsAPI(), this.allowTeams);
						killer.playSound(killer.getLocation(), Sound.ENDERMAN_HIT, 2, 1);
						getKitManager().update(killer);
						healPlayer(killer);
					}));
				});
			}
		})));
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event)
	{
		if (event.getClickedInventory() != null && event.getClickedInventory().equals(event.getInventory()))
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event)
	{
		if (event.getCause() == EntityDamageEvent.DamageCause.FALL)
		{
			event.setCancelled(true);
		}
	}
	
	public void healPlayer(Player player)
	{
		if (player.getHealth() != 0 && !getUseHealer().contains(player.getName()))
		{
			new Thread(() ->
			{
				try
				{
					getUseHealer().add(player.getName());
					for (int i = 0; i < 100; i++)
					{
						if (Math.round(player.getHealth() * 100) / 100 >= (int) player.getMaxHealth() - 1)
						{
							player.setHealth(player.getMaxHealth());
							getUseHealer().remove(player.getName());
							return;
						}
						try
						{
							Thread.sleep(50);
						} catch (InterruptedException ex)
						{
							ex.printStackTrace();
						}
						player.setHealth(player.getHealth() + 1);
					}
				} catch (IllegalArgumentException ex)
				{
					ex.printStackTrace();
				} finally
				{
					getUseHealer().remove(player.getName());
				}
			}).start();
		}
	}
	
	private void broadcastKillStreak(Integer killStreak, Player killer)
	{
		switch (killStreak)
		{
			case 5:
			case 10:
			case 15:
			case 20:
			case 25:
			case 30:
			case 35:
			case 40:
			case 45:
			case 50:
			case 60:
			case 70:
			case 80:
			case 90:
			case 100:
			case 125:
			case 150:
			case 200:
			case 250:
			case 500:
			case 1000:
				User user = UserManager.getUser(killer.getUniqueId());
				if (user != null)
				{
					Bukkit.broadcastMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + IMPORTANT + user.getPrefix() + killer.getDisplayName() + user.getSuffix() + TEXT + " hat eine " + IMPORTANT + EXTRA + killStreak + "er KillStreak");
				}
				break;
			default:
				break;
		}
	}
}
