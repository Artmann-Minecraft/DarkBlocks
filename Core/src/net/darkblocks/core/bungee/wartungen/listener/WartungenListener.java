package net.darkblocks.core.bungee.wartungen.listener;

import lombok.Getter;
import net.darkblocks.core.bungee.wartungen.Wartungen;
import net.darkblocks.dark.universal.messages.Messages;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 09.01.2018  09:45.
 */
@Getter
public class WartungenListener implements Listener
{
	private final Wartungen wartungen;
	
	public WartungenListener(Wartungen wartungen)
	{
		this.wartungen = wartungen;
		BungeeCord.getInstance().getPluginManager().registerListener(getWartungen().getPlugin(), this);
	}
	
	@EventHandler
	public void onPostLoginEvent(PostLoginEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		if (getWartungen().isOn() && !player.hasPermission("dark.core.bungee.wartungen.bypass"))
		{
			player.disconnect(new TextComponent(PRIMARY + Messages.getInstance().getMessage(Messages.getInstance().getPathPrefix(), "servername") + TEXT + " bedindet sich im " + IMPORTANT + "Wartungsmodus\n" + TEXT + "Das Betreten des Netztwerkes ist derzeit deswegen nicht möglich"));
		}
	}
}
