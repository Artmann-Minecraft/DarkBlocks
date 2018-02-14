package net.darkblocks.core.bungee.playercount;

import lombok.Getter;
import net.darkblocks.core.bungee.playercount.listener.PlayerCountListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by LartyHD on 14.02.2018  19:24.
 */
@Getter
public class PlayerCount
{
	private List<String> player;
	
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
		try
		{
			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			this.player = configuration.getStringList("players");
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
		new PlayerCountListener(plugin, this.player);
	}
	
	public void disable(Plugin plugin)
	{
		File file = new File(plugin.getDataFolder(), "playercount.yml");
		try
		{
			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			configuration.set("players", getPlayer());
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
}
