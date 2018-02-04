/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.core.bungee.commands;

import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import static net.darkblocks.dark.universal.messages.Colors.*;

public class ListCommand extends Command
{
	@SuppressWarnings("SameParameterValue")
	public ListCommand(Plugin plugin)
	{
		super(CommandUtils.getName(ListCommand.class));
		CommandUtils.register(plugin, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		ServerInfo info = ((ProxiedPlayer) sender).getServer().getInfo();
		sender.sendMessage(new TextComponent("" + IMPORTANT + EXTRA + DESIGN + "                                                               "));
		sender.sendMessage(new TextComponent(TEXT + "Spieler auf dem " + IMPORTANT + "Netzwerk" + TEXT + ": " + IMPORTANT + BungeeCord.getInstance().getOnlineCount()));
		sender.sendMessage(new TextComponent(TEXT + "Spieler auf " + IMPORTANT + info.getName() + TEXT + ":" + IMPORTANT + " " + info.getPlayers().size()));
		sender.sendMessage(new TextComponent("" + IMPORTANT + EXTRA + DESIGN + "                                                               "));
	}
}
