/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
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
	
	public static Configuration loadConfiguration(@NonNull File folder, @NonNull File file)
	{
		Configuration config = new Configuration();
		config.setTheFile(file);
		try
		{
			if (!folder.exists())
			{
				if (folder.mkdirs())
				{
					System.out.println(" ");
					System.out.println("[Configuration] Created Folder " + folder);
					System.out.println(" ");
				}
			}
			if (!file.exists())
			{
				if (file.createNewFile())
				{
					System.out.println(" ");
					System.out.println("[Configuration] Created File " + file);
					System.out.println(" ");
				}
			}
			config.load(file);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return config;
	}
	
	@SuppressWarnings("WeakerAccess")
	public File getTheFile()
	{
		return this.theFile;
	}
	
	private void setTheFile(File file)
	{
		this.theFile = file;
	}
}