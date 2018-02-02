package net.darkblocks.core.spigot.fix.chat;

import net.darkblocks.core.spigot.fix.chat.listener.AsyncPlayerChatEventFixListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 09.01.2018  22:28.
 */
public class AsyncPlayerChatEventFix
{
	public AsyncPlayerChatEventFix(JavaPlugin javaPlugin)
	{
		new AsyncPlayerChatEventFixListener(javaPlugin);
	}
}
