package net.darkblock.lobby.extras.cookieclicker.listener;

import lombok.Getter;
import net.darkblock.lobby.extras.cookieclicker.CookieClicker;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.utils.InventoryUtils;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 13.02.2018  14:31.
 */
@Getter
public class CookieShopListener implements Listener
{
	private final JavaPlugin javaPlugin;
	private final CookieClicker cookieClicker;
	private final CoinsAPI coinsAPI;
	private final Inventory shop;
	private final Inventory updates;
	private final Inventory coins;
	
	CookieShopListener(JavaPlugin javaPlugin, CookieClicker cookieClicker, CoinsAPI coinsAPI)
	{
		this.javaPlugin = javaPlugin;
		this.cookieClicker = cookieClicker;
		this.coinsAPI = coinsAPI;
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
		this.shop = Bukkit.createInventory(null, InventoryType.HOPPER, SECONDARY + "CookieShop");
		this.updates = Bukkit.createInventory(null, InventoryType.CHEST, SECONDARY + "CookieShop | Updates");
		this.coins = Bukkit.createInventory(null, InventoryType.CHEST, SECONDARY + "CookieShop | Coins kaufen");
		InventoryUtils.setDesign(this.shop, new ArrayList<>());
		InventoryUtils.setDesign(this.updates, new ArrayList<>());
		InventoryUtils.setDesign(this.coins, new ArrayList<>());
		this.shop.setItem(1, new ItemBuilder(Material.COOKIE).setName(SECONDARY + "Cookie Updates").build());
		this.shop.setItem(3, new ItemBuilder(Material.GOLD_INGOT).setName(SECONDARY + "Coins kaufen").build());
		this.updates.setItem(10, new ItemBuilder(Material.COOKIE).setName(SECONDARY + "Kaufe ein hundertstel Cookie pro Klick mehr").setLore(Arrays.asList(TEXT + "für " + PRIMARY + "1.000 " + IMPORTANT + "Cookies", " ", TEXT + "Anzahl" + IMPORTANT + ": " + PRIMARY + "0.01")).build());
		this.updates.setItem(11, new ItemBuilder(Material.COOKIE).setName(SECONDARY + "Kaufe ein zehntel Cookie pro Klick mehr").setLore(Arrays.asList(TEXT + "für " + PRIMARY + "10.000 Cookies", " ", TEXT + "Anzahl" + IMPORTANT + ": " + PRIMARY + "0.1")).build());
		this.updates.setItem(13, new ItemBuilder(Material.COOKIE).setName(SECONDARY + "Kaufe ein Cookie pro Klick mehr").setLore(Arrays.asList(TEXT + "für " + PRIMARY + "100.000 " + IMPORTANT + "Cookies", " ", TEXT + "Anzahl" + IMPORTANT + ": " + PRIMARY + "1")).build());
		this.updates.setItem(15, new ItemBuilder(Material.COOKIE).setName(SECONDARY + "Kaufe 10 Cookies pro Klick mehr").setLore(Arrays.asList(TEXT + "für " + PRIMARY + "1.000.000 " + IMPORTANT + "Cookies", " ", TEXT + "Anzahl" + IMPORTANT + ": " + PRIMARY + "10")).build());
		this.updates.setItem(16, new ItemBuilder(Material.COOKIE).setName(SECONDARY + "Kaufe 100 Cookies pro Klick mehr").setLore(Arrays.asList(TEXT + "für " + PRIMARY + "10.000.000 " + IMPORTANT + "Cookies", " ", TEXT + "Anzahl" + IMPORTANT + ": " + PRIMARY + "100")).build());
		this.coins.setItem(10, new ItemBuilder(Material.GOLD_INGOT).setName(SECONDARY + "Kaufe ein Coin").setLore(Arrays.asList(TEXT + "für " + PRIMARY + "1.000 " + IMPORTANT + "Cookies", " ", TEXT + "Anzahl" + IMPORTANT + ": " + PRIMARY + "1")).build());
		this.coins.setItem(11, new ItemBuilder(Material.GOLD_INGOT).setName(SECONDARY + "Kaufe 10 Coins").setLore(Arrays.asList(TEXT + "für " + PRIMARY + "10.000 " + IMPORTANT + "Cookies", " ", TEXT + "Anzahl" + IMPORTANT + ": " + PRIMARY + "10")).build());
		this.coins.setItem(12, new ItemBuilder(Material.GOLD_INGOT).setName(SECONDARY + "Kaufe 100 Coins").setLore(Arrays.asList(TEXT + "für " + PRIMARY + "100.000 " + IMPORTANT + "Cookies", " ", TEXT + "Anzahl" + IMPORTANT + ": " + PRIMARY + "100")).build());
		this.coins.setItem(14, new ItemBuilder(Material.GOLD_INGOT).setName(SECONDARY + "Kaufe 1000 Coins").setLore(Arrays.asList(TEXT + "für " + PRIMARY + "1.000.000 " + IMPORTANT + "Cookies", " ", TEXT + "Anzahl" + IMPORTANT + ": " + PRIMARY + "1000")).build());
		this.coins.setItem(15, new ItemBuilder(Material.GOLD_INGOT).setName(SECONDARY + "Kaufe 10000 Coins").setLore(Arrays.asList(TEXT + "für " + PRIMARY + "10.000.000 " + IMPORTANT + "Cookies", " ", TEXT + "Anzahl" + IMPORTANT + ": " + PRIMARY + "10000")).build());
		this.coins.setItem(16, new ItemBuilder(Material.GOLD_INGOT).setName(SECONDARY + "Kaufe 100000 Coins").setLore(Arrays.asList(TEXT + "für " + PRIMARY + "100.000.000 " + IMPORTANT + "Cookies", " ", TEXT + "Anzahl" + IMPORTANT + ": " + PRIMARY + "100000")).build());
	}
	
