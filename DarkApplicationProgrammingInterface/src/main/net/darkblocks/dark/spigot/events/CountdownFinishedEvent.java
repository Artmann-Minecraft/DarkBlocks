package net.darkblocks.dark.spigot.events;

import net.darkblocks.dark.spigot.countdowns.Countdown;

/**
 * Created by LartyHD on 01.12.2017  05:27.
 */
@SuppressWarnings("ALL")
public abstract class CountdownFinishedEvent extends CountdownEvent
{
	public CountdownFinishedEvent(Countdown countdown)
	{
		super(countdown);
	}
}
