/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.dark.spigot.events;

import lombok.Getter;
import net.darkblocks.dark.spigot.countdowns.Countdown;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by LartyHD on 01.12.2017  05:27.
 */
@Getter
class CountdownEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private final Countdown countdown;
	
	CountdownEvent(Countdown countdown)
	{
		this.countdown = countdown;
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
