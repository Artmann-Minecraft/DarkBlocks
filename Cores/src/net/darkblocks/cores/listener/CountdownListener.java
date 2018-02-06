package net.darkblocks.cores.listener;

import net.darkblocks.cores.Cores;
import net.darkblocks.cores.manager.CoreManager;
import net.darkblocks.cores.utils.Core;
import net.darkblocks.cores.utils.ScoreBoard;
import net.darkblocks.dark.java.utils.ServerState;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.config.Configuration;
import net.darkblocks.dark.spigot.controller.GameController;
import net.darkblocks.dark.spigot.countdowns.EndGameCountdown;
import net.darkblocks.dark.spigot.countdowns.LobbyCountdown;
import net.darkblocks.dark.spigot.countdowns.PreGameCountdown;
import net.darkblocks.dark.spigot.events.*;
import net.darkblocks.dark.spigot.listener.EndGameListener;
import net.darkblocks.dark.spigot.listener.InGameListener;
import net.darkblocks.dark.spigot.listener.LobbyListener;
import net.darkblocks.dark.spigot.listener.PreGameListener;
import net.darkblocks.dark.spigot.team.GameTeam;
import net.darkblocks.dark.spigot.team.SpectatorManager;
import net.darkblocks.dark.spigot.team.TeamManager;
import net.darkblocks.dark.spigot.utils.MapsUtils;
import net.darkblocks.dark.spigot.vote.VoteManager;
import net.darkblocks.dark.universal.messages.Colors;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 04.01.2018  23:33.
 */
public class CountdownListener implements Listener
{
	private final String prefix;
	private final JavaPlugin javaPlugin;
	private final VoteManager voteManager;
	private final GameController gameController;
	private TeamManager teamManager;
	private SpectatorManager spectatorManager;
	private CoreManager coreManager;
	
	public CountdownListener(String prefix, JavaPlugin javaPlugin)
	{
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
		this.prefix = prefix;
		this.javaPlugin = javaPlugin;
		Set<String> maps = new HashSet<>(MapsUtils.loadMapNames(javaPlugin));
		this.gameController = new GameController(javaPlugin, ServerState.STARTUP, 2);
		this.voteManager = new VoteManager(javaPlugin, ((Cores) javaPlugin).getPrefix(javaPlugin.getName()), this.gameController, maps);
		registerGameController(this.gameController);
	}
	
