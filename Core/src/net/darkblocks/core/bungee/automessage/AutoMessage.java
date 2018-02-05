package net.darkblocks.core.bungee.automessage;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 05.02.2018  18:36.
 */
public class AutoMessage
{
	public AutoMessage(Plugin plugin)
	{
		List<List<TextComponent>> list = Arrays.asList(Arrays.asList(new TextComponent(""), new TextComponent(TEXT + "Interesse am " + IMPORTANT + "Youtuber " + TEXT + "oder " + IMPORTANT + "Mega+ " + TEXT + "Rang?"), new TextComponent(TEXT + "Dann mach doch mal " + IMPORTANT + "/YT " + TEXT + "oder " + IMPORTANT + "/Mega+"), new TextComponent("")));
		BungeeCord.getInstance().getScheduler().runAsync(plugin, () -> {
			try
			{
				while (true)
				{
					for (List<TextComponent> textComponents : list)
					{
						for (TextComponent textComponent : textComponents)
						{
							for (ProxiedPlayer players : BungeeCord.getInstance().getPlayers())
							{
								players.sendMessage(textComponent);
							}
						}
						Thread.sleep(300000);
					}
				}
			} catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
		});
	}
}
