/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.events.cashed;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by LartyHD on 22.01.2018  00:08.
 */
public interface CashedPlayerInteractEvent extends CashedEvent
{
	@EventHandler
	void onCashedPlayerInteractEvent(PlayerInteractEvent event);
}
