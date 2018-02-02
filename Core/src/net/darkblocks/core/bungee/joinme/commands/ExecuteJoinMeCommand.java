package net.darkblocks.core.bungee.joinme.commands;

import lombok.Getter;
import net.craftplugin.craftpluginapi.universal.messages.Messages;
import net.craftplugin.craftpluginapi.universal.utils.CommandUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import static net.craftplugin.craftpluginapi.universal.messages.Colors.IMPORTANT;
import static net.craftplugin.craftpluginapi.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 10.10.2017  23:55.
 */
@Getter
public class ExecuteJoinMeCommand extends Command
{
	public ExecuteJoinMeCommand(Plugin plugin)
	{
		super(CommandUtils.getName(ExecuteJoinMeCommand.class));
		CommandUtils.register(plugin, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (!(sender instanceof ProxiedPlayer))
		{
			sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "onlyforplayers"));
		}
		else if (args.length == 1)
		{
			((ProxiedPlayer) sender).connect(BungeeCord.getInstance().getServerInfo(args[0]));
		}
		else
		{
			sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName() + TEXT + ""));
		}
	}
}
