package net.darkblocks.cores;

import com.segdogames.segdocloudplugin.api.CloudAPI;
import com.segdogames.segdocloudplugin.spigot.Bootstrap;
import com.segdogames.segdocloudplugin.spigot.utils.ServerPing;
import net.craftplugin.craftpluginapi.java.utils.ServerState;
import net.craftplugin.craftpluginapi.spigot.events.ServerStateChangeEvent;
import net.craftplugin.craftpluginapi.spigot.plugin.CraftPlugin;
import net.darkblocks.core.spigot.Core;
import net.darkblocks.cores.listener.CountdownListener;
import org.bukkit.Bukkit;

import static net.craftplugin.craftpluginapi.universal.messages.Colors.*;

/**
 * Created by LartyHD on 03.01.2018  11:14.
 */
public class Cores extends CraftPlugin
{
	@Override
	public synchronized void onEnable()
	{
		super.onEnable();
		new Core(this);
		new CountdownListener(getPrefix(getName()), this);
		Bukkit.getPluginManager().callEvent(new ServerStateChangeEvent(ServerState.STARTUP, ServerState.LOBBY));
		CloudAPI.get().getPingAPI().setPing(new ServerPing(CloudAPI.get().getNameAPI().getServerName(), 0, Bukkit.getMaxPlayers(), com.segdogames.segdocloudplugin.spigot.utils.ServerState.LOBBY, "Vote", ""));
		Bootstrap.getINSTANCE().getPingManager().configure();
	}
	
	@SuppressWarnings("StringBufferReplaceableByString")
	public String getPrefix(String name)
	{
		return new StringBuffer().append(IMPORTANT).append(EXTRA).append("[").append(PRIMARY).append(EXTRA).append(name).append(IMPORTANT).append(EXTRA).append("] §r").toString();
	}
}
