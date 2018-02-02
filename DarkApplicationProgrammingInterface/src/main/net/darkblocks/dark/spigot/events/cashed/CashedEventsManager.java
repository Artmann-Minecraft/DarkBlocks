package net.darkblocks.dark.spigot.events.cashed;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LartyHD on 22.01.2018  00:13.
 */
@Getter
public class CashedEventsManager implements Listener
{
	private static CashedEventsManager instance;
	private final List<CashedEvent> cashedEvents;
	
	public CashedEventsManager(JavaPlugin javaPlugin) throws IllegalArgumentException
	{
		if (instance != null)
		{
			throw new IllegalArgumentException();
		}
		instance = this;
		this.cashedEvents = new ArrayList<>();
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
	
	public static CashedEventsManager getInstance()
	{
		return instance;
	}
	
	@SuppressWarnings({"SingleStatementInBlock", "unchecked"})
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event)
	{
		for (CashedEvent cashedEvent : getCashedEvents())
		{
			if (cashedEvent instanceof CashedInventoryClickEvent)
			{
				cashedEvent.flush(event);
			}
		}
	}
	
	public void register(CashedEvent cashedEvent)
	{
		getCashedEvents().add(cashedEvent);
	}
}
