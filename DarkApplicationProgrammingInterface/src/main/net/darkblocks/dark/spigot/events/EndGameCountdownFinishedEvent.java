package net.darkblocks.dark.spigot.events;

import net.darkblocks.dark.spigot.countdowns.EndGameCountdown;

/**
 * Created by LartyHD on 01.12.2017  05:27.
 */
public class EndGameCountdownFinishedEvent extends CountdownFinishedEvent
{
	public EndGameCountdownFinishedEvent(EndGameCountdown countdown)
	{
		super(countdown);
	}
}
