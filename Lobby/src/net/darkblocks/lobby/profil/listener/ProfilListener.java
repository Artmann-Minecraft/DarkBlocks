/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.lobby.profil.listener;

import lombok.Getter;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import net.darkblocks.dark.spigot.events.cashed.CashedEventsManager;
import net.darkblocks.dark.spigot.events.cashed.CashedInventoryClickEvent;
import net.darkblocks.dark.spigot.events.cashed.CashedPlayerInteractEvent;
import net.darkblocks.dark.spigot.utils.InventoryUtils;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 08.02.2018  10:12.
 */
@Getter
public class ProfilListener implements CashedPlayerInteractEvent, CashedInventoryClickEvent
{
	private final Map<String, Boolean> navigatorAnimation;
	private final Map<String, ItemBuilder> skulls;
	private final Inventory profil;
	private final Inventory settings;
	
	public ProfilListener(JavaPlugin javaPlugin, CashedEventsManager cashedEventsManager, Map<String, Boolean> navigatorAnimation)
	{
		init(javaPlugin, cashedEventsManager);
		this.navigatorAnimation = navigatorAnimation;
		this.skulls = new HashMap<>();
		this.profil = Bukkit.createInventory(null, InventoryType.HOPPER, SECONDARY + "Profil");
		InventoryUtils.setDesign(this.profil, new ArrayList<>());
		this.profil.setItem(1, new ItemBuilder(Material.REDSTONE).setName(SECONDARY + "Settings").build());
		this.settings = Bukkit.createInventory(null, InventoryType.HOPPER, SECONDARY + "Settings");
		InventoryUtils.setDesign(this.settings, new ArrayList<>());
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		String name = event.getPlayer().getName();
		this.skulls.put(name, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setOwner(name));
		event.getPlayer().getInventory().setItem(8, this.skulls.get(name).setName(SECONDARY + "Profil").build());
	}
	
	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
	{
		this.skulls.remove(event.getPlayer().getName());
	}
	
	@Override
	public void onCashedPlayerInteractEvent(PlayerInteractEvent event)
	{
		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null && event.getItem().getType() == Material.SKULL_ITEM)
		{
			this.profil.setItem(3, getSkulls().get(event.getPlayer().getName()).setName(SECONDARY + "Freunde").build());
			event.getPlayer().openInventory(this.profil);
		}
	}
	
	@Override
	public void onCashedInventoryClickEvent(InventoryClickEvent event)
	{
		ItemStack currentItem = event.getCurrentItem();
		if (currentItem != null && currentItem.getItemMeta() != null)
		{
			Player player = (Player) event.getWhoClicked();
			if (getProfil() == event.getInventory())
			{
				switch (currentItem.getType())
				{
					case REDSTONE:
						ItemBuilder itemBuilder = new ItemBuilder(Material.COMPASS).setName(SECONDARY + "Navigator | Animation").hideItemFlags();
						if (getNavigatorAnimation().get(player.getName()))
						{
							this.settings.setItem(2, itemBuilder.addUnsafeEnchantment(Enchantment.LUCK, 10).build());
						}
						else
						{
							this.settings.setItem(2, itemBuilder.build());
						}
						player.openInventory(this.settings);
						break;
					case SKULL_ITEM:
						player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Die Freunde sind in Arbeit");
						player.closeInventory();
						break;
				}
			}
			else if (getSettings() == event.getInventory() && currentItem.getType() == Material.COMPASS)
			{
				if (getNavigatorAnimation().get(player.getName()))
				{
					getNavigatorAnimation().put(player.getName(), false);
					player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du hast die " + IMPORTANT + "Navigator-Animation " + TEXT + "ausgeschaltet");
				}
				else
				{
					getNavigatorAnimation().put(player.getName(), true);
					player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du hast die " + IMPORTANT + "Navigator-Animation " + TEXT + "angeschaltet");
				}
				player.closeInventory();
			}
		}
	}
}
