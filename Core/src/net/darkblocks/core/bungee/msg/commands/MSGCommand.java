/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.core.bungee.msg.commands;

import lombok.Getter;
import net.darkblocks.core.bungee.msg.PrivateMessage;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

@Getter
public class MSGCommand extends Command
{
	private final PrivateMessage privateMessage;
	
	public MSGCommand(Plugin plugin, PrivateMessage privateMessage)
	{
		super(CommandUtils.getName(MSGCommand.class));
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
		else
		{
			if (args.length > 1)
			{
				StringBuilder stringBuilder = new StringBuilder();
				for (String arg : args)
				{
					stringBuilder.append(IMPORTANT).append(arg).append(" ");
				}
				String messages = stringBuilder.substring(target.getName().length() + 3);
				if (!messages.equalsIgnoreCase("") && !messages.equalsIgnoreCase(" "))
				{
					getPrivateMessage().getReplay().put(target.getName(), sender.getName());
					sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName() + TEXT + "<Spieler> <Nachricht>"));
					sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "[Du -> " + target.getName() + "] " + IMPORTANT + messages));
					target.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "[" + sender.getName() + " -> Dir] " + IMPORTANT + messages));
				}
			}
			else
			{
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName() + TEXT + " <Spieler> <Nachricht>"));
			}
		}
	}
}