	@EventHandler
	public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event)
	{
		if (event.getRightClicked() != null && event.getRightClicked().getCustomName() != null && org.bukkit.ChatColor.stripColor(event.getRightClicked().getCustomName()).equalsIgnoreCase("CookieShop"))
		{
			event.setCancelled(true);
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					Player player = event.getPlayer();
					player.closeInventory();
					player.openInventory(CookieShopListener.this.shop);
					player.updateInventory();
				}
			}.runTaskLater(getJavaPlugin(), 2);
		}
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event)
	{
		Inventory inventory = event.getInventory();
		System.out.println(1);
		System.out.println(inventory);
		System.out.println(getShop());
		System.out.println(getUpdates());
		System.out.println(getCoins());
		if (inventory.equals(getShop()) || inventory.equals(getUpdates()) || inventory.equals(getCoins()))
		{
			System.out.println(2);
			event.setCancelled(true);
			ItemStack currentItem = event.getCurrentItem();
			if (currentItem != null && currentItem.getType() != Material.STAINED_GLASS_PANE && currentItem.getItemMeta() != null)
			{
				System.out.println(currentItem.getItemMeta().getLore());
				System.out.println(3);
				HumanEntity whoClicked = event.getWhoClicked();
				if (inventory == getShop())
				{
					System.out.println(4);
					if (currentItem.getType() == Material.COOKIE)
					{
						System.out.println(5);
						whoClicked.openInventory(getUpdates());
					}
					else if (currentItem.getType() == Material.GOLD_INGOT)
					{
						System.out.println(6);
						whoClicked.openInventory(getCoins());
					}
				}
				else if (currentItem.getItemMeta().getLore() != null)
				{
					System.out.println(7);
					double value = Double.valueOf(org.bukkit.ChatColor.stripColor(currentItem.getItemMeta().getLore().get(2)).substring(8));
					if (inventory.equals(getUpdates()))
					{
						System.out.println(8);
						if (getCookieClicker().getCookies().get(whoClicked.getUniqueId()) >= value * 1000)
						{
							System.out.println(9);
							getCookieClicker().getCookies().put(whoClicked.getUniqueId(), getCookieClicker().getCookies().get(whoClicked.getUniqueId()) - value);
							getCookieClicker().getCookiesPerClick().put(whoClicked.getUniqueId(), getCookieClicker().getCookiesPerClick().get(whoClicked.getUniqueId()) + value);
							whoClicked.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + "Du hast " + PRIMARY + value + IMPORTANT + " Coins" + currentItem.getItemMeta().getLore().get(0) + TEXT + " gekauft");
						}
						else
						{
							System.out.println(10);
							whoClicked.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + "Du hast nicht " + PRIMARY + "genug " + IMPORTANT + "Coins");
						}
					}
					else if (inventory.equals(getCoins()))
					{
						System.out.println(11);
						if (getCookieClicker().getCookies().get(whoClicked.getUniqueId()) >= value * 1000)
						{
							System.out.println(12);
							getCookieClicker().getCookies().put(whoClicked.getUniqueId(), getCookieClicker().getCookies().get(whoClicked.getUniqueId()) - value);
							whoClicked.sendMessage(getCoinsAPI().addCoins(whoClicked.getUniqueId(), String.valueOf((int) value), () -> whoClicked.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + "Du hast " + PRIMARY + value + IMPORTANT + " Cookie" + (value > 1 ? "s" : "") + " pro Klick mehr" + currentItem.getItemMeta().getLore().get(0) + TEXT + " gekauft")));
						}
						else
						{
							System.out.println(13);
							whoClicked.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + "Du hast nicht " + PRIMARY + "genug " + IMPORTANT + "Coins");
						}
					}
				}
			}
		}
	}
}

