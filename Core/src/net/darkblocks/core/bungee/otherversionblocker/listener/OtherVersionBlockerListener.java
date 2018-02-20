/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.core.bungee.otherversionblocker.listener;

import lombok.Getter;
import net.darkblocks.dark.universal.messages.Messages;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 19.01.2018  23:15.
 */
@Getter
public class OtherVersionBlockerListener implements Listener
{
	public OtherVersionBlockerListener(Plugin plugin)
	{
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
	}
	
	@EventHandler
	public void onPreLoginEvent(PreLoginEvent event)
	{
		if (event.getConnection().getVersion() != 47)
		{
			event.setCancelled(true);
			event.setCancelReason(TEXT + "Bitte betrete " + Messages.getInstance().getShortMessage(getClass(), "servername") + TEXT + " mit einer dieser Versionen" + IMPORTANT + ":\n" + "1.8.0, 1.8.1, 1.8.2, 1.8.3, 1.8.4, 1.8.5, 1.8.6, 1.8.7, 1.8.8, 1.8.9");
		}
	}
}
