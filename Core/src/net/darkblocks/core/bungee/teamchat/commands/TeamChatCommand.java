package net.darkblocks.core.bungee.teamchat.commands;

import lombok.Getter;
import net.darkblocks.core.bungee.teamchat.TeamChat;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 19.01.2018  23:33.
 */
@Getter
public class TeamChatCommand extends Command
{
	private final TeamChat teamChat;
	
	public TeamChatCommand(Plugin plugin, TeamChat teamChat)
	{
		super(CommandUtils.getName(TeamChatCommand.class), CommandUtils.getPermission(TeamChatCommand.class), "tc");
		this.teamChat = teamChat;
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
				if (getTeamChat().getPlayers().contains(sender))
				{
					getTeamChat().getPlayers().remove(sender);
					sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Du bist jetzt nicht mehr im " + IMPORTANT + "TeamChat " + TEXT + "eingeloggt"));
				}
				else
				{
					getTeamChat().getPlayers().add((ProxiedPlayer) sender);
					sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Du bist jetzt im " + IMPORTANT + "TeamChat " + TEXT + "eingeloggt"));
				}
			}
			else
			{
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName()));
			}
		}
	}
}
