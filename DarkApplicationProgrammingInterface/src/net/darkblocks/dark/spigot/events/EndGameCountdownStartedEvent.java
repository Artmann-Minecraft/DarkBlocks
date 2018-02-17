/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.events;

import net.darkblocks.dark.spigot.countdowns.EndGameCountdown;

/**
 * Created by LartyHD on 01.12.2017  05:27.
 */
public class EndGameCountdownStartedEvent extends CountdownStartedEvent
{
	public EndGameCountdownStartedEvent(EndGameCountdown countdown)
	{
		super(countdown);
	}
}
