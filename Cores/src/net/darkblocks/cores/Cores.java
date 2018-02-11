package net.darkblocks.cores;

import net.darkblocks.core.spigot.Core;
import net.darkblocks.core.universal.permissions.manager.GroupManager;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.cores.listener.CountdownListener;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.java.utils.ServerState;
import net.darkblocks.dark.segdocloud.manager.CloudManager;
import net.darkblocks.dark.spigot.events.ServerStateChangeEvent;
import net.darkblocks.dark.spigot.plugin.DarkPlugin;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

import static net.darkblocks.dark.universal.messages.Colors.EXTRA;
import static net.darkblocks.dark.universal.messages.Colors.PRIMARY;

/**
 * Created by LartyHD on 03.01.2018  11:14.
 */
public class Cores extends DarkPlugin
{
	@Override
	public synchronized void onEnable()
	{
		super.onEnable();
		Map<String, String> messages = new HashMap<>();
		messages.put("dark.prefix", "§f" + EXTRA + "[" + PRIMARY + EXTRA + "Cores§f" + EXTRA + "] §r");
		messages.put("dark.servername", "" + PRIMARY + EXTRA + "DarkBlocks§f" + EXTRA + "." + PRIMARY + EXTRA + "Net");
		new Messages(messages);
		MySQL mySQL = new MySQL();
		new Core(this, mySQL, new UserManager(mySQL, null), new GroupManager(mySQL, null));
		new CountdownListener(this);
		new CloudManager(this, "- - -");
		Bukkit.getPluginManager().callEvent(new ServerStateChangeEvent(ServerState.STARTUP, ServerState.LOBBY));
	}
}
