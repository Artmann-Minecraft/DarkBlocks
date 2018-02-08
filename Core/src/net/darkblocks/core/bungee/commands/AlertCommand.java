/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.core.bungee.commands;

import net.darkblocks.dark.universal.messages.Colors;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

public class AlertCommand extends Command
{
	public AlertCommand(Plugin plugin)
	{
		super(CommandUtils.getName(AlertCommand.class), CommandUtils.getPermission(AlertCommand.class), "bc", "broadcast");
		CommandUtils.register(plugin, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (args.length != 0)
		{
			StringBuilder stringBuilder = new StringBuilder();
			for (String arg : args)
			{
				stringBuilder.append(arg).append(" ");
			}
			for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
			{
				player.sendMessage(new TextComponent(" "));
				player.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "servername", "" + Colors.IMPORTANT + Colors.EXTRA + ": " + Colors.TEXT + ChatColor.translateAlternateColorCodes('&', stringBuilder.toString())));
				player.sendMessage(new TextComponent(" "));
			}
		}
		else
		{
			sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName() + TEXT + " <Nachricht>"));
		}
	}
}
