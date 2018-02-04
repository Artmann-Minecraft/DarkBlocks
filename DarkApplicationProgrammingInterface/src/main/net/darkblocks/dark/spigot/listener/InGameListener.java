package net.darkblocks.dark.spigot.listener;

import lombok.Getter;
import lombok.Setter;
import net.darkblocks.dark.java.utils.ServerState;
import net.darkblocks.dark.spigot.controller.GameController;
import net.darkblocks.dark.spigot.events.ServerStateChangeEvent;
import net.darkblocks.dark.spigot.team.SpectatorManager;
import net.darkblocks.dark.universal.messages.Colors;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;

/**
 * Created by LartyHD on 29.11.2017  14:06.
 */
@Getter
@Setter
public class InGameListener implements Listener
{
	private final String prefix;
	private final GameController gameController;
	private final HashMap<String, Player> killer;
	private SpectatorManager spectatorManager;
	
	public InGameListener(String prefix, GameController gameController, SpectatorManager spectatorManager)
	{
		this.prefix = prefix;
		this.gameController = gameController;
		this.spectatorManager = spectatorManager;
		this.killer = new HashMap<>();
		gameController.registerListener(this);
	}
	
	@EventHandler
	public void onServerStateChange(ServerStateChangeEvent event)
	{
		if (event.getNewServerState() != ServerState.INGAME)
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
		event.setQuitMessage(null);
		//TODO: CHECK WIN???
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event)
	{
		event.setLeaveMessage(null);
		//TODO: CHECK WIN???
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		if (player.getLocation().getBlockY() < 0)
		{
			player.damage(player.getHealth());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		String playerName = player.getDisplayName();
		Player killer = getKiller().get(playerName);
		if (playerName != null)
		{
			/*TeamManager teamManager = Saves.getTeamManager(); TODO: ADD TEAMS
			if (teamManager != null)
			{
				GameTeam gameTeam = teamManager.getTeam(player);
				if (gameTeam != null)
				{
					ChatColor playerTeamChatColor = gameTeam.getChatColor();
					if (killer == null)
					{
						event.setDeathMessage(this.prefix + TEXT + playerTeamChatColor + playerName + TEXT + " ist gestorben");
					}
					else
					{
						event.setDeathMessage(this.prefix + playerTeamChatColor + playerName + TEXT + " wurde von " + teamManager.getTeam(killer).getChatColor() + killer.getName() + TEXT + " getötet");
					}
				}
			}
			else*//*if (UserManager.getUser(player) != null) TODO: USER PREFIX
			{
				String userPrefix = UserManager.getUser(player).getPrefix();
				if (killer == null)
				{
					event.setDeathMessage(this.prefix + TEXT + userPrefix + playerName + TEXT + " ist gestorben");
				}
				else
				{
					event.setDeathMessage(this.prefix + TEXT + userPrefix + playerName + TEXT + " wurde von " + UserManager.getUser(killer).getPrefix() + killer.getName() + TEXT + " getötet");
				}
			}
			else
			{*/
			if (killer == null)
			{
				event.setDeathMessage(this.prefix + Colors.IMPORTANT + playerName + Colors.TEXT + " ist gestorben");
			}
			else
			{
				event.setDeathMessage(this.prefix + Colors.IMPORTANT + playerName + Colors.TEXT + " wurde von " + Colors.IMPORTANT + killer.getDisplayName() + Colors.TEXT + " getötet");
			}
			/*}*/
		}
		getKiller().remove(player.getName());
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event)
	{
		Entity damager = event.getDamager();
		if (!event.isCancelled() && damager != null)
		{
			String name = event.getEntity().getName();
			if (damager instanceof Player)
			{
				getKiller().put(name, (Player) damager);
			}
			else if (damager instanceof Projectile)
			{
				ProjectileSource shooter = ((Projectile) damager).getShooter();
				if (shooter instanceof Player)
				{
					getKiller().put(name, (Player) shooter);
				}
			}
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
//		if (Saves.getTeamManager() != null) TODO: TEAMS
//		{
//			event.setCancelled(true);
//			if (Saves.getTeamManager().getSpectators().contains(player))
//			{
//				for (Player spec : Saves.getTeamManager().getSpectators().getPlayers())
//				{
//					spec.sendMessage(UserManager.getUser(player).getPrefix() + player.getName() + IMPORTANT + ": §f" + event.getMessage());
//				}
//			}
//			else
//			{
//				for (Player others : Bukkit.getOnlinePlayers())
//				{
//					if (!Saves.getTeamManager().getSpectators().contains(others))
//					{
//						if (!event.getMessage().startsWith("@"))
//						{
//							if (Saves.getTeamManager().getTeam(player).getPlayers().contains(others))
//							{
//								others.sendMessage(IMPORTANT + "[" + Saves.getTeamManager().getTeam(player).getChatColor() + Saves.getTeamManager().getTeam(player).getName() + IMPORTANT + "] " + Saves.getTeamManager().getTeam(player).getChatColor() + player.getName() + IMPORTANT + ": §f" + event.getMessage());
//							}
//						}
//						else
//						{
//							if (Saves.isTeamChat())
//							{
//								String message = null;
//								if (event.getMessage().startsWith("@all"))
//								{
//									message = event.getMessage().substring(4);
//								}
//								else if (event.getMessage().startsWith("@al"))
//								{
//									message = event.getMessage().substring(3);
//								}
//								else if (event.getMessage().startsWith("@a"))
//								{
//									message = event.getMessage().substring(2);
//								}
//								else if (event.getMessage().startsWith("@"))
//								{
//									message = event.getMessage().substring(1);
//								}
//								if (message == null || message.equalsIgnoreCase("") || message.equalsIgnoreCase(" "))
//								{
//									return;
//								}
//								if (message.startsWith(" "))
//								{
//									message.substring(1);
//								}
//								others.sendMessage(IMPORTANT + "[" + TEXT + "@all" + IMPORTANT + "] " + "[" + Saves.getTeamManager().getTeam(player).getChatColor() + Saves.getTeamManager().getTeam(player).getName() + IMPORTANT + "] " + Saves.getTeamManager().getTeam(player).getChatColor() + player.getName() + IMPORTANT + ": §f" + message);
//							}
//						}
//					}
//				}
//			}
//		}
//		else
//		{
		event.setFormat(player.getDisplayName() + Colors.IMPORTANT + ": §f" + event.getMessage());
//		}
	}
}
