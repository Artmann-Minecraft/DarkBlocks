/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.dark.spigot.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Created by LartyHD on 03.09.2017  18:49.
 * Project: RedStone
 */
@Getter
@Setter
public class PlayerUpdateCoinsEvent extends PlayerEvent
{
	private static final HandlerList handlers = new HandlerList();
	private int coins;
	
	public PlayerUpdateCoinsEvent(Player who, int coins)
	{
		super(who);
		this.coins = coins;
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
