package net.darkblocks.core.bungee.coins;

import net.darkblocks.core.bungee.coins.commands.CoinsCommand;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.md_5.bungee.api.plugin.Plugin;

public class Coins
{
	public Coins(Plugin plugin, CoinsAPI coinsAPI)
	{
		new CoinsCommand(plugin, coinsAPI);
	}
}
