package net.darkblocks.dark.spigot.events.cashed;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by LartyHD on 22.01.2018  00:13.
 */
@Getter
public class CashedEventsManager implements Listener
{
	private final Set<CashedEvent> cashedEvents;
	
	public CashedEventsManager(JavaPlugin javaPlugin) throws IllegalArgumentException
	{
		this.cashedEvents = new HashSet<>();
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event)
	{
		for (CashedEvent cashedEvent : getCashedEvents())
		{
			if (cashedEvent instanceof CashedInventoryClickEvent)
			{
				((CashedInventoryClickEvent) cashedEvent).onCashedInventoryClickEvent(event);
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event)
	{
		for (CashedEvent cashedEvent : getCashedEvents())
		{
			if (cashedEvent instanceof CashedPlayerInteractEvent)
			{
				((CashedPlayerInteractEvent) cashedEvent).onCashedPlayerInteractEvent(event);
			}
		}
	}
	
	public void register(CashedEvent cashedEvent)
	{
		getCashedEvents().add(cashedEvent);
	}
}
