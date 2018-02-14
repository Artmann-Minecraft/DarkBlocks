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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

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
	public void onInteract(PlayerInteractAtEntityEvent event)
	{
		if (event.getRightClicked() != null && event.getRightClicked().getCustomName() != null && event.getRightClicked().getCustomName().equalsIgnoreCase(SECONDARY + "CookieClicker"))
		{
			event.setCancelled(true);
			addCoin(event.getPlayer(), event.getRightClicked().getLocation().add(0, 2.5, 0));
		}
	}
	
	private void addCoin(Player player, Location location)
	{
		UUID uuid = player.getUniqueId();
		if (!getCookieClicker().getBlockedClicks().contains(uuid))
		{
			getCookieClicker().getBlockedClicks().add(uuid);
			Bukkit.getScheduler().runTaskLater(getJavaPlugin(), () -> getCookieClicker().getBlockedClicks().remove(uuid), 2);
			getCookieClicker().getCookies().put(uuid, getCookieClicker().getCookies().get(uuid) + getCookieClicker().getCookiesPerClick().get(uuid));
			new Thread(() -> {
				try
				{
					Item item = location.getWorld().dropItemNaturally(location, new ItemStack(Material.COOKIE));
					Thread.sleep(1000);
					item.remove();
				} catch (InterruptedException ex)
				{
					ex.printStackTrace();
				}
			}).start();
			String cookies = String.valueOf(Math.round(100.0 * getCookieClicker().getCookies().get(uuid)));
			PackageUtils.sendTitle(player, "" + PRIMARY + EXTRA + "CookieClicker", TEXT + "" + cookies.substring(0, cookies.length() - 2) + "." + cookies.substring(cookies.length() - 2) + IMPORTANT + " Cookies", 0, 20, 10);
		}
	}
}
