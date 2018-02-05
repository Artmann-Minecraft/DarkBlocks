package net.darkblocks.core.spigot.fix.bungeehack.listener;

import lombok.Getter;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

import static net.darkblocks.dark.universal.messages.Colors.PRIMARY;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 13.01.2018  00:49.
 */
@Getter
public class BungeeHackListener implements Listener
{
	private final Set<String> bungeeCords;
	
	public BungeeHackListener(JavaPlugin javaPlugin)
	{
		this.bungeeCords = new HashSet<>();
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(PlayerLoginEvent event)
	{
		String ip = event.getRealAddress().getHostAddress();
		if (!check(ip))
		{
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, TEXT + "Bitte joine über " + PRIMARY + Messages.getInstance().getMessage(Messages.getInstance().getPathPrefix() + "servername"));
		}
	}
	
	private boolean check(String ip)
	{
		for (String string : getBungeeCords())
		{
			if (string.equalsIgnoreCase(ip))
			{
				return true;
			}
		}
		return false;
	}
}
