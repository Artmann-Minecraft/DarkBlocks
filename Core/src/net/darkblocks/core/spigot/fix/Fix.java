package net.darkblocks.core.spigot.fix;

import net.darkblocks.core.spigot.fix.bungeehack.BungeeHack;
import net.darkblocks.core.spigot.fix.chat.AsyncPlayerChatEventFix;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 09.02.2018  03:58.
 */
public class Fix
{
	public Fix(JavaPlugin javaPlugin)
	{
		new AsyncPlayerChatEventFix(javaPlugin);
		new BungeeHack(javaPlugin);
	}
}
