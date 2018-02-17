/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.dark.java.config;

import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by LartyHD on 11.11.2017  03:50.
 */
@SuppressWarnings("ALL")
@Getter
public class PropertiesConfig extends Properties
{
	private final File file;
	private final File directory;
	
	public PropertiesConfig(File directory, String file)
	{
		this.directory = directory;
		this.file = new File(directory + File.separator + file);
		if (this.file.exists())
		{
			try
			{
				FileInputStream fileInputStream = new FileInputStream(this.file);
				this.load(fileInputStream);
				fileInputStream.close();
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		else if (!directory.exists())
		{
			if (!directory.mkdir())
			{
				System.out.println(" ");
				System.out.println("[PropertiesConfig] Der Ordner konnte nicht erstellt werden");
				System.out.println(" ");
			}
			else
			{
				try
				{
					if (!this.file.createNewFile())
					{
						System.out.println(" ");
						System.out.println("[PropertiesConfig] Die Datei konnte nicht erstellt werden");
						System.out.println(" ");
					}
				} catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}
		}
		save();
	}
	
	public void save()
	{
		try
		{
			FileOutputStream fileOutputStream = new FileOutputStream(this.file);
			this.store(fileOutputStream, null);
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
}
