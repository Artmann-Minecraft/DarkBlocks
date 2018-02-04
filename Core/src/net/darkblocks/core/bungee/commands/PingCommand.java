/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.core.bungee.commands;

import lombok.Getter;
import net.darkblocks.dark.universal.messages.Colors;
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
public class PingCommand extends Command
{
	@SuppressWarnings("WeakerAccess")
	public PingCommand(Plugin plugin)
	{
		super(CommandUtils.getName(PingCommand.class));
		CommandUtils.register(plugin, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (args == null || args.length == 0)
		{
			if (!(sender instanceof ProxiedPlayer))
			{
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "onlyforplayers"));
			}
			else
			{
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Dein Ping beträgt " + IMPORTANT + ((ProxiedPlayer) sender).getPing() + TEXT + "ms"));
			}
		}
		else if (args.length == 1)
		{
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
			if (target != null)
			{
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Der Ping des Spielers " + IMPORTANT + target.getName() + TEXT + " beträgt " + IMPORTANT + target.getPing() + TEXT + "ms"));
			}
			else
			{
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "notonline"));
			}
		}
		else
		{
			sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", Colors.IMPORTANT + "/" + getName() + Colors.TEXT + "[Spieler]"));
		}
	}
}
