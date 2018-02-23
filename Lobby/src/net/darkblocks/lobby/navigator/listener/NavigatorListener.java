/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.lobby.navigator.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.segdogames.segdocloudplugin.api.CloudAPI;
import com.segdogames.segdocloudplugin.spigot.Bootstrap;
import com.segdogames.segdocloudplugin.spigot.signs.SignServer;
import lombok.Getter;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.config.Configuration;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import net.darkblocks.dark.spigot.events.cashed.CashedEventsManager;
import net.darkblocks.dark.spigot.events.cashed.CashedInventoryClickEvent;
import net.darkblocks.dark.spigot.events.cashed.CashedPlayerInteractEvent;
import net.darkblocks.dark.spigot.utils.InventoryUtils;
import net.darkblocks.dark.spigot.utils.PackageUtils;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.lobby.navigator.utils.NavigatorItems;
import net.darkblocks.lobby.navigator.utils.NavigatorThread;
import net.minecraft.server.v1_8_R3.EnumParticle;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 08.02.2018  07:16.
 */
@Getter
public class NavigatorListener implements CashedPlayerInteractEvent, CashedInventoryClickEvent
{
	private final Map<String, Boolean> navigatorAnimation;
	private final Map<String, Location> warps;
	private final ItemStack itemStack;
	private final ItemBuilder lobby;
	private final Inventory inventory;
	private final MySQL mySQL;
	private final JavaPlugin javaPlugin;
	
	public NavigatorListener(JavaPlugin javaPlugin, MySQL mySQL, Map<String, Boolean> navigatorAnimation, CashedEventsManager cashedEventsManager)
	{
		init(javaPlugin, cashedEventsManager);
		this.javaPlugin = javaPlugin;
		this.navigatorAnimation = navigatorAnimation;
		this.warps = new HashMap<>();
		this.itemStack = new ItemBuilder(Material.COMPASS).setName(SECONDARY + "Navigator").build();
		this.lobby = new ItemBuilder(Material.INK_SACK).setName(SECONDARY + CloudAPI.get().getNameAPI().getServerName());
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
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(javaPlugin, "BungeeCord");
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
		if (event.getInventory() != null && event.getInventory().getName().equalsIgnoreCase(this.inventory.getName()))
		{
			for (int i = 9; i < event.getPlayer().getInventory().getSize(); i++)
			{
				event.getPlayer().getInventory().setItem(i, new ItemStack(Material.AIR));
			}
		}
	}
	
	@Override
	public void onCashedPlayerInteractEvent(PlayerInteractEvent event)
	{
		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null && getItemStack().getType() == event.getItem().getType())
		{
			event.setCancelled(true);
			Player player = event.getPlayer();
			InventoryUtils.setDesign(player.getInventory(), new ArrayList<>());
			setLobbys(player);
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
		if (event.getInventory() != null && event.getInventory().getName() != null && event.getInventory().getName().equalsIgnoreCase(SECONDARY + "Navigator") && currentItem != null && currentItem.getItemMeta() != null)
		{
			Player player = (Player) event.getWhoClicked();
			if (event.getClickedInventory() == player.getOpenInventory().getTopInventory())
			{
				String name = ChatColor.stripColor(currentItem.getItemMeta().getDisplayName());
				if (name != null && !name.replaceAll(" ", "").equalsIgnoreCase(""))
				{
					event.setCancelled(true);
					Location location = getWarps().get(name.toLowerCase());
					if (location != null)
					{
						player.teleport(location);
						player.closeInventory();
						PackageUtils.sendTitle(player, IMPORTANT + ">>" + PRIMARY + EXTRA + " Lobby " + IMPORTANT + "<<", "" + SECONDARY + EXTRA + name, 10, 40, 10);
						PackageUtils.sendPlayerParticle(player, EnumParticle.ENCHANTMENT_TABLE, player.getLocation().add(0, 1, 0), 3F, 25);
					}
				}
			}
			else if (event.getClickedInventory() == player.getOpenInventory().getBottomInventory() && currentItem.getType() == Material.INK_SACK)
			{
				if (currentItem.getDurability() == 10)
				{
					player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du bist bereits auf dieser Lobby");
				}
				else if (currentItem.getItemMeta().getDisplayName() != null)
				{
					setLobbys(player);
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("Connect");
					out.writeUTF(ChatColor.stripColor(currentItem.getItemMeta().getDisplayName()));
					player.sendPluginMessage(getJavaPlugin(), "BungeeCord", out.toByteArray());
				}
			}
		}
	}
	
	private void setLobbys(Player player)
	{
		List<ItemStack> lobbies = new ArrayList<>();
		for (SignServer server : Bootstrap.getINSTANCE().getSignSystem().getServers().values())
		{
			if (server.getGroup().equalsIgnoreCase("Lobby"))
			{
				lobbies.add(this.lobby.clone().setAmount((server.getName().equalsIgnoreCase(CloudAPI.get().getNameAPI().getServerName()) ? Bukkit.getOnlinePlayers().size() : server.getPlayers())).setLore(TEXT + "Spieler" + IMPORTANT + ": " + TEXT + server.getPlayers() + IMPORTANT + "/" + TEXT + server.getMaxPlayers()).setDurability((short) (server.getName().equalsIgnoreCase(CloudAPI.get().getNameAPI().getServerName()) ? 10 : 8)).setName(SECONDARY + server.getName()).build());
			}
		}
		InventoryUtils.sortChestInventory(player.getInventory(), lobbies, 18);
	}
}
