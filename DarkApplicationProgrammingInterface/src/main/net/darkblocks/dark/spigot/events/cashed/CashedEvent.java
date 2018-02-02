package net.darkblocks.dark.spigot.events.cashed;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by LartyHD on 22.01.2018  00:19.
 */
public interface CashedEvent<E> extends Listener
{
	@EventHandler
	void flush(E event);
	
	default void init()
	{
		CashedEventsManager.getInstance().register(this);
	}
}
