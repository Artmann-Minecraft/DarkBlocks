/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.core.bungee.coins.commands;

import lombok.Getter;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.darkblocks.dark.universal.utils.fetcher.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 06.02.2018  14:55.
 */
@Getter
public class CoinsCommand extends Command
{
	private final CoinsAPI coinsAPI;
	
	public CoinsCommand(Plugin plugin, CoinsAPI coinsAPI)
	{
		super(CommandUtils.getName(CoinsCommand.class));
		this.coinsAPI = coinsAPI;
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
			switch (args.length)
			{
				case 0:
					this.coinsAPI.getCoins(((ProxiedPlayer) sender).getUniqueId(), result -> sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Du hast " + PRIMARY + result + IMPORTANT + " Coins")));
					break;
				case 1:
					ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
					if (target != null)
					{
						this.coinsAPI.getCoins(target.getUniqueId(), result -> sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + target.getName() + TEXT + " hat " + PRIMARY + result + IMPORTANT + " Coins")));
					}
					else
					{
						sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "notonline"));
						sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Lade Informationen aus Datenbank... "));
						getCoinsAPI().getCoins(UUIDFetcher.getUUID(args[0]), result -> {
							if (result != -1)
							{
								sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + args[0] + TEXT + " hat " + PRIMARY + result + IMPORTANT + " Coins"));
							}
							else
							{
								sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "notindatabase"));
							}
						});
					}
					break;
				default:
					sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName() + TEXT + " [Spieler]"));
					break;
			}
		}
	}
}
