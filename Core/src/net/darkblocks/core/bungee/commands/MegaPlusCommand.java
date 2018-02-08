/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.core.bungee.commands;

import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

public class MegaPlusCommand extends Command
{
	MegaPlusCommand(Plugin plugin)
	{
		super(CommandUtils.getName(MegaPlusCommand.class), null, "mega+", "premium+", "premiumplus");
		CommandUtils.register(plugin, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		sender.sendMessage(new TextComponent(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Vorraussetzungen für " + IMPORTANT + "Mega+" + TEXT + ":")));
		sender.sendMessage(new TextComponent(TEXT + "-> Dein " + IMPORTANT + "Kanal " + TEXT + "muss mindestens " + IMPORTANT + "500 Abonnenten" + TEXT + " besitzen."));
		sender.sendMessage(new TextComponent(TEXT + "-> Du benötigst 250 Klicks nach 24 Stunden auf dein Video."));
		sender.sendMessage(new TextComponent(TEXT + "-> Dieses Video muss eine Server vorstellung oder der\n" + TEXT + "    Server Trailer sein."));
		sender.sendMessage(new TextComponent(TEXT + "-> Dieses Video muss in angemessener Qualität und Länge sein."));
		sender.sendMessage(new TextComponent(TEXT + "-> Alles erfüllt? Bewerbe dich für den " + IMPORTANT + "Youtuber " + TEXT + "Rang im TS"));
	}
}
