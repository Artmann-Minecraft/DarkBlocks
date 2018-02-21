/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.lobby.verstecker.listener;

import lombok.Getter;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.events.cashed.CashedEventsManager;
import net.darkblocks.dark.spigot.events.cashed.CashedInventoryClickEvent;
import net.darkblocks.dark.spigot.events.cashed.CashedPlayerInteractEvent;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

import static net.darkblocks.dark.universal.messages.Colors.SECONDARY;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 08.02.2018  10:12.
 */
@Getter
public class VersteckerListener implements CashedPlayerInteractEvent, CashedInventoryClickEvent
{
	private final Inventory inventory;
	private final ItemStack itemStack;
	
	public VersteckerListener(JavaPlugin javaPlugin, CashedEventsManager cashedEventsManager)
	{
		init(javaPlugin, cashedEventsManager);
		this.inventory = Bukkit.createInventory(null, InventoryType.BREWING, SECONDARY + "Spieler verstecken");
		this.inventory.setItem(0, new ItemBuilder(Material.INK_SACK, 1, (short) 10).setName(SECONDARY + "Alle Spieler anzeigen").build());
		this.inventory.setItem(1, new ItemBuilder(Material.INK_SACK, 1, (short) 11).setName(SECONDARY + "Nur Teammitglieder anzeigen").build());
		this.inventory.setItem(2, new ItemBuilder(Material.INK_SACK, 1, (short) 1).setName(SECONDARY + "Keine Spieler anzeigen").build());
		this.inventory.setItem(3, new ItemBuilder(Material.PAPER).setName(SECONDARY + "Spieler verstecken").build());
		this.itemStack = new ItemBuilder(Material.BLAZE_ROD).setName(SECONDARY + "Spieler verstecken").build();
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		event.getPlayer().getInventory().setItem(1, getItemStack());
	}
	
	@Override
	public void onCashedPlayerInteractEvent(PlayerInteractEvent event)
	{
		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null && getItemStack().getType() == event.getItem().getType())
		{
			event.getPlayer().openInventory(this.inventory);
		}
	}
	
	@Override
	public void onCashedInventoryClickEvent(InventoryClickEvent event)
	{
		if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null && getInventory() == event.getInventory())
		{
			Player player = (Player) event.getWhoClicked();
			switch (org.bukkit.ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()))
			{
				case "Alle Spieler anzeigen":
					for (Player players : Bukkit.getOnlinePlayers())
					{
						player.showPlayer(players);
					}
					player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Dir werden nun alle Spieler angezeigt");
					player.closeInventory();
					break;
				case "Nur Teammitglieder anzeigen":
					for (Player players : Bukkit.getOnlinePlayers())
					{
						if (players.hasPermission(CommandUtils.getPermission(getClass())))
						{
							player.showPlayer(players);
						}
						else
						{
							player.hidePlayer(players);
						}
					}
					player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Dir werden nur die Teammitglieder angezeigt");
					player.closeInventory();
					break;
				case "Keine Spieler anzeigen":
					for (Player players : Bukkit.getOnlinePlayers())
					{
						player.hidePlayer(players);
					}
					player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Dir werden nun keine Spieler angezeigt");
					player.closeInventory();
					break;
			}
		}
	}
}
