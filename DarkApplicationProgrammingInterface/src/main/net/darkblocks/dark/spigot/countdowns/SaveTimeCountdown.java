/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.dark.spigot.countdowns;

import lombok.Getter;
import net.craftplugin.craftpluginapi.spigot.events.SaveTimeCountdownFinishedEvent;
import net.craftplugin.craftpluginapi.spigot.events.SaveTimeCountdownStartedEvent;
import net.craftplugin.craftpluginapi.spigot.utils.PackageUtils;
import net.darkblocks.dark.universal.messages.Colors;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 24.06.2017  18:01.
 * Project: PreGameCountdown
 */
@Getter
public class SaveTimeCountdown extends Countdown
{
	private final Messages messages;
	private final JavaPlugin javaPlugin;
	
	@SuppressWarnings("unused")
	public SaveTimeCountdown(Messages messages, JavaPlugin javaPlugin)
	{
		super(60);
		this.messages = messages;
		this.javaPlugin = javaPlugin;
	}
	
	@Override
	public void start()
	{
		if (!isRunning())
		{
			setRunning(true);
			Bukkit.getPluginManager().callEvent(new SaveTimeCountdownStartedEvent(this));
			setTaskID(Bukkit.getScheduler().scheduleSyncRepeatingTask(getJavaPlugin(), () ->
			{
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
						Bukkit.broadcastMessage(getMessages().getMessage("dark.api.spigot.countdouns.savetimecountdown.prefix") + Colors.TEXT + "Die Schutzzeit ended in " + Colors.IMPORTANT + getSeconds() + Colors.TEXT + " Sekunden");
						break;
					case 1:
						Bukkit.broadcastMessage(getMessages().getMessage("dark.api.spigot.countdouns.savetimecountdown.prefix") + Colors.TEXT + "Die Schutzzeit ended in " + Colors.IMPORTANT + getSeconds() + Colors.TEXT + " Sekunde");
						break;
					case 0:
						Bukkit.broadcastMessage(getMessages().getMessage("dark.api.spigot.countdouns.savetimecountdown.prefix") + Colors.TEXT + "Die Schutzzeit ended");
						Bukkit.getPluginManager().callEvent(new SaveTimeCountdownFinishedEvent(this));
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
}
