/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.dark.spigot.team;

import lombok.Getter;
import lombok.NonNull;
import net.darkblocks.dark.spigot.builder.InventoryBuilder;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import net.darkblocks.dark.spigot.utils.InventoryUtils;
import net.darkblocks.dark.universal.messages.Colors;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by LartyHD on 03.01.2018  12:33.
 */
@Getter
public class SpectatorManager implements Listener
{
	private final Set<Player> players;
	private final GameTeam spectators;
	
	public SpectatorManager(@NonNull JavaPlugin javaPlugin, boolean colored, Location location)
	{
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
		this.players = new HashSet<>();
		this.spectators = new GameTeam("spectators", colored ? ChatColor.DARK_GRAY : ChatColor.GRAY, Integer.MAX_VALUE, colored);
		Team team = this.spectators.getTeam();
		team.setPrefix((colored ? ChatColor.GRAY : ChatColor.DARK_GRAY) + "[" + Colors.PRIMARY + "✘" + (colored ? ChatColor.GRAY : ChatColor.DARK_GRAY) + "] " + (colored ? ChatColor.DARK_GRAY : ChatColor.GRAY));
		team.setCanSeeFriendlyInvisibles(true);
		team.setAllowFriendlyFire(false);
		this.spectators.setLocation(location);
	}
	
	public void add(Player player)
	{
		player.spigot().setCollidesWithEntities(false);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1), true);
		this.players.add(player);
		this.spectators.add(player);
		for (Player players : Bukkit.getOnlinePlayers())
		{
			if (!this.players.contains(players))
			{
				players.hidePlayer(player);
			}
			player.showPlayer(players);
		}
		setSpectatorsSettings(player);
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		event.setJoinMessage(null);
		Bukkit.broadcastMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.IMPORTANT + event.getPlayer().getDisplayName() + Colors.TEXT + " hat die Runde als Spectator betreten");
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event)
	{
		event.setQuitMessage(null);
	}
	
	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent event)
	{
		event.setLeaveMessage(null);
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
	{
		Bukkit.broadcastMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.IMPORTANT + event.getPlayer().getDisplayName() + Colors.TEXT + " hat die Runde als Spectator verlassen");
	}
	
	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event)
	{
		event.setFormat(this.spectators.getTeam().getPrefix() + event.getFormat());
		//TODO: MAKE IT!
	}
	
	@EventHandler
	public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event)
	{
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		if (this.players.contains(player) && entity instanceof Player)
		{
			Player target = (Player) entity;
			PlayerInventory playerInventory = player.getInventory();
			PlayerInventory targetInventory = target.getInventory();
			playerInventory.setArmorContents(targetInventory.getArmorContents());
			playerInventory.setContents(targetInventory.getContents());
			player.setHealth(((Player) entity).getHealth());
			player.setFoodLevel(((Player) entity).getFoodLevel());
			player.updateInventory();
			player.setSpectatorTarget(entity);
		}
	}
	
	@EventHandler
	public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event)
	{
		Player player = event.getPlayer();
		if (!event.isSneaking() && this.players.contains(player))
		{
			setSpectatorsSettings(player);
			player.setSpectatorTarget(null);
		}
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		block(player, event);
		Action action = event.getAction();
		if (this.players.contains(player) && (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR))
		{
			event.setCancelled(true);
			if (event.getMaterial() == Material.COMPASS)
			{
				Inventory inventory = new InventoryBuilder(null, InventoryUtils.getInventorySize(this.players.size()), Colors.SECONDARY + "Teleporter").build();
				for (Player players : Bukkit.getOnlinePlayers())
				{
					if (!this.players.contains(players))
					{
						inventory.addItem(new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setOwner(players.getName()).setName(Colors.SECONDARY + players.getName()).build());
					}
				}
				player.openInventory(inventory);
			}
			else if (event.getMaterial() == Material.SKULL_ITEM)
			{
				player.kickPlayer("LEAVE");
			}
		}
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		if (this.players.contains(player))
		{
			event.setCancelled(true);
			Inventory inventory = event.getInventory();
			if (inventory != null)
			{
				String title = inventory.getTitle();
				if (title != null && title.equalsIgnoreCase(Colors.SECONDARY + "Teleporter"))
				{
					ItemStack currentItem = event.getCurrentItem();
					if (currentItem != null)
					{
						ItemMeta itemMeta = currentItem.getItemMeta();
						if (itemMeta != null)
						{
							String displayName = itemMeta.getDisplayName();
							if (displayName != null)
							{
								Player target = Bukkit.getPlayer(ChatColor.stripColor(displayName));
								if (target != null && !this.spectators.getPlayers().contains(target))
								{
									player.teleport(target);
									player.closeInventory();
								}
								else
								{
									player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Der Spieler ist nicht mehr im Spiel");
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Player)
		{
			block((Player) event.getDamager(), event);
		}
		else if (event.getDamager() instanceof Arrow)
		{
			block((Player) ((Arrow) event.getDamager()).getShooter(), event);
		}
	}
	
	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event)
	{
		block((Player) event.getEntity(), event);
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event)
	{
		block(event.getPlayer(), event);
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event)
	{
		block(event.getPlayer(), event);
	}
	
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event)
	{
		block(event.getPlayer(), event);
	}
	
	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event)
	{
		block(event.getPlayer(), event);
	}
	
	private void block(Player player, Cancellable cancellable)
	{
		if (this.players.contains(player))
		{
			cancellable.setCancelled(true);
		}
	}
	
	private void setSpectatorsSettings(Player player)
	{
		PlayerInventory inventory = player.getInventory();
		inventory.setArmorContents(null);
		inventory.clear();
		inventory.setItem(0, new ItemBuilder(Material.COMPASS).setName(Colors.SECONDARY + "Teleporter").build());
		inventory.setItem(8, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setOwnerFromURL("http://textures.minecraft.net/texture/1b6f1a25b6bc199946472aedb370522584ff6f4e83221e5946bd2e41b5ca13b", "MHF_ArrowRight").setName(Colors.SECONDARY + "Zurück zur Lobby").build());
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(true);
		player.setFlying(true);
		player.setFoodLevel(20);
		player.setSaturation(0);
		player.setHealth(20);
		player.setLevel(0);
		player.setExp(0);
	}
}
