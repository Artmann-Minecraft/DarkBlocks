/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblock.lobby.extras.cookieclicker.listener;

import lombok.Getter;
import net.darkblock.lobby.extras.cookieclicker.CookieClicker;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import net.darkblocks.dark.spigot.utils.PackageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.UUID;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 13.02.2018  14:31.
 */
@Getter
public class CookieListener implements Listener
{
	private final CookieClicker cookieClicker;
	private final JavaPlugin javaPlugin;
	
	public CookieListener(JavaPlugin javaPlugin, CookieClicker cookieClicker, CoinsAPI coinsAPI)
	{
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
		this.cookieClicker = cookieClicker;
		this.javaPlugin = javaPlugin;
		new CookieShopListener(javaPlugin, cookieClicker, coinsAPI);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		final UUID uuid = event.getPlayer().getUniqueId();
		this.getCookieClicker().getMySQL().query("SELECT * FROM Cookies WHERE uuid = '" + uuid.toString() + "'", result -> {
			try
			{
				if (result.next())
				{
					getCookieClicker().getCookies().put(uuid, result.getDouble("coins"));
				}
			} catch (SQLException ignored)
			{
			} finally
			{
				if (getCookieClicker().getCookies().get(uuid) == null)
				{
					getCookieClicker().getCookies().put(uuid, 0D);
					this.getCookieClicker().getMySQL().update("INSERT INTO Cookies (uuid, coins) values ('" + uuid.toString() + "', '0')");
				}
			}
		});
		this.getCookieClicker().getMySQL().query("SELECT * FROM CookiesPerClick WHERE uuid = '" + uuid.toString() + "'", result -> {
			try
			{
				if (result.next())
				{
					getCookieClicker().getCookiesPerClick().put(uuid, result.getDouble("coins"));
				}
			} catch (SQLException ignored)
			{
				ignored.printStackTrace();
			} finally
			{
				if (getCookieClicker().getCookiesPerClick().get(uuid) == null)
				{
					getCookieClicker().getCookiesPerClick().put(uuid, 0D);
					this.getCookieClicker().getMySQL().update("INSERT INTO CookiesPerClick (uuid, coins) values ('" + uuid.toString() + "', '1')");
				}
			}
		});
	}
	
	@EventHandler
	public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event)
	{
		if (event.getRightClicked() != null && event.getRightClicked().getCustomName() != null && org.bukkit.ChatColor.stripColor(event.getRightClicked().getCustomName()).equalsIgnoreCase("CookieClicker"))
		{
			event.setCancelled(true);
			addCoin(event.getPlayer(), event.getRightClicked().getLocation().add(0, 2.5, 0));
		}
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
	{
		UUID uuid = event.getPlayer().getUniqueId();
		getCookieClicker().getMySQL().update("UPDATE Cookies SET coins='" + getCookieClicker().getCookies().get(uuid) + "' WHERE uuid='" + uuid.toString() + "'", () -> getCookieClicker().getCookies().remove(uuid));
		getCookieClicker().getMySQL().update("UPDATE CookiesPerClick SET coins='" + getCookieClicker().getCookiesPerClick().get(uuid) + "' WHERE uuid='" + uuid + "'", () -> getCookieClicker().getCookiesPerClick().remove(uuid));
	}
	
	private void addCoin(Player player, Location location)
	{
		UUID uuid = player.getUniqueId();
		if (!getCookieClicker().getBlockedClicks().contains(uuid))
		{
			getCookieClicker().getBlockedClicks().add(uuid);
			Bukkit.getScheduler().runTaskLaterAsynchronously(getJavaPlugin(), () -> getCookieClicker().getBlockedClicks().remove(uuid), 2);
			Item item = location.getWorld().dropItemNaturally(location, new ItemStack(Material.COOKIE));
			new Thread(() -> {
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException ex)
				{
					ex.printStackTrace();
				} finally
				{
					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							item.remove();
						}
					}.runTask(getJavaPlugin());
				}
			}).start();
			String subtitle = "§4FEHLER!";
			if (getCookieClicker().getCookies().get(uuid) != null)
			{
				getCookieClicker().getCookies().put(uuid, getCookieClicker().getCookies().get(uuid) + getCookieClicker().getCookiesPerClick().get(uuid));
				subtitle = String.valueOf(Math.round(100.0 * getCookieClicker().getCookies().get(uuid)) / 100.0);
				if (subtitle.length() > 2)
				{
					subtitle = (subtitle.substring(0, subtitle.length() - 2) + "," + subtitle.substring(subtitle.length() - 2)).replace(".", "");
					if (subtitle.length() > 6)
					{
						subtitle = subtitle.substring(0, subtitle.length() - 6) + "." + subtitle.substring(subtitle.length() - 6);
						if (subtitle.length() > 10)
						{
							subtitle = subtitle.substring(0, subtitle.length() - 10) + "." + subtitle.substring(subtitle.length() - 10);
							if (subtitle.length() > 14)
							{
								subtitle = subtitle.substring(0, subtitle.length() - 14) + "." + subtitle.substring(subtitle.length() - 14);
							}
						}
					}
				}
			}
			PackageUtils.sendTitle(player, "" + PRIMARY + EXTRA + "CookieClicker", TEXT + "" + subtitle + IMPORTANT + " Cookies", 0, 20, 10);
		}
	}
}
