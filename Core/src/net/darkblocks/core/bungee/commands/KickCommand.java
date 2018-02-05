/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.core.bungee.commands;

import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

public class KickCommand extends Command
{
	KickCommand(Plugin plugin)
	{
		super(CommandUtils.getName(KickCommand.class), CommandUtils.getPermission(KickCommand.class));
		CommandUtils.register(plugin, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (args.length > 1)
		{
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
			if (target != null)
			{
				StringBuilder sb = new StringBuilder();
				for (String arg : args)
				{
					sb.append(arg).append(" ");
				}
				String grund = sb.toString().substring(target.getName().length() + 1);
				target.disconnect(new TextComponent(TEXT + "Du wurdest vom " + Messages.getInstance().getShortTextComponent(getClass(), "servername", IMPORTANT + " Netzwerk" + TEXT + " gekickt.\n" + TEXT + "Grund: " + IMPORTANT + grund)));
				sender.sendMessage(new TextComponent(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Du hast " + IMPORTANT + target.getName() + TEXT + " gekickt")));
				for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers())
				{
					if (!players.hasPermission(getPermission()))
					{
						continue;
					}
					players.sendMessage(new TextComponent(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + target.getName() + TEXT + " wurde von " + IMPORTANT + sender.getName() + TEXT + " gekickt.")));
					players.sendMessage(new TextComponent(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Grund" + IMPORTANT + ": " + IMPORTANT + grund)));
				}
			}
			else
			{
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "notonline"));
			}
		}
		else
		{
			sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName() + TEXT + "<Spieler> <Grund>"));
		}
	}
}
