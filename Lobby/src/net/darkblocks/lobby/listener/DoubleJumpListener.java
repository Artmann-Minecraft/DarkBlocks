/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.lobby.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DoubleJumpListener implements Listener
{
	public DoubleJumpListener(JavaPlugin javaPlugin)
	{
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		if ((player.isOnGround()) && (!player.getAllowFlight()))
		{
			player.setAllowFlight(true);
		}
	}
	
	@EventHandler
	public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent event)
	{
		Player player = event.getPlayer();
		if ((player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) && !player.isFlying())
		{
			event.setCancelled(true);
			player.setFlying(false);
			player.setAllowFlight(false);
			player.setFallDistance(0.0F);
			if (event.getPlayer().getItemInHand() == null || event.getPlayer().getItemInHand().getType() != Material.FEATHER)
			{
				player.setVelocity(player.getLocation().getDirection().multiply(3D).setY(1D));
				player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
			}
		}
	}
}
