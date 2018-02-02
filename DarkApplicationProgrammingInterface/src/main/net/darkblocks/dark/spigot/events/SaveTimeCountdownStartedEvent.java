package net.darkblocks.dark.spigot.events;

import net.darkblocks.dark.spigot.countdowns.SaveTimeCountdown;

/**
 * Created by LartyHD on 01.12.2017  05:27.
 */
public class SaveTimeCountdownStartedEvent extends CountdownStartedEvent
{
	public SaveTimeCountdownStartedEvent(SaveTimeCountdown countdown)
	{
		super(countdown);
	}
}
