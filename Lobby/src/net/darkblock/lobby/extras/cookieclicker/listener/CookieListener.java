package net.darkblock.lobby.extras.cookieclicker.listener;

import lombok.Getter;
import net.darkblock.lobby.extras.cookieclicker.CookieClicker;
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
	
	public CookieListener(JavaPlugin javaPlugin, CookieClicker cookieClicker)
	{
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
		this.cookieClicker = cookieClicker;
		this.javaPlugin = javaPlugin;
		new CookieShopListener(javaPlugin);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		final UUID uuid = event.getPlayer().getUniqueId();
		this.getCookieClicker().getMySQL().query("SELECT * FROM Cookies WHERE uuid = '" + uuid.toString() + "'", result -> {
			System.out.println(10);
			try
			{
				System.out.println(11);
				if (result.next())
				{
					System.out.println(12);
					getCookieClicker().getCookies().put(uuid, result.getDouble(1));
				}
			} catch (SQLException ignored)
			{
				System.out.println(13);
			} finally
			{
				System.out.println(14);
				if (getCookieClicker().getCookies().get(uuid) == null)
				{
					System.out.println(15);
					getCookieClicker().getCookies().put(uuid, 0D);
					this.getCookieClicker().getMySQL().update("INSERT INTO Cookies (uuid, coins) values ('" + uuid.toString() + "', '0')");
					System.out.println(16);
				}
			}
			System.out.println(17);
		});
		this.getCookieClicker().getMySQL().query("SELECT * FROM CookiesPerClick WHERE uuid = '" + uuid.toString() + "'", result -> {
			try
			{
				if (result.next())
				{
					getCookieClicker().getCookiesPerClick().put(uuid, result.getDouble(1));
				}
			} catch (SQLException ignored)
			{
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
	public void onInteract(PlayerInteractAtEntityEvent event)
	{
		System.out.println(1);
		if (event.getRightClicked() != null && event.getRightClicked().getCustomName() != null && org.bukkit.ChatColor.stripColor(event.getRightClicked().getCustomName()).equalsIgnoreCase("CookieClicker"))
		{
			System.out.println(2);
			event.setCancelled(true);
			addCoin(event.getPlayer(), event.getRightClicked().getLocation().add(0, 2.5, 0));
		}
	}
	
	private void addCoin(Player player, Location location)
	{
		System.out.println(3);
		UUID uuid = player.getUniqueId();
		if (!getCookieClicker().getBlockedClicks().contains(uuid))
		{
			System.out.println(4);
			getCookieClicker().getBlockedClicks().add(uuid);
			Bukkit.getScheduler().runTaskLater(getJavaPlugin(), () -> getCookieClicker().getBlockedClicks().remove(uuid), 2);
			getCookieClicker().getCookies().put(uuid, getCookieClicker().getCookies().get(uuid) + getCookieClicker().getCookiesPerClick().get(uuid));
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					Item item = location.getWorld().dropItemNaturally(location, new ItemStack(Material.COOKIE));
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException ex)
					{
						ex.printStackTrace();
					} finally
					{
						item.remove();
					}
					System.out.println(4);
				}
			}.runTask(getJavaPlugin());
			String cookies = getCookieClicker().getCookies().get(uuid) == null ? "§4FEHLER!" : String.valueOf(Math.round(100.0 * (getCookieClicker().getCookies().get(uuid))));
			PackageUtils.sendTitle(player, "" + PRIMARY + EXTRA + "CookieClicker", TEXT + "" + cookies.substring(0, cookies.length() - 2) + "." + cookies.substring(cookies.length() - 2) + IMPORTANT + " Cookies", 0, 20, 10);
			System.out.println(5);
		}
	}
}
