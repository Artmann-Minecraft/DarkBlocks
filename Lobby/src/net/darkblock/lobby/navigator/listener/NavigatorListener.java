package net.darkblock.lobby.navigator.listener;

import com.segdogames.segdocloudplugin.api.CloudAPI;
import lombok.Getter;
import net.darkblock.lobby.navigator.utils.NavigatorItems;
import net.darkblock.lobby.navigator.utils.NavigatorThread;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.config.Configuration;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import net.darkblocks.dark.spigot.events.cashed.CashedEventsManager;
import net.darkblocks.dark.spigot.events.cashed.CashedInventoryClickEvent;
import net.darkblocks.dark.spigot.events.cashed.CashedPlayerInteractEvent;
import net.darkblocks.dark.spigot.utils.InventoryUtils;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static net.darkblocks.dark.universal.messages.Colors.SECONDARY;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 08.02.2018  07:16.
 */
@Getter
public class NavigatorListener implements CashedPlayerInteractEvent, CashedInventoryClickEvent
{
	private final Map<String, Boolean> navigatorAnimation;
	private final Map<String, Location> warps;
	private final ItemStack itemStack;
	private final ItemStack lobby;
	private final Inventory inventory;
	private final MySQL mySQL;
	
	public NavigatorListener(JavaPlugin javaPlugin, MySQL mySQL, Map<String, Boolean> navigatorAnimation, CashedEventsManager cashedEventsManager)
	{
		init(javaPlugin, cashedEventsManager);
		this.navigatorAnimation = navigatorAnimation;
		this.warps = new HashMap<>();
		this.itemStack = new ItemBuilder(Material.COMPASS).setName(SECONDARY + "Navigator").build();
		this.lobby = new ItemBuilder(Material.INK_SACK, 1, (short) 10).setName(SECONDARY + CloudAPI.get().getNameAPI().getServerName()).build();
		this.inventory = Bukkit.createInventory(null, 45, SECONDARY + "Navigator");
		InventoryUtils.setDesign(this.inventory, new ArrayList<>());
		this.inventory.setItem(4, NavigatorItems.CORES.getItemStack());
		this.inventory.setItem(19, NavigatorItems.FFA.getItemStack());
		this.inventory.setItem(25, NavigatorItems.GUNGAME.getItemStack());
		this.inventory.setItem(22, NavigatorItems.SPAWN.getItemStack());
		this.inventory.setItem(40, NavigatorItems.TEAMPVP.getItemStack());
		this.inventory.setItem(38, NavigatorItems.COOKIECLICKER.getItemStack());
		this.inventory.setItem(42, NavigatorItems.BELOHNUNG.getItemStack());
		this.mySQL = mySQL;
		Configuration configuration = Configuration.loadConfiguration(new File(javaPlugin.getDataFolder(), "Data.yml"));
		for (String name : configuration.getConfigurationSection("warps").getKeys(false))
		{
			getWarps().put(name.toLowerCase(), new Location(Bukkit.getWorld(configuration.getString("warps." + name + ".world")), configuration.getDouble("warps." + name + ".x"), configuration.getDouble("warps." + name + ".y"), configuration.getDouble("warps." + name + ".z"), (float) configuration.getDouble("warps." + name + ".yaw"), (float) configuration.getDouble("warps." + name + ".pitch")));
		}
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		player.getInventory().setItem(0, getItemStack());
		getMySQL().query("SELECT * FROM NavAnimation WHERE `uuid` = '" + player.getUniqueId() + "'", result -> {
			try
			{
				if (result.next())
				{
					getNavigatorAnimation().put(player.getName(), result.getBoolean("on"));
				}
				else
				{
					getMySQL().update("INSERT INTO NavAnimation(`uuid`, `name`, `ip`, `on`) VALUES ('" + player.getUniqueId() + "','" + player.getName() + "','" + player.getAddress().getHostString() + "','" + 1 + "')");
					getNavigatorAnimation().put(player.getName(), true);
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
				getNavigatorAnimation().put(player.getName(), true);
			}
		});
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
	{
		getMySQL().update("UPDATE NavAnimation SET `on` = '" + (getNavigatorAnimation().get(event.getPlayer().getName()) ? 1 : 0) + "' WHERE `uuid` = '" + event.getPlayer().getUniqueId() + "'");
	}
	
	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event)
	{
		if (event.getInventory().getName().equalsIgnoreCase(this.inventory.getName()))
		{
			for (int i = 9; i < event.getPlayer().getInventory().getSize(); i++)
			{
				event.getPlayer().getInventory().setItem(i, new ItemStack(Material.AIR));
			}
//			((Player) event.getPlayer()).updateInventory();
		}
	}
	
	@Override
	public void onCashedPlayerInteractEvent(PlayerInteractEvent event)
	{
		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null && getItemStack().getType() == event.getItem().getType())
		{
			event.setCancelled(true);
			Player player = event.getPlayer();
			PlayerInventory inventory = player.getInventory();
			InventoryUtils.setDesign(inventory, new ArrayList<>());
			inventory.setItem(22, this.lobby);
			if (getNavigatorAnimation().get(player.getName()))
			{
				new NavigatorThread(player).start();
			}
			else
			{
				player.playSound(player.getLocation(), Sound.BURP, 1, 1);
				player.openInventory(this.inventory);
			}
			player.updateInventory();
		}
	}
	
	@Override
	public void onCashedInventoryClickEvent(InventoryClickEvent event)
	{
		ItemStack currentItem = event.getCurrentItem();
		if (currentItem != null && currentItem.getItemMeta() != null)
		{
			Player player = (Player) event.getWhoClicked();
			if (event.getClickedInventory() == player.getOpenInventory().getTopInventory())
			{
				String name = ChatColor.stripColor(currentItem.getItemMeta().getDisplayName());
				if (name != null && !name.replaceAll(" ", "").equalsIgnoreCase(""))
				{
					event.setCancelled(true);
					if (getWarps().get(name.toLowerCase()) != null)
					{
						player.teleport(getWarps().get(name.toLowerCase()));
						player.closeInventory();
					}
				}
			}
			else if (event.getClickedInventory() == player.getOpenInventory().getBottomInventory())
			{
				if (currentItem.getType() == Material.INK_SACK)
				{
					player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du bist bereits auf dieser Logger");
				}
			}
		}
	}
}
