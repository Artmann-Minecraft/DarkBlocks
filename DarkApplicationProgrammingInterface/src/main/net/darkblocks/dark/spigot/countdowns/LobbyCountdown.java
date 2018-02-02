/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.dark.spigot.countdowns;

import lombok.Getter;
import lombok.Setter;
import net.craftplugin.craftpluginapi.spigot.events.LobbyCountdownFinishedEvent;
import net.craftplugin.craftpluginapi.spigot.events.LobbyCountdownLastTenSecondsEvent;
import net.craftplugin.craftpluginapi.spigot.events.LobbyCountdownPreFinishedEvent;
import net.craftplugin.craftpluginapi.spigot.utils.PackageUtils;
import net.darkblocks.dark.universal.messages.Colors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static net.craftplugin.craftpluginapi.universal.messages.Colors.*;

/**
 * Created by LartyHD on 24.06.2017  16:41.
 * Project: LobbyCountdown
 */
@Getter
@Setter
public class LobbyCountdown extends Countdown
{
	private final JavaPlugin javaPlugin;
	private final String prefix;
	private final int minPlayers;
	private boolean idling;
	private int idleID;
	
	public LobbyCountdown(int minPlayers, String prefix, JavaPlugin javaPlugin)
	{
		super(61);
		this.minPlayers = minPlayers;
		this.prefix = prefix;
		this.javaPlugin = javaPlugin;
	}
	
	@Override
	public void start()
	{
		if (!isRunning())
		{
			setRunning(true);
			stopIdle();
			setTaskID(Bukkit.getScheduler().scheduleSyncRepeatingTask(this.javaPlugin, () ->
			{
				if (getSeconds() != 61)
				{
					for (Player players : Bukkit.getOnlinePlayers())
					{
						setLevel(players);
					}
				}
				switch (getSeconds())
				{
					case 60:
					case 45:
					case 30:
					case 20:
					case 10:
					case 5:
					case 4:
					case 3:
					case 2:
						Bukkit.broadcastMessage(this.prefix + Colors.TEXT + "Das Spiel startet in " + Colors.IMPORTANT + getSeconds() + Colors.TEXT + " Sekunden");
						break;
					case 1:
						Bukkit.broadcastMessage(this.prefix + Colors.TEXT + "Das Spiel startet in " + Colors.IMPORTANT + "einer" + Colors.TEXT + " Sekunde");
						break;
					case 0:
						LobbyCountdownPreFinishedEvent lobbyCountdownPreFinishedEvent = new LobbyCountdownPreFinishedEvent(this);
						Bukkit.getPluginManager().callEvent(lobbyCountdownPreFinishedEvent);
						if (!lobbyCountdownPreFinishedEvent.isNext())
						{
							Bukkit.getPluginManager().callEvent(new LobbyCountdownFinishedEvent(this));
							Bukkit.broadcastMessage(this.prefix + Colors.TEXT + "Das Spiel startet");
							for (Player players : Bukkit.getOnlinePlayers())
							{
								players.playSound(players.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
							}
							stop();
						}
						break;
				}
				if (getSeconds() == 10)
				{
					Bukkit.getPluginManager().callEvent(new LobbyCountdownLastTenSecondsEvent(this));
				}
				for (Player players : Bukkit.getOnlinePlayers())
				{
					Location playerLocation = players.getLocation();
					if (getSeconds() == 10)
					{
						//					Utils.sendTitle(players, 10, 20, 10, this.messages.getGameName(), this.messages.getColoredServerName());TODO: SEND TITLE
						players.playSound(playerLocation, Sound.ORB_PICKUP, 1.0F, 1.0F);
					}
					else if (getSeconds() >= 1 && getSeconds() <= 4)
					{
						PackageUtils.sendTitle(players, Colors.SECONDARY + "" + getSeconds(), null, 1, 18, 1);
						players.playSound(playerLocation, Sound.ORB_PICKUP, 1.0F, 1.0F);
					}
				}
				this.setSeconds(getSeconds() - 1);
			}, 0, 20));
		}
	}
	
	@Override
	public void stop()
	{
		stopCountdown();
		stopIdle();
	}
	
	public void idle()
	{
		if (isIdling())
		{
			return;
		}
		setIdling(true);
		stopCountdown();
		setIdleID(Bukkit.getScheduler().scheduleSyncRepeatingTask(getJavaPlugin(), () ->
		{
			int missing = this.minPlayers - Bukkit.getOnlinePlayers().size();
			if (missing == 1)
			{
				Bukkit.broadcastMessage(this.prefix + Colors.TEXT + "Warte auf " + Colors.IMPORTANT + "einen" + Colors.TEXT + " weiteren Spieler...");
			}
			else if (missing >= 1)
			{
				Bukkit.broadcastMessage(this.prefix + Colors.TEXT + "Warte auf " + Colors.IMPORTANT + missing + Colors.TEXT + " weitere Spieler...");
			}
			else
			{
				stopIdle();
				start();
			}
		}, 1, 400));
	}
	
	public void stopCountdown()
	{
		if (!isRunning())
		{
			return;
		}
		setRunning(false);
		Bukkit.getScheduler().cancelTask(getTaskID());
		setSeconds(61);
		for (Player players : Bukkit.getOnlinePlayers())
		{
			players.setLevel(0);
		}
	}
	
	public void stopIdle()
	{
		if (!isIdling())
		{
			return;
		}
		setIdling(false);
		Bukkit.getScheduler().cancelTask(getIdleID());
	}
	
	private void setLevel(Player player)
	{
		player.setLevel(getSeconds());
		player.setExp(getSeconds() / 60);
	}
}
	
