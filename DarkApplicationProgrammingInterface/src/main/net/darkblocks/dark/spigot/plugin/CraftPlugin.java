package net.darkblocks.dark.spigot.plugin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

/**
 * Created by LartyHD on 15.12.2017  03:21.
 */
@Getter
@Setter
@AllArgsConstructor
public class CraftPlugin extends JavaPlugin
{
	@Override
	public synchronized void onLoad()
	{
		sendPluginInfos("Load");
	}
	
	@Override
	public synchronized void onEnable()
	{
		sendPluginInfos("Enable");
	}
	
	@Override
	public synchronized void onDisable()
	{
		sendPluginInfos("Disable");
	}
	
	private void sendPluginInfos(String key)
	{
		ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
		consoleSender.sendMessage("§a" + key + " Plugin§8...");
		PluginDescriptionFile description = getDescription();
		consoleSender.sendMessage("§m                                                  ");
		consoleSender.sendMessage("§aName§8: " + description.getName());
		consoleSender.sendMessage("§aMain§8: " + description.getMain());
		consoleSender.sendMessage("§aVersion§8: " + description.getVersion());
		if (description.getAuthors() != null)
		{
			consoleSender.sendMessage("§aAuthors§8: " + description.getAuthors());
		}
		if (description.getWebsite() != null)
		{
			consoleSender.sendMessage("§aWebSite§8: " + description.getWebsite());
		}
		if (description.getDescription() != null)
		{
			consoleSender.sendMessage("§aDescription§8: " + description.getDescription());
		}
		if (description.getDepend() != null)
		{
			consoleSender.sendMessage("§aDepend§8: " + description.getDepend());
		}
		if (description.getSoftDepend() != null)
		{
			consoleSender.sendMessage("§aSoftDepend§8: " + description.getSoftDepend());
		}
		Map<String, Map<String, Object>> commands = description.getCommands();
		if (commands != null)
		{
			Map<String, Object> command = commands.get(getName());
			if (command != null)
			{
				consoleSender.sendMessage("§aCommands§8: " + commands.values());
			}
		}
		consoleSender.sendMessage("§m                                                  ");
	}
}
