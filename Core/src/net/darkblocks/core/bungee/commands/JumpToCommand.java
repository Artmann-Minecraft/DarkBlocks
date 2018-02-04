/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.core.bungee.commands;

import lombok.Getter;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

@Getter
public class JumpToCommand extends Command
{
	JumpToCommand(Plugin plugin)
	{
		super(CommandUtils.getName(JumpToCommand.class), CommandUtils.getPermission(JumpToCommand.class));
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
			if (args.length == 1)
			{
				ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
				if (target != null)
				{
					((ProxiedPlayer) sender).connect(target.getServer().getInfo());
				}
				else
				{
					sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "notonline"));
				}
			}
			else
			{
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName() + TEXT + "<Spieler>"));
			}
		}
	}
}
