package net.darkblocks.core.bungee.joinme.commands;

import lombok.Getter;
import net.craftplugin.craftpluginapi.universal.messages.Colors;
import net.craftplugin.craftpluginapi.universal.messages.Messages;
import net.craftplugin.craftpluginapi.universal.utils.CommandUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import static net.craftplugin.craftpluginapi.universal.messages.Colors.IMPORTANT;
import static net.craftplugin.craftpluginapi.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 15.01.2018  01:12.
 */
@Getter
public class JoinMeCommand extends Command
{
	public JoinMeCommand(Plugin plugin)
	{
		super(CommandUtils.getName(JoinMeCommand.class), CommandUtils.getPermission(JoinMeCommand.class), "join");
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
				String serverName = ((ProxiedPlayer) sender).getServer().getInfo().getName();
				TextComponent textComponent = new TextComponent();
				textComponent.setText(Colors.IMPORTANT + sender.getName() + Colors.TEXT + " spielt auf " + Colors.IMPORTANT + serverName + " " + Colors.PRIMARY + "[NACHJOINEN]");
				textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/executejoinme " + serverName));
				for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers())
				{
					players.sendMessage(new TextComponent("" + Colors.IMPORTANT + Colors.EXTRA + Colors.DESIGN + "                                                               "));
					players.sendMessage(new TextComponent(" "));
					players.sendMessage(textComponent);
					players.sendMessage(new TextComponent(" "));
					players.sendMessage(new TextComponent("" + Colors.IMPORTANT + Colors.EXTRA + Colors.DESIGN + "                                                               "));
				}
			}
			else
			{
				sender.sendMessage(new TextComponent(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName() + TEXT + "")));
			}
		}
	}
}