	private void setItems(Player player)
	{
		List<ItemStack> itemStacks = Arrays.asList(new ItemBuilder(Material.STONE_SWORD).build(), new ItemBuilder(Material.BOW).build(), new ItemBuilder(Material.LOG_2, 64, (short) 1).build(), new ItemBuilder(Material.ARROW, 16).build(), new ItemBuilder(Material.AIR).build(), new ItemBuilder(Material.GOLDEN_APPLE, 16).build(), new ItemBuilder(Material.BREAD, 32).build(), new ItemBuilder(Material.IRON_AXE).build(), new ItemBuilder(Material.IRON_PICKAXE).build());
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		for (int i = 0; i < itemStacks.size(); i++)
		{
			inventory.setItem(i, itemStacks.get(i));
		}
		GameTeam team = this.teamManager.getTeam(player);
		if (team != null)
		{
			Color leatherColor = team.getLeatherColor();
			inventory.setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setColor(leatherColor).build());
			inventory.setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setColor(leatherColor).build());
			inventory.setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setColor(leatherColor).build());
			inventory.setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setColor(leatherColor).build());
		}
	}
	
	private void registerGameController(GameController gameController)
	{
		Location location = MapsUtils.getLobbyLocation(this.javaPlugin);
		gameController.getLobbyCountdowns().add(new LobbyCountdown(2, this.prefix, this.javaPlugin));
		gameController.getPreGameCountdowns().add(new PreGameCountdown(this.prefix, this.javaPlugin));
		gameController.getEndGameCountdowns().add(new EndGameCountdown(this.prefix, this.javaPlugin));
		gameController.getLobbyListener().add(new LobbyListener(CountdownListener.this.prefix, gameController, location)
		{
			@Override
			protected void setJoinItems(Player player)
			{
				PlayerInventory inventory = player.getInventory();
				inventory.setItem(0, new ItemBuilder(Material.ENDER_CHEST).setName(Colors.SECONDARY + "Teams").build());
				inventory.setItem(8, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setOwnerFromURL("http://textures.minecraft.net/texture/1b6f1a25b6bc199946472aedb370522584ff6f4e83221e5946bd2e41b5ca13b", "MHF_ArrowRight").setName(SECONDARY + "Zurück zur Lobby").build());
			}
		});
		gameController.getPreGameListener().add(new PreGameListener(this.prefix, gameController, this.spectatorManager));
		gameController.getInGameListener().add(new InGameListener(CountdownListener.this.prefix, gameController, CountdownListener.this.spectatorManager)
		{
			@EventHandler
			public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
			{
				((Player) event.getEntity()).setNoDamageTicks(3);
			}
			
			@EventHandler
			public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
			{
				Player player = event.getPlayer();
				CountdownListener.this.teamManager.getTeam(player).remove(player);
			}
			
			@EventHandler
			public void onBlockPlaceEvent(BlockPlaceEvent event)
			{
				for (GameTeam gameTeam : CountdownListener.this.teamManager.getTeams())
				{
					if (gameTeam.getLocation().distance(event.getBlock().getLocation()) <= 10)
					{
						event.setCancelled(true);
						event.getPlayer().sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du darfst am " + IMPORTANT + "Spawn " + TEXT + "nicht bauen");
					}
				}
			}
			
			@EventHandler
			public void onBlockBreakEvent(BlockBreakEvent event)
			{
				for (GameTeam gameTeam : CountdownListener.this.teamManager.getTeams())
				{
					if (gameTeam.getLocation().distance(event.getBlock().getLocation()) <= 10)
					{
						event.setCancelled(true);
						event.getPlayer().sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du darfst am " + IMPORTANT + "Spawn " + TEXT + "nichts abbauen");
					}
				}
			}
			
			@EventHandler
			public void onPlayerInteractEvent(PlayerInteractEvent event)
			{
				Block block = event.getClickedBlock();
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.BEACON)
				{
					event.setCancelled(true);
				}
			}
			
			@EventHandler
			public void onPlayerDeathEvent(PlayerDeathEvent event)
			{
				event.getDrops().clear();
				event.setKeepLevel(false);
				event.setDroppedExp(0);
			}
			
			@EventHandler
			public void onPlayerRespawnEvent(PlayerRespawnEvent event)
			{
				event.setRespawnLocation(CountdownListener.this.teamManager.getTeam(event.getPlayer()).getLocation());
				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						setItems(event.getPlayer());
					}
				}.runTaskLater(CountdownListener.this.javaPlugin, 1);
			}
		});
		gameController.getEndGameListener().add(new EndGameListener(this.prefix, gameController, location));
	}
	
	@EventHandler
	public void onLobbyCountdownPreFinishedEvent(LobbyCountdownPreFinishedEvent event)
	{
		if (!this.teamManager.finishTeams(this.prefix))
		{
			event.setNext(true);
			event.getCountdown().setSeconds(9);
		}
	}
	
	@EventHandler
	public void onLobbyCountdownFinishedEvent(LobbyCountdownFinishedEvent event)
	{
		for (GameTeam gameTeam : this.teamManager.getTeams())
		{
			for (Player players : gameTeam.getPlayers())
			{
				players.teleport(gameTeam.getLocation());
			}
		}
	}
	
	@EventHandler
	public void onLobbyCountdownLastTenSecondsEvent(LobbyCountdownLastTenSecondsEvent event)
	{
		if (this.teamManager != null)
		{
			HandlerList.unregisterAll(this.teamManager);
		}
		this.teamManager = new TeamManager(this.javaPlugin, true, 2);
		this.voteManager.getVotes().getResult();
		MapsUtils.loadSpawns(Configuration.loadConfiguration(new File(this.javaPlugin.getDataFolder() + File.separator + "maps" + File.separator + this.voteManager.getMapName() + ".yml")), this.teamManager, this.spectatorManager);
	}
	
	@EventHandler
	public void onPreGameCountdownStartedEvent(PreGameCountdownStartedEvent event)
	{
		for (Player players : Bukkit.getOnlinePlayers())
		{
			setItems(players);
		}
		this.spectatorManager = new SpectatorManager(this.javaPlugin, true, this.prefix, this.gameController.getLobbyListener().get(0).getLobbyLocation());
		for (PreGameListener listener : this.gameController.getPreGameListener())
		{
			listener.setSpectatorManager(this.spectatorManager);
		}
		for (InGameListener listener : this.gameController.getInGameListener())
		{
			listener.setSpectatorManager(this.spectatorManager);
		}
	}
	
	@EventHandler
	public void onPreGameCountdownFinishedEvent(PreGameCountdownFinishedEvent event)
	{
		Configuration configuration = Configuration.loadConfiguration(new File(this.javaPlugin.getDataFolder() + File.separator + "maps" + File.separator + this.voteManager.getMapName() + ".yml"));
		//MapsUtils.loadSpawns(configuration, this.teamManager, this.spectatorManager);
		List<Core> cores = new ArrayList<>();
		for (String coreNames : configuration.getStringList("Cores.CoreNames"))
		{
			for (GameTeam gameTeam : this.teamManager.getTeams())
			{
				String name = gameTeam.getName();
				cores.add(new Core(coreNames, new Location(Bukkit.getWorld(configuration.getString("Cores." + name + "." + coreNames + ".World")), configuration.getDouble("Cores." + name + "." + coreNames + ".X"), configuration.getDouble("Cores." + name + "." + coreNames + ".Y"), configuration.getDouble("Cores." + name + "." + coreNames + ".Z")), gameTeam, false));
			}
		}
		this.coreManager = new CoreManager(this.javaPlugin, cores, this.teamManager, this.spectatorManager, this.gameController);
		for (Player players : Bukkit.getOnlinePlayers())
		{
			players.updateInventory();
		}
		for (Player players : Bukkit.getOnlinePlayers())
		{
			ScoreBoard.update(players, Messages.getInstance().getShortMessage(getClass(), "servername"), cores, 0);
		}
		this.gameController.setServerState(ServerState.INGAME);
	}
	
	@EventHandler
	public void onEndGameCountdownStartedEvent(EndGameCountdownStartedEvent event)
	{
		HandlerList.unregisterAll(this.coreManager);
		for (Player players : Bukkit.getOnlinePlayers())
		{
			for (PotionEffect potionEffect : players.getActivePotionEffects())
			{
				players.removePotionEffect(potionEffect.getType());
			}
		}
	}
}
