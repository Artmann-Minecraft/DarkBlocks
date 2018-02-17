/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.countdowns;

import lombok.Getter;
import net.darkblocks.dark.spigot.events.PreGameCountdownFinishedEvent;
import net.darkblocks.dark.spigot.events.PreGameCountdownStartedEvent;
import net.darkblocks.dark.spigot.utils.PackageUtils;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 24.06.2017  18:01.
 * Project: PreGameCountdown
 */
@Getter
public class PreGameCountdown extends Countdown
{
	private final JavaPlugin javaPlugin;
	
	public PreGameCountdown(JavaPlugin javaPlugin)
	{
		super(6);
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
						Bukkit.broadcastMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Die Runde startet in " + IMPORTANT + getSeconds() + TEXT + " Sekunden");
						break;
					case 1:
						Bukkit.broadcastMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Die Runde startet in " + IMPORTANT + "einer" + TEXT + " Sekunde");
						break;
					case 0:
						Bukkit.broadcastMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Die Runde startet");
						Bukkit.getPluginManager().callEvent(new PreGameCountdownFinishedEvent(this));
						stop();
						break;
				}
				if (getSeconds() != 0 && getSeconds() != 6)
				{
					for (Player players : Bukkit.getOnlinePlayers())
					{
						PackageUtils.sendTitle(players, SECONDARY + "" + getSeconds(), null, 1, 18, 1);
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
