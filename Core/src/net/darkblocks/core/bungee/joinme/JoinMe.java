package net.darkblocks.core.bungee.joinme;

import net.darkblocks.core.bungee.joinme.commands.ExecuteJoinMeCommand;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created by LartyHD on 15.01.2018  02:36.
 */
public class JoinMe
{
	public JoinMe(Plugin plugin)
	{
		new JoinMe(plugin);
		new ExecuteJoinMeCommand(plugin);
	}
}
