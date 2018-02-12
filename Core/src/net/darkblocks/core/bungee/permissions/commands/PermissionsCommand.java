package net.darkblocks.core.bungee.permissions.commands;

import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.core.universal.permissions.utils.User;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 12.02.2018  23:19.
 */
public class PermissionsCommand extends Command
{
	private final UserManager userManager;
	
	public PermissionsCommand(Plugin plugin, UserManager userManager)
	{
		super(CommandUtils.getName(PermissionsCommand.class), null, "perms");
		this.userManager = userManager;
		CommandUtils.register(plugin, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (!(sender instanceof ProxiedPlayer))
		{
			sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "onlyforplayers"));
		}
		else
		{
			for (User user : this.userManager.getUser())
			{
				if (user.getUuid() == ((ProxiedPlayer) sender).getUniqueId())
				{
					sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Deine Permissions"));
					for (String s : user.getPermissions())
					{
						sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + s));
					}
				}
			}
		}
	}
}
