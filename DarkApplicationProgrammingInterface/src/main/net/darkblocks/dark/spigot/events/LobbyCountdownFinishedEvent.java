package net.darkblocks.dark.spigot.events;

import net.darkblocks.dark.spigot.countdowns.LobbyCountdown;

/**
 * Created by LartyHD on 01.12.2017  04:55.
 */
public class LobbyCountdownFinishedEvent extends CountdownFinishedEvent
{
	public LobbyCountdownFinishedEvent(LobbyCountdown countdown)
	{
		super(countdown);
	}
}
