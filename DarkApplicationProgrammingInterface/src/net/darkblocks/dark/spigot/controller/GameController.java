/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.controller;

import lombok.Getter;
import lombok.Setter;
import net.darkblocks.dark.java.utils.ServerState;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.countdowns.EndGameCountdown;
import net.darkblocks.dark.spigot.countdowns.LobbyCountdown;
import net.darkblocks.dark.spigot.countdowns.PreGameCountdown;
import net.darkblocks.dark.spigot.countdowns.SaveTimeCountdown;
import net.darkblocks.dark.spigot.events.*;
import net.darkblocks.dark.spigot.events.listener.EventsListener;
import net.darkblocks.dark.spigot.listener.*;
import net.darkblocks.dark.universal.messages.Colors;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

/**
 * Created by LartyHD on 29.11.2017  14:06.
 */
@Getter
@Setter
public class GameController implements Listener
{
	private final JavaPlugin javaPlugin;
	private final ArrayList<LobbyListener> lobbyListener;
	private final ArrayList<PreGameListener> preGameListener;
	private final ArrayList<SaveTimeListener> saveTimeListener;
	private final ArrayList<InGameListener> inGameListener;
	private final ArrayList<EndGameListener> endGameListener;
	private final ArrayList<LobbyCountdown> lobbyCountdowns;
	private final ArrayList<PreGameCountdown> preGameCountdowns;
	private final ArrayList<SaveTimeCountdown> saveTimeCountdowns;
	private final ArrayList<EndGameCountdown> endGameCountdowns;
	private ServerState serverState;
	private int minPlayers;
	
	public GameController(JavaPlugin javaPlugin, ServerState serverState, int minPlayers)
	{
		this.javaPlugin = javaPlugin;
		this.serverState = serverState;
		this.minPlayers = minPlayers;
		//LISTENER
		this.lobbyListener = new ArrayList<>();
		this.preGameListener = new ArrayList<>();
		this.saveTimeListener = new ArrayList<>();
		this.inGameListener = new ArrayList<>();
		this.endGameListener = new ArrayList<>();
		//LISTENER
		//COUNTDOWNS
		this.lobbyCountdowns = new ArrayList<>();
		this.preGameCountdowns = new ArrayList<>();
		this.saveTimeCountdowns = new ArrayList<>();
		this.endGameCountdowns = new ArrayList<>();
		//COUNTDOWNS
		registerListener(this);
		new EventsListener(javaPlugin);
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		if ((this.serverState == ServerState.LOBBY || this.serverState == ServerState.LOBBYFULL) && this.minPlayers <= Bukkit.getOnlinePlayers().size())
		{
			for (LobbyCountdown countdown : getLobbyCountdowns())
			{
				countdown.start();
			}
		}
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
	{
		if ((this.serverState == ServerState.LOBBY || this.serverState == ServerState.LOBBYFULL) && this.minPlayers <= Bukkit.getOnlinePlayers().size())
		{
			for (LobbyCountdown countdown : getLobbyCountdowns())
			{
				countdown.stopCountdown();
				countdown.idle();
			}
		}
	}
	
	@EventHandler
	public void onLobbyCountdownFinishedEvent(LobbyCountdownFinishedEvent event)
	{
		this.setServerState(ServerState.PREGAME);
	}
	
	@EventHandler
	public void onSaveTimeCountdownFinishedEvent(SaveTimeCountdownFinishedEvent event)
	{
		this.setServerState(ServerState.INGAME);
	}
	
	@EventHandler
	public void onEndGameCountdownStartedEvent(EndGameCountdownStartedEvent event)
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for (Player players : Bukkit.getOnlinePlayers())
				{
					PlayerInventory inventory = players.getInventory();
					inventory.clear();
					inventory.setArmorContents(null);
					players.setLevel(0);
					players.setExp(0);
					players.teleport(getEndGameListener().get(0).getLobbyLocation());
					for (Player all : Bukkit.getOnlinePlayers())
					{
						players.showPlayer(all);
					}
					inventory.setItem(8, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setOwnerFromURL("http://textures.minecraft.net/texture/1b6f1a25b6bc199946472aedb370522584ff6f4e83221e5946bd2e41b5ca13b", "MHF_ArrowRight").setDisplayName(Colors.SECONDARY + "Zurück zur Lobby").build());
					players.setGameMode(GameMode.SURVIVAL);
					players.setAllowFlight(false);
					players.setFlying(false);
					players.setFoodLevel(20);
					players.setHealth(20);
					players.setSaturation(0);
				}
			}
		}.runTaskLater(getJavaPlugin(), 1);
	}
	
	@EventHandler
	public void onServerStateChangeEvent(ServerStateChangeEvent event)
	{
		ServerState newServerState = event.getNewServerState();
		switch (this.serverState != newServerState ? this.serverState = newServerState : newServerState)
		{
			case LOBBY:
			case LOBBYFULL:
				for (LobbyListener listener : getLobbyListener())
				{
					HandlerList.unregisterAll(listener);
					registerListener(listener);
				}
				break;
			case PREGAME:
				for (PreGameListener listener : getPreGameListener())
				{
					HandlerList.unregisterAll(listener);
					registerListener(listener);
				}
				for (PreGameCountdown countdown : getPreGameCountdowns())
				{
					countdown.start();
				}
				break;
			case SAVETIME:
				for (SaveTimeListener listener : getSaveTimeListener())
				{
					HandlerList.unregisterAll(listener);
					registerListener(listener);
				}
				for (SaveTimeCountdown countdown : getSaveTimeCountdowns())
				{
					countdown.start();
				}
				break;
			case INGAME:
				for (InGameListener listener : getInGameListener())
				{
					HandlerList.unregisterAll(listener);
					registerListener(listener);
				}
				break;
			case ENDGAME:
				for (EndGameListener listener : getEndGameListener())
				{
					HandlerList.unregisterAll(listener);
					registerListener(listener);
				}
				for (EndGameCountdown countdown : getEndGameCountdowns())
				{
					countdown.start();
				}
				break;
		}
	}
	
	public void setServerState(ServerState serverState)
	{
		Bukkit.getPluginManager().callEvent(new ServerStateChangeEvent(this.serverState, serverState));
		this.serverState = serverState;
	}
	
	public void registerListener(Listener listener)
	{
		Bukkit.getPluginManager().registerEvents(listener, this.javaPlugin);
	}
}
