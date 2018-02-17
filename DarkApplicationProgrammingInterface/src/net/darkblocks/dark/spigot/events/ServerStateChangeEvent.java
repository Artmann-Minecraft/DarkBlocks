/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.dark.spigot.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.darkblocks.dark.java.utils.ServerState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by LartyHD on 29.11.2017  14:21.
 */
@Getter
@Setter
@AllArgsConstructor
public class ServerStateChangeEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	@NonNull
	private ServerState oldServerState;
	@NonNull
	private ServerState newServerState;
	
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
