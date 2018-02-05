package net.darkblocks.core.bungee.commands;

import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created by LartyHD on 14.01.2018  02:03.
 */
public class Commands
{
	public Commands(Plugin plugin)
	{
		new JumpToCommand(plugin);
		new ListCommand(plugin);
		new LobbyCommand(plugin);
		new PingCommand(plugin);
		new KickCommand(plugin);
	}
}
