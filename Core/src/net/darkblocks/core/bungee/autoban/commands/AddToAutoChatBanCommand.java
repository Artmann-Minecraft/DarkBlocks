package net.darkblocks.core.bungee.autoban.commands;

import lombok.Getter;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.universal.messages.Messages;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 09.01.2018  09:24.
 */
@Getter
public class AddToAutoChatBanCommand extends Command
{
	private final MySQL mySQL;
	
	public AddToAutoChatBanCommand(Plugin plugin, MySQL mySQL)
	{
		super(AddToAutoChatBanCommand.class.getName().replaceAll("Command", ""), Messages.getInstance().getPath(AddToAutoChatBanCommand.class) + AddToAutoChatBanCommand.class.getName() + ".use");
		this.mySQL = mySQL;
		BungeeCord.getInstance().getPluginManager().registerCommand(plugin, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		switch (args.length)
		{
			case 1:
				getMySQL().update("INSERT INTO `ChatAutoBan` (`name`) VALUES ('" + args[0] + "')");
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Du hast " + IMPORTANT + args[0] + TEXT + " zum AutoChatBan-System hinzugefügt"));
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Ab dem nächsten " + IMPORTANT + "Proxy" + TEXT + " restart ist es geblockt"));
				break;
			default:
				sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName() + TEXT + "<Nachricht>"));
				break;
		}
	}
}
