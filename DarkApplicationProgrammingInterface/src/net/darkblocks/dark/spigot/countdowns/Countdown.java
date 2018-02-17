/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.countdowns;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Countdown
{
	private int seconds;
	private int taskID;
	private boolean running;
	
	Countdown(int seconds)
	{
		this.seconds = seconds;
	}
	
	public abstract void start();
	
	public abstract void stop();
}
