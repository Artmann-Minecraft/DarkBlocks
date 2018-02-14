package net.darkblocks.core.bungee.playercount.listener;

import lombok.Getter;
import net.darkblocks.core.bungee.playercount.PlayerCount;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

/**
 * Created by LartyHD on 14.02.2018  19:30.
 */
@Getter
public class PlayerCountListener implements Listener
{
	private final PlayerCount playerCount;
	
	public PlayerCountListener(Plugin plugin, PlayerCount playerCount)
	{
		this.playerCount = playerCount;
		System.out.println(2);
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
	}
	
	@EventHandler
	public void onPostLoginEvent(LoginEvent event)
	{
		getPlayerCount().getPlayer().add(event.getConnection().getName());
	}
}
