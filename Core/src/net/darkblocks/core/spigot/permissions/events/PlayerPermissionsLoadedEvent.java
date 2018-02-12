package net.darkblocks.core.spigot.permissions.events;

import lombok.Getter;
import net.darkblocks.core.universal.permissions.utils.User;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Created by LartyHD on 12.02.2018  12:35.
 */
@Getter
public class PlayerPermissionsLoadedEvent extends PlayerEvent
{
	private static final HandlerList handlers = new HandlerList();
	private final User user;
	
	public PlayerPermissionsLoadedEvent(Player who, User user)
	{
		super(who);
		this.user = user;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}
}
