package net.darkblocks.core.spigot.fix.bungeehack.listener;

import com.segdogames.segdocloudplugin.api.CloudAPI;
import lombok.Getter;
import net.craftplugin.craftpluginapi.java.mysql.MySQL;
import net.craftplugin.craftpluginapi.universal.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.craftplugin.craftpluginapi.universal.messages.Colors.PRIMARY;
import static net.craftplugin.craftpluginapi.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 13.01.2018  00:49.
 */
@Getter
public class BungeeHackListener implements Listener
{
	private final MySQL mySQL;
	private final List<String> bungeeCords;
	
	public BungeeHackListener(MySQL mySQL, JavaPlugin javaPlugin)
	{
		this.mySQL = mySQL;
		this.bungeeCords = new ArrayList<>();
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
		getMySQL().update("CREATE TABLE IF NOT EXISTS BungeeHackerAtacks(id INT NOT NULL AUTO_INCREMENT, address VARCHAR(100), host_address VARCHAR(100), uuid VARCHAR(100), `name` VARCHAR(20), `date` VARCHAR(100), `server` VARCHAR(100), PRIMARY KEY(ID))");
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(PlayerLoginEvent event)
	{
		String ip = event.getRealAddress().getHostAddress();
		if (!check(ip))
		{
			Player player = event.getPlayer();
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, TEXT + "Bitte joine über " + PRIMARY + Messages.getInstance().getMessage(Messages.getInstance().getPathPrefix() + "servername"));
			String time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
			getMySQL().update("INSERT INTO BungeeHackerAngriffe(address, host_address, uuid, `name`, `date`, `server`) VALUES ('" + event.getRealAddress() + "','" + ip + "','" + player.getUniqueId().toString() + "','" + player.getName() + "','" + time + "','" + CloudAPI.get().getNameAPI().getServerName() + "')");
		/*for (Player players : Bukkit.getOnlinePlayers())
		{
			if (!players.hasPermission("dark.core.spigot.fix.bungeehack.seehackjoins"))
			{
				continue;
			}
			players.sendMessage(getMessages().getMessage("dark.fix.bungeehack.prefix") + TEXT + "Der Server hat ein BungeeHackerAngriffe blockiert weitere daten in der DatenBank. Uhrzeit:" + time);
		}*/
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
