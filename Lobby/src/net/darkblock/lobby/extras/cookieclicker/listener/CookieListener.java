/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblock.lobby.extras.cookieclicker.listener;

import lombok.Getter;
import lombok.NonNull;
import net.darkblock.lobby.extras.cookieclicker.CookieClicker;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.utils.Callback;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import net.darkblocks.dark.spigot.utils.PackageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.text.DecimalFormat;
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
		new CookieShopListener(this, cookieClicker, coinsAPI);
	}
	
	@EventHandler
	public void onPlayerLoginEvent(PlayerLoginEvent event)
	{
		final UUID uuid = event.getPlayer().getUniqueId();
		this.getCookieClicker().getMySQL().query("SELECT * FROM Cookies WHERE uuid = '" + uuid.toString() + "'", result -> {
			try
			{
				if (result.next())
				{
					getCookieClicker().getCookies().put(uuid, result.getDouble("coins"));
				}
				else
				{
					this.getCookieClicker().getMySQL().update("INSERT INTO Cookies (uuid, coins) values ('" + uuid.toString() + "', '0')");
				}
			} catch (SQLException ignored)
			{
			} finally
			{
				getCookieClicker().getCookies().putIfAbsent(uuid, 0D);
			}
		});
		this.getCookieClicker().getMySQL().query("SELECT * FROM CookiesPerClick WHERE uuid = '" + uuid.toString() + "'", result -> {
			try
			{
				if (result.next())
				{
					getCookieClicker().getCookiesPerClick().put(uuid, result.getDouble("coins"));
				}
				else
				{
					this.getCookieClicker().getMySQL().update("INSERT INTO CookiesPerClick (uuid, coins) values ('" + uuid.toString() + "', '1')");
				}
			} catch (SQLException ignored)
			{
				ignored.printStackTrace();
			} finally
			{
				getCookieClicker().getCookiesPerClick().putIfAbsent(uuid, 1D);
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
			String subtitle;
			if (getCookieClicker().getCookies().get(uuid) != null)
			{
				getCookieClicker().getCookies().put(uuid, getCookieClicker().getCookies().get(uuid) + getCookieClicker().getCookiesPerClick().get(uuid));
				format(player, result -> PackageUtils.sendTitle(player, "" + PRIMARY + EXTRA + "CookieClicker", TEXT + "" + result + IMPORTANT + " Cookies", 0, 20, 10));
			}
		}
	}
	
	void format(@NonNull HumanEntity humanEntity, @NonNull Callback<String> callback)
	{
		String format = new DecimalFormat("0,000,000,000.00").format(getCookieClicker().getCookies().get(humanEntity.getUniqueId()));
		for (char c : format.toCharArray())
		{
			if (c == '0' || c == '.')
			{
				format = format.substring(1);
			}
			else
			{
				if (c == ',')
				{
					format = "0" + format;
				}
				callback.call(format);
				return;
			}
		}
	}
}
