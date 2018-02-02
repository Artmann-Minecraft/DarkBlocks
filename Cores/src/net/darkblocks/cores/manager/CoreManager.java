package net.darkblocks.cores.manager;

import net.craftplugin.craftpluginapi.java.utils.ServerState;
import net.craftplugin.craftpluginapi.spigot.controller.GameController;
import net.craftplugin.craftpluginapi.spigot.events.PlayerDisconnectEvent;
import net.craftplugin.craftpluginapi.spigot.events.ServerStateChangeEvent;
import net.craftplugin.craftpluginapi.spigot.team.GameTeam;
import net.craftplugin.craftpluginapi.spigot.team.SpectatorManager;
import net.craftplugin.craftpluginapi.spigot.team.TeamManager;
import net.craftplugin.craftpluginapi.spigot.utils.MapsUtils;
import net.craftplugin.craftpluginapi.spigot.utils.PackageUtils;
import net.darkblocks.cores.utils.Core;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.craftplugin.craftpluginapi.spigot.messages.Colors.IMPORTANT;
import static net.craftplugin.craftpluginapi.spigot.messages.Colors.TEXT;

/**
 * Created by LartyHD on 03.01.2018  11:26.
 */
public class CoreManager implements Listener
{
	private final List<Core> cores;
	private final TeamManager teamManager;
	private final SpectatorManager spectatorManager;
	
	public CoreManager(JavaPlugin javaPlugin, List<Core> cores, TeamManager teamManager, SpectatorManager spectatorManager, GameController gameController)
	{
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
		this.cores = cores;
		this.teamManager = teamManager;
		this.spectatorManager = spectatorManager;
		/*new BukkitRunnable()
		{
			@Override
			public void run()
			{
				while (gameController.getServerState() == ServerState.INGAME)
				{
					for (Player players : Bukkit.getOnlinePlayers())
					{
						for (Location location : getArenaBlocks(players.getLocation(), 5))
						{
							if (location.getBlock().getType() == Material.BEACON)
							{
								players.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 2), true);
							}
						}
					}
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}.runTask(javaPlugin);*/
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
	{
		checkWin();
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event)
	{
		Block block = event.getBlock();
		if (block.getType() == Material.BEACON && !event.isCancelled())
		{
			for (Core core : this.cores)
			{
				if (MapsUtils.equalsLocation(block.getLocation(), core.getLocation()))
				{
					event.setCancelled(true);
					Player player = event.getPlayer();
					GameTeam team = this.teamManager.getTeam(player);
					if (team != core.getGameTeam())
					{
						for (Player players : Bukkit.getOnlinePlayers())
						{
							players.sendMessage(this.teamManager.getPrefix() + IMPORTANT + core.getGameTeam().getChatColor() + core.getName() + TEXT + " wurde von " + IMPORTANT + team.getChatColor() + player.getName() + TEXT + " zerstört");
							PackageUtils.sendTitle(players, core.getGameTeam().getChatColor() + core.getName(), TEXT + "wurde von " + team.getChatColor() + player.getName() + TEXT + " zerstört", 5, 40, 5);
						}
						block.setType(Material.AIR);
						checkWin();
						return;
					}
					else
					{
						player.sendMessage(this.teamManager.getPrefix() + TEXT + "Du darfst dein eigenen " + IMPORTANT + core.getName() + TEXT + " nicht abbauen");
						return;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event)
	{
		if (event.getBlock().getLocation().add(0, -1, 0).getBlock().getType() == Material.BEACON)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event)
	{
		if (event.getBlock().getLocation().add(0, -1, 0).getBlock().getType() == Material.BEACON)
		{
			event.setCancelled(true);
			event.getEntity().remove();
		}
	}
	
	private List<Location> getArenaBlocks(Location location, int radius)
	{
		World world = location.getWorld();
		List<Location> tempList = new ArrayList<>();
		for (int x = -radius; x <= radius; x++)
		{
			for (int z = -radius; z <= radius; z++)
			{
				for (int y = -radius; y <= radius; y++)
				{
					tempList.add(new Location(world, location.getX() + x, location.getY() + y, location.getZ() + z));
				}
			}
		}
		return tempList;
	}
	
	private void checkWin()
	{
		Map<GameTeam, Integer> cores = new HashMap<>();
		for (Core core : this.cores)
		{
			Block block = core.getLocation().getBlock();
			GameTeam gameTeam = core.getGameTeam();
			Integer size = cores.get(gameTeam);
			cores.put(gameTeam, block == null ? 0 : block.getType() != Material.BEACON ? 0 : size == null ? 1 : size + 1);
		}
		int livingTeams = 0;
		GameTeam winnerTeam = null;
		for (GameTeam gameTeam : cores.keySet())
		{
			if (cores.get(gameTeam) == 0)
			{
				for (Player players : gameTeam.getPlayers())
				{
					this.spectatorManager.add(players);
				}
			}
			else
			{
				livingTeams++;
				winnerTeam = gameTeam;
			}
		}
		if (livingTeams == 1)
		{
			Bukkit.broadcastMessage(this.teamManager.getPrefix() + TEXT + "Das Team " + IMPORTANT + winnerTeam.getChatColor() + winnerTeam.getName() + TEXT + " hat gewonnen");
			Bukkit.getPluginManager().callEvent(new ServerStateChangeEvent(ServerState.INGAME, ServerState.ENDGAME));
		}
	}
}
