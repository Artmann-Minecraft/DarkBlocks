/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Created by LartyHD on 29.11.2017  14:21.
 */
public class PlayerDisconnectEvent extends PlayerEvent
{
	private static final HandlerList handlers = new HandlerList();
	
	public PlayerDisconnectEvent(Player who)
	{
		super(who);
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
