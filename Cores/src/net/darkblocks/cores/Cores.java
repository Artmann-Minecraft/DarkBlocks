package net.darkblocks.cores;

import com.segdogames.segdocloudplugin.api.CloudAPI;
import com.segdogames.segdocloudplugin.spigot.Bootstrap;
import com.segdogames.segdocloudplugin.spigot.utils.ServerPing;
import net.darkblocks.core.spigot.Core;
import net.darkblocks.cores.listener.CountdownListener;
import net.darkblocks.dark.java.utils.ServerState;
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
		new Core(this);
		new CountdownListener(this);
		Bukkit.getPluginManager().callEvent(new ServerStateChangeEvent(ServerState.STARTUP, ServerState.LOBBY));
		CloudAPI.get().getPingAPI().setPing(new ServerPing(CloudAPI.get().getNameAPI().getServerName(), 0, Bukkit.getMaxPlayers(), com.segdogames.segdocloudplugin.spigot.utils.ServerState.LOBBY, "Vote", ""));
		Bootstrap.getINSTANCE().getPingManager().configure();
	}
}
