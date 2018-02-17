/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.events;

import net.darkblocks.dark.spigot.countdowns.PreGameCountdown;

/**
 * Created by LartyHD on 01.12.2017  05:27.
 */
public class PreGameCountdownStartedEvent extends CountdownStartedEvent
{
	public PreGameCountdownStartedEvent(PreGameCountdown countdown)
	{
		super(countdown);
	}
}
