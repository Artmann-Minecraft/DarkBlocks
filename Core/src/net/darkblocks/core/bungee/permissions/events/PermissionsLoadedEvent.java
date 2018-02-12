package net.darkblocks.core.bungee.permissions.events;

import lombok.Getter;
import lombok.NonNull;
import net.darkblocks.core.universal.permissions.utils.User;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.plugin.Event;

/**
 * Created by LartyHD on 12.02.2018  12:35.
 */
@Getter
public class PermissionsLoadedEvent extends Event
{
	private final User user;
	private final PendingConnection connection;
	
	public PermissionsLoadedEvent(@NonNull PendingConnection connection, User user)
	{
		this.user = user;
		this.connection = connection;
	}
}
