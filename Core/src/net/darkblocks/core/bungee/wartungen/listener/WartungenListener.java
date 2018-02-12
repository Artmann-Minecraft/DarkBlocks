package net.darkblocks.core.bungee.wartungen.listener;

import lombok.Getter;
import net.darkblocks.core.bungee.permissions.events.PermissionsLoadedEvent;
import net.darkblocks.core.bungee.wartungen.Wartungen;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
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
	public void onPermissionsLoadedEvent(PermissionsLoadedEvent event)
	{
		PendingConnection connection = event.getConnection();
		if (getWartungen().isOn() && !getWartungen().getWhitelist().contains(connection.getName().toLowerCase()))
		{
			if (!getWartungen().getUserManager().hasPermission(connection.getUniqueId(), CommandUtils.getPermission(getClass())))
			{
				connection.disconnect(new TextComponent(PRIMARY + Messages.getInstance().getShortMessage(getClass(), "servername") + TEXT + " befindet sich im " + IMPORTANT + "Wartungsmodus\n" + TEXT + "Das Betreten des Netztwerkes ist derzeit deswegen nicht möglich"));
			}
		}
	}
}
