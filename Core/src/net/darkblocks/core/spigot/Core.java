package net.darkblocks.core.spigot;

import net.darkblocks.core.spigot.fix.bungeehack.BungeeHack;
import net.darkblocks.core.spigot.fix.chat.AsyncPlayerChatEventFix;
import net.darkblocks.dark.java.config.PropertiesConfig;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.spigot.events.listener.EventsListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by LartyHD on 09.01.2018  08:32.
 */
public class Core
{
	public Core(JavaPlugin javaPlugin)
	{
		new EventsListener(javaPlugin);
		new AsyncPlayerChatEventFix(javaPlugin);
		@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
		PropertiesConfig properties = new PropertiesConfig(new File("databases"), "mysql.properties");
		MySQL mySQL = new MySQL((String) properties.get("Host"), (String) properties.get("Port"), (String) properties.get("Username"), (String) properties.get("Password"), (String) properties.get("Database"));
		new BungeeHack(mySQL, javaPlugin);
		//		@SuppressWarnings("StringBufferReplaceableByString")
//		String prefix = new StringBuffer().append(IMPORTANT).append(EXTRA).append("[").append(PRIMARY).append(EXTRA).append("Core").append(IMPORTANT).append(EXTRA).append("] §r").toString();
	}
}
