package net.darkblocks.core.bungee.playercount.listener;

import lombok.Getter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.event.EventHandler;

import java.util.List;

/**
 * Created by LartyHD on 14.02.2018  19:30.
 */
@Getter
public class PlayerCountListener implements Listener
{
	private final List<String> player;
	
	public PlayerCountListener(Plugin plugin, List<String> player)
	{
		this.player = player;
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
	}
	
	@EventHandler
	public void onLoginEvent(LoginEvent event)
	{
		if (getPlayer().contains(event.getConnection().getName()))
		{
			getPlayer().add(event.getConnection().getName());
		}
	}
}
