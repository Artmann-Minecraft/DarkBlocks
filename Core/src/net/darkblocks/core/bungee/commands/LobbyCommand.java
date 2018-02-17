/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.core.bungee.commands;

import lombok.Getter;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 14.01.2018  02:02.
 */
@Getter
class LobbyCommand extends Command
{
	LobbyCommand(Plugin plugin)
	{
		super(CommandUtils.getName(LobbyCommand.class), null, "Hub", "l", "leave", "quit");
		CommandUtils.register(plugin, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (!(sender instanceof ProxiedPlayer))
		{
			sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "onlyforplayers"));
		}
		else if (args == null || args.length == 0)
		{
			ProxiedPlayer player = ((ProxiedPlayer) sender);
			if (player.getServer().getInfo().getName().split("-")[0].equalsIgnoreCase("lobby"))
			{
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Du bist schon auf einer Lobby"));
			}
			else
			{
				player.connect(BungeeCord.getInstance().getServerInfo("fallback"));
			}
		}
		else
		{
			sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName() + TEXT + ""));
		}
	}
}
