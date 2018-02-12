package net.darkblocks.core.bungee.joinme;

import net.darkblocks.core.bungee.joinme.commands.ExecuteJoinMeCommand;
import net.darkblocks.core.bungee.joinme.commands.JoinMeCommand;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created by LartyHD on 15.01.2018  02:36.
 */
public class JoinMe
{
	public JoinMe(Plugin plugin, UserManager userManager)
	{
		new JoinMeCommand(plugin, userManager);
		new ExecuteJoinMeCommand(plugin);
	}
}
