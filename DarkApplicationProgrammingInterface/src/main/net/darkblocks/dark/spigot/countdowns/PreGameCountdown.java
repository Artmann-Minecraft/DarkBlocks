/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.dark.spigot.countdowns;

import lombok.Getter;
import net.darkblocks.dark.spigot.events.PreGameCountdownFinishedEvent;
import net.darkblocks.dark.spigot.events.PreGameCountdownStartedEvent;
import net.darkblocks.dark.spigot.utils.PackageUtils;
import net.darkblocks.dark.universal.messages.Colors;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 24.06.2017  18:01.
 * Project: PreGameCountdown
 */
@Getter
public class PreGameCountdown extends Countdown
{
	private final String prefix;
	private final JavaPlugin javaPlugin;
	
	public PreGameCountdown(String prefix, JavaPlugin javaPlugin)
	{
		super(6);
		this.prefix = prefix;
		this.javaPlugin = javaPlugin;
	}
	
	@Override
	public void start()
	{
		if (!isRunning())
		{
			setRunning(true);
			Bukkit.getPluginManager().callEvent(new PreGameCountdownStartedEvent(this));
			setTaskID(Bukkit.getScheduler().scheduleSyncRepeatingTask(getJavaPlugin(), () ->
			{
				for (Player players : Bukkit.getOnlinePlayers())
				{
					setLevel(players);
				}
				switch (getSeconds())
				{
					case 10:
					case 5:
					case 4:
					case 3:
					case 2:
						Bukkit.broadcastMessage(this.prefix + Colors.TEXT + "Die Runde startet in " + Colors.IMPORTANT + getSeconds() + Colors.TEXT + " Sekunden");
						break;
					case 1:
						Bukkit.broadcastMessage(this.prefix + Colors.TEXT + "Die Runde startet in " + Colors.IMPORTANT + "einer" + Colors.TEXT + " Sekunde");
						break;
					case 0:
						Bukkit.broadcastMessage(this.prefix + Colors.TEXT + "Die Runde startet");
						Bukkit.getPluginManager().callEvent(new PreGameCountdownFinishedEvent(this));
						stop();
						break;
				}
				if (getSeconds() != 0 && getSeconds() != 6)
				{
					for (Player players : Bukkit.getOnlinePlayers())
					{
						PackageUtils.sendTitle(players, Colors.SECONDARY + "" + getSeconds(), null, 1, 18, 1);
						players.playSound(players.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
					}
				}
				this.setSeconds(getSeconds() - 1);
			}, 0, 20));
		}
	}
	
	@Override
	public void stop()
	{
		Bukkit.getScheduler().cancelTask(getTaskID());
		setRunning(false);
		setSeconds(6);
	}
	
	private void setLevel(Player player)
	{
		player.setLevel(getSeconds());
		player.setExp((float) getSeconds() / 5F);
	}
}
