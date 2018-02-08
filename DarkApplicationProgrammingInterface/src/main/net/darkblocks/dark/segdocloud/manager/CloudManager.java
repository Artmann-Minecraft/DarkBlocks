package net.darkblocks.dark.segdocloud.manager;

import com.segdogames.segdocloudplugin.api.CloudAPI;
import com.segdogames.segdocloudplugin.spigot.Bootstrap;
import com.segdogames.segdocloudplugin.spigot.utils.ServerPing;
import lombok.Getter;
import lombok.Setter;
import net.darkblocks.dark.java.utils.ServerState;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import net.darkblocks.dark.spigot.events.ServerStateChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 08.02.2018  07:53.
 */
@Getter
@Setter
public class CloudManager implements Listener
{
	private ServerState serverState;
	private String extra;
	
	public CloudManager(JavaPlugin javaPlugin, String extra)
	{
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
		this.serverState = ServerState.LOBBY;
		this.extra = extra;
		refreshPing(0, this.serverState, "");
		Bootstrap.getINSTANCE().getPingManager().configure();
	}
	
	public static void refreshPing(int players, ServerState serverState, String extra)
	{
		CloudAPI.get().getPingAPI().setPing(new ServerPing(CloudAPI.get().getNameAPI().getServerName(), players, Bukkit.getMaxPlayers(), serverState.getServerState(), extra, ""));
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		refreshPing(Bukkit.getOnlinePlayers().size());
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
	{
		refreshPing(Bukkit.getOnlinePlayers().size() - 1);
	}
	
	@EventHandler
	public void onServerStateChangeEvent(ServerStateChangeEvent event)
	{
		this.serverState = event.getNewServerState();
		refreshPing(Bukkit.getOnlinePlayers().size());
	}
	
	public void refreshPing(int players)
	{
		CloudAPI.get().getPingAPI().setPing(new ServerPing(CloudAPI.get().getNameAPI().getServerName(), players, Bukkit.getMaxPlayers(), this.serverState.getServerState(), this.extra, ""));
	}
}
