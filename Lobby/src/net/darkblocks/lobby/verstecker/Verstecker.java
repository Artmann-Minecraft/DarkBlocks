/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.lobby.verstecker;

import net.darkblocks.dark.spigot.events.cashed.CashedEventsManager;
import net.darkblocks.lobby.verstecker.listener.VersteckerListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 08.02.2018  10:11.
 */
public class Verstecker
{
	public Verstecker(JavaPlugin javaPlugin, CashedEventsManager cashedEventsManager)
	{
		new VersteckerListener(javaPlugin, cashedEventsManager);
	}
}
