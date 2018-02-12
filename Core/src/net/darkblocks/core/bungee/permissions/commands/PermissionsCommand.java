package net.darkblocks.core.bungee.permissions.commands;

import net.darkblocks.core.universal.permissions.manager.GroupManager;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.core.universal.permissions.utils.Group;
import net.darkblocks.core.universal.permissions.utils.User;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 12.02.2018  23:19.
 */
public class PermissionsCommand extends Command
{
	private final UserManager userManager;
	private final GroupManager groupManager;
	
	public PermissionsCommand(Plugin plugin, UserManager userManager, GroupManager groupManager)
	{
		super(CommandUtils.getName(PermissionsCommand.class), null, "perms");
		this.userManager = userManager;
		this.groupManager = groupManager;
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
			if (args.length == 0)
			{
				for (User user : this.userManager.getUser())
				{
					if (user.getUuid() == ((ProxiedPlayer) sender).getUniqueId())
					{
						sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Deine Permissions"));
						for (String s : user.getPermissions())
						{
							sender.sendMessage(new TextComponent(TEXT + s));
						}
					}
				}
			}
			else if (args.length == 1)
			{
				for (Group group : this.groupManager.getGroups())
				{
					try
					{
						if (Integer.valueOf(args[0]) == group.getSaveID())
						{
							sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Permissions der Gruppe " + IMPORTANT + group.getName()));
							for (String s : group.getPermissions())
							{
								sender.sendMessage(new TextComponent(TEXT + s));
							}
							return;
						}
					} catch (NumberFormatException ex)
					{
						if (group.getName().equalsIgnoreCase(args[0]))
						{
							sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Permissions der Gruppe " + IMPORTANT + group.getName()));
							for (String s : group.getPermissions())
							{
								sender.sendMessage(new TextComponent(TEXT + s));
							}
							return;
						}
					}
				}
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Die Gruppe " + IMPORTANT + args[0] + TEXT + " gibt es nicht"));
			}
			else
			{
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName() + TEXT + " [Gruppe]"));
			}
		}
	}
}
