package net.darkblocks.dark.spigot.events;

import net.darkblocks.dark.spigot.countdowns.LobbyCountdown;

/**
 * Created by LartyHD on 01.12.2017  04:52.
 */
public class LobbyCountdownLastTenSecondsEvent extends CountdownEvent
{
	public LobbyCountdownLastTenSecondsEvent(LobbyCountdown countdown)
	{
		super(countdown);
	}
}
