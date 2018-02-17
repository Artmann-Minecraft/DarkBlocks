/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.events;

import net.darkblocks.dark.spigot.countdowns.LobbyCountdown;

/**
 * Created by LartyHD on 01.12.2017  04:55.
 */
public class LobbyCountdownPreFinishedEvent extends CountdownFinishedEvent
{
	private boolean next;
	
	public LobbyCountdownPreFinishedEvent(LobbyCountdown countdown)
	{
		super(countdown);
	}
	
	public boolean isNext()
	{
		return this.next;
	}
	
	public void setNext(boolean next)
	{
		this.next = next;
	}
}
