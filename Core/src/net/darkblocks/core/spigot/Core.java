package net.darkblocks.core.spigot;

import net.craftplugin.craftpluginapi.java.config.PropertiesConfig;
import net.craftplugin.craftpluginapi.java.mysql.MySQL;
import net.craftplugin.craftpluginapi.spigot.events.EventsManager;
import net.craftplugin.craftpluginapi.spigot.messages.Messages;
import net.darkblocks.core.spigot.fix.bungeehack.BungeeHack;
import net.darkblocks.core.spigot.fix.chat.AsyncPlayerChatEventFix;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by LartyHD on 09.01.2018  08:32.
 */
public class Core
{
	public Core(JavaPlugin javaPlugin)
	{
		new EventsManager(javaPlugin);
		new AsyncPlayerChatEventFix(javaPlugin);
		@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
		PropertiesConfig properties = new PropertiesConfig(new File("databases"), "mysql.properties");
		MySQL mySQL = new MySQL((String) properties.get("Host"), (String) properties.get("Port"), (String) properties.get("Username"), (String) properties.get("Password"), (String) properties.get("Database"));
		new BungeeHack(mySQL, new Messages(), javaPlugin);
		//		@SuppressWarnings("StringBufferReplaceableByString")
//		String prefix = new StringBuffer().append(IMPORTANT).append(EXTRA).append("[").append(PRIMARY).append(EXTRA).append("Core").append(IMPORTANT).append(EXTRA).append("] §r").toString();
	}
}
