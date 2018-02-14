package net.darkblocks.core.bungee.playercount;

import lombok.Getter;
import net.darkblocks.core.bungee.playercount.listener.PlayerCountListener;
import net.darkblocks.dark.spigot.config.Configuration;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by LartyHD on 14.02.2018  19:24.
 */
@Getter
public class PlayerCount
{
	private final List<String> player;
	
	public PlayerCount(Plugin plugin)
	{
		File file = new File(plugin.getDataFolder(), "playercount.yml");
		if (!file.exists())
		{
			try
			{
				if (!file.createNewFile())
				{
					System.err.println("Can not create " + file);
				}
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		Configuration configuration = Configuration.loadConfiguration(file);
		this.player = configuration.getStringList("players");
		new PlayerCountListener(plugin, this.player);
	}
	
	public void disable(Plugin plugin)
	{
		Configuration configuration = Configuration.loadConfiguration(new File(plugin.getDataFolder(), "playercount.yml"));
		configuration.set("players", getPlayer());
	}
}
