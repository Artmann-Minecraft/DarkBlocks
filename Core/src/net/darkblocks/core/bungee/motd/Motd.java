package net.darkblocks.core.bungee.motd;

import net.darkblocks.core.bungee.motd.listener.MotdListener;
import net.darkblocks.dark.java.mysql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created by LartyHD on 06.02.2018  10:07.
 */
public class Motd
{
	public Motd(Plugin plugin, MySQL mySQL)
	{
		new MotdListener(plugin, mySQL);
	}
}
