package net.darkblocks.dark.spigot.events;

import lombok.Getter;
import net.darkblocks.dark.spigot.countdowns.Countdown;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by LartyHD on 01.12.2017  05:27.
 */
@Getter
public abstract class CountdownEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private final Countdown countdown;
	
	public CountdownEvent(Countdown countdown)
	{
		this.countdown = countdown;
	}
	
	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}
}
