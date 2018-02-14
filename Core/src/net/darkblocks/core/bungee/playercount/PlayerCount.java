package net.darkblocks.core.bungee.playercount;

import lombok.Getter;
import net.darkblocks.core.bungee.playercount.listener.PlayerCountListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by LartyHD on 14.02.2018  19:24.
 */
@Getter
public class PlayerCount
{
	private Set<String> player;
	
	public PlayerCount(Plugin plugin)
	{
		System.out.println(3);
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
			this.player = new HashSet<>(ConfigurationProvider.getProvider(YamlConfiguration.class).load(file).getStringList("players"));
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
		new PlayerCountListener(plugin, this);
		System.out.println(4);
	}
	
	public void disable(Plugin plugin)
	{
		System.out.println(5);
		if (getPlayer() != null)
		{
			System.out.println(6);
			File file = new File(plugin.getDataFolder(), "playercount.yml");
			try
			{
				System.out.println(7);
				Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
				configuration.set("players", new ArrayList<>(getPlayer()));
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
				System.out.println(8);
			} catch (IOException ex)
			{
				System.out.println(9);
				ex.printStackTrace();
			}
		}
	}
}
