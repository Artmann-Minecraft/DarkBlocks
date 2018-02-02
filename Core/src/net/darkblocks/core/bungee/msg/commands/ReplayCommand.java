/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.core.bungee.msg.commands;

import lombok.Getter;
import net.craftplugin.craftpluginapi.universal.messages.Colors;
import net.craftplugin.craftpluginapi.universal.messages.Messages;
import net.craftplugin.craftpluginapi.universal.utils.CommandUtils;
import net.darkblocks.core.bungee.msg.PrivateMessage;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import static net.craftplugin.craftpluginapi.universal.messages.Colors.IMPORTANT;
import static net.craftplugin.craftpluginapi.universal.messages.Colors.TEXT;

@Getter
public class ReplayCommand extends Command
{
	private final PrivateMessage privateMessage;
	
	public ReplayCommand(Plugin plugin, PrivateMessage privateMessage)
	{
		super(CommandUtils.getName(ReplayCommand.class), null, "r");
		this.privateMessage = privateMessage;
		CommandUtils.register(plugin, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
		if (target == null)
		{
			sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "notonline"));
		}
		else if (args.length > 1)
		{
			String targetName = getPrivateMessage().getReplay().get(sender.getName());
			if (targetName == null)
			{
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "notindatabase"));
			}
			else
			{
				StringBuilder sb = new StringBuilder();
				for (String arg : args)
				{
					sb.append(IMPORTANT).append(arg).append(" ");
				}
				String messages = sb.toString();
				if (!messages.equalsIgnoreCase("") && !messages.equalsIgnoreCase(" "))
				{
					getPrivateMessage().getReplay().put(target.getName(), sender.getName());
					sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "[Du -> " + target.getName() + "] " + IMPORTANT + messages));
					target.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "[" + sender.getName() + " -> Dir] " + IMPORTANT + messages));
					return;
				}
			}
		}
		sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", Colors.IMPORTANT + "/" + getName() + Colors.TEXT + "<Nachricht>"));
	}
}
