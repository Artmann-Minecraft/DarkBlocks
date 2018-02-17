/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.events.cashed;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by LartyHD on 22.01.2018  00:12.
 */
public interface CashedInventoryClickEvent extends CashedEvent
{
	@EventHandler
	void onCashedInventoryClickEvent(InventoryClickEvent event);
}
