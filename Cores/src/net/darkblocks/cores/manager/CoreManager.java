package net.darkblocks.cores.manager;

import net.darkblocks.cores.utils.Core;
import net.darkblocks.dark.java.utils.ServerState;
import net.darkblocks.dark.spigot.controller.GameController;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import net.darkblocks.dark.spigot.events.ServerStateChangeEvent;
import net.darkblocks.dark.spigot.team.GameTeam;
import net.darkblocks.dark.spigot.team.SpectatorManager;
import net.darkblocks.dark.spigot.team.TeamManager;
import net.darkblocks.dark.spigot.utils.MapsUtils;
import net.darkblocks.dark.spigot.utils.PackageUtils;
import net.darkblocks.dark.universal.messages.Messages;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

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
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				try
				{
					while (gameController.getServerState() == ServerState.INGAME)
					{
						for (Core core : cores)
						{
							for (Player players : Bukkit.getOnlinePlayers())
							{
								if (core.getGameTeam() != teamManager.getTeam(players) && players.getLocation().distance(core.getLocation()) <= 5)
								{
									new BukkitRunnable()
									{
										@Override
										public void run()
										{
											players.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 2), true);
										}
									}.runTask(javaPlugin);
								}
							}
						}
						Thread.sleep(1000);
					}
				} catch (InterruptedException ex)
				{
					ex.printStackTrace();
				}
			}
		}.runTaskAsynchronously(javaPlugin);
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
							players.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + IMPORTANT + core.getGameTeam().getChatColor() + core.getName() + TEXT + " wurde von " + IMPORTANT + team.getChatColor() + player.getName() + TEXT + " zerstört");
							PackageUtils.sendTitle(players, core.getGameTeam().getChatColor() + core.getName(), TEXT + "wurde von " + team.getChatColor() + player.getName() + TEXT + " zerstört", 5, 40, 5);
						}
						block.setType(Material.AIR);
						checkWin();
					}
					else
					{
						player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du darfst dein eigenen " + IMPORTANT + core.getName() + TEXT + " nicht abbauen");
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
			if (block != null && block.getType() == Material.BEACON)
			{
				GameTeam gameTeam = core.getGameTeam();
				cores.merge(gameTeam, 1, (a, b) -> a + b);
			}
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
			Bukkit.broadcastMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Das Team " + IMPORTANT + winnerTeam.getChatColor() + winnerTeam.getName() + TEXT + " hat gewonnen");
			Bukkit.getPluginManager().callEvent(new ServerStateChangeEvent(ServerState.INGAME, ServerState.ENDGAME));
		}
	}
}
