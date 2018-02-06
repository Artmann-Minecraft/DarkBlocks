package net.darkblocks.cores.events;

import lombok.Getter;
import net.darkblocks.cores.utils.Core;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by LartyHD on 06.02.2018  01:49.
 */
@Getter
public class CoreAttackedEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private final Core core;
	
	public CoreAttackedEvent(Core core)
	{
		this.core = core;
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
