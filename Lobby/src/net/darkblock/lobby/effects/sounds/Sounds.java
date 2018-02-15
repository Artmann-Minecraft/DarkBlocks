package net.darkblock.lobby.effects.sounds;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 15.02.2018  22:32.
 */
public class Sounds
{
	public Sounds(JavaPlugin javaPlugin)
	{
		new SoundsListener(javaPlugin);
	}
}
