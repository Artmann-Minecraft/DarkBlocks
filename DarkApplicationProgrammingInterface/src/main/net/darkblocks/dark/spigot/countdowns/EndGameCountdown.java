/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.dark.spigot.countdowns;

import lombok.Getter;
import net.craftplugin.craftpluginapi.spigot.events.EndGameCountdownFinishedEvent;
import net.craftplugin.craftpluginapi.spigot.events.EndGameCountdownStartedEvent;
import net.darkblocks.dark.universal.messages.Colors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 24.06.2017  17:22.
 * Project: EndGameCountdown
 */
@SuppressWarnings("ALL")
@Getter
public class EndGameCountdown extends Countdown
{
	private final String prefix;
	private final JavaPlugin javaPlugin;
	
	@SuppressWarnings("unused")
	public EndGameCountdown(String prefix, JavaPlugin javaPlugin)
	{
		super(16);
		this.prefix = prefix;
		this.javaPlugin = javaPlugin;
	}
	
	@Override
	public void start()
	{
		if (!isRunning())
		{
			setRunning(true);
			Bukkit.getPluginManager().callEvent(new EndGameCountdownStartedEvent(this));
			setTaskID(Bukkit.getScheduler().scheduleSyncRepeatingTask(getJavaPlugin(), () ->
			{
				for (Player players : Bukkit.getOnlinePlayers())
				{
					setLevel(players);
				}
				switch (getSeconds())
				{
					case 1:
						Bukkit.broadcastMessage(prefix + Colors.TEXT + "Der Server startet in " + Colors.IMPORTANT + "einer" + Colors.TEXT + " Sekunde neu");
						break;
					case 2:
					case 3:
					case 4:
					case 5:
					case 10:
					case 15:
					case 20:
					case 30:
					case 45:
					case 60:
						Bukkit.broadcastMessage(prefix + Colors.TEXT + "Der Server startet in " + Colors.IMPORTANT + getSeconds() + Colors.TEXT + " Sekunden neu");
						break;
				}
				if (this.getSeconds() == 0 || Bukkit.getOnlinePlayers().size() == 0)
				{
					Bukkit.getPluginManager().callEvent(new EndGameCountdownFinishedEvent(this));
					Bukkit.shutdown();
					stop();
				}
				this.setSeconds(getSeconds() - 1);
			}, 0, 20));
		}
	}
	
	@Override
	public void stop()
	{
		if (isRunning())
		{
			Bukkit.getScheduler().cancelTask(getTaskID());
			setRunning(false);
			setSeconds(11);
		}
	}
	
	private void setLevel(Player player)
	{
		player.setLevel(getSeconds());
		player.setExp(getSeconds() / 15);
	}
}
