package net.darkblocks.dark.spigot.config;

import lombok.NonNull;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Created by LartyHD on 02.08.2017  14:06.
 */
public class Configuration extends YamlConfiguration
{
	private File theFile;
	
	public static Configuration loadConfiguration(@NonNull File file)
	{
		Configuration config = new Configuration();
		try
		{
			config.load(file);
			config.setTheFile(file);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return config;
	}
	
	public File getTheFile()
	{
		return this.theFile;
	}
	
	private void setTheFile(File file)
	{
		this.theFile = file;
	}
}