/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.core.bungee.automessage;

import net.darkblocks.core.bungee.Core;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

import static net.darkblocks.dark.universal.messages.Colors.PRIMARY;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 05.02.2018  18:36.
 */
public class AutoMessage
{
	public AutoMessage(Plugin plugin)
	{
		List<List<TextComponent>> list = Arrays.asList(
				Arrays.asList(new TextComponent(""), new TextComponent(TEXT + "Interesse am " + PRIMARY + "Youtuber " + TEXT + "oder " + PRIMARY + "Mega+ " + TEXT + "Rang?"), new TextComponent(TEXT + "Dann mach doch mal " + PRIMARY + "/YT " + TEXT + "oder " + PRIMARY + "/Mega+"), new TextComponent("")),
				Arrays.asList(new TextComponent(""), new TextComponent(TEXT + "Du willst unserem " + PRIMARY + "Serverteam " + TEXT + "beitreten?"), new TextComponent(TEXT + "Dann bewerbe dich doch im " + PRIMARY + "Forum"), new TextComponent("")));
		BungeeCord.getInstance().getScheduler().runAsync(plugin, () -> {
			try
			{
				while (((Core) plugin).isRun())
				{
					for (List<TextComponent> textComponents : list)
					{
						Thread.sleep(300000);
						for (TextComponent textComponent : textComponents)
						{
							for (ProxiedPlayer players : BungeeCord.getInstance().getPlayers())
							{
								players.sendMessage(textComponent);
							}
						}
					}
				}
			} catch (InterruptedException ignored)
			{
			}
		});
	}
}
