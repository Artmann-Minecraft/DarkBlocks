/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.gungame.shop.manager;

import lombok.Getter;
import net.darkblocks.dark.spigot.builder.InventoryBuilder;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import net.darkblocks.dark.spigot.listener.Listener;
import net.darkblocks.dark.spigot.utils.InventoryUtils;
import net.darkblocks.dark.universal.messages.Colors;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.gungame.kits.utils.Kits;
import net.darkblocks.gungame.listener.InGameListener;
import net.darkblocks.gungame.shop.listener.ShopItemListener;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 19.02.2018  02:32.
 */
@Getter
public class ShopManager extends Listener
{
	private final Inventory inventory;
	private final InGameListener inGameListener;
	private final List<ShopItemListener> items;
	
	public ShopManager(JavaPlugin javaPlugin, InGameListener inGameListener)
	{
		super(javaPlugin);
		this.inGameListener = inGameListener;
		this.items = Arrays.asList(new ShopItemListener(javaPlugin, new ItemBuilder(Material.INK_SACK, (short) 1).setName(SECONDARY + "Magic Heal").setUnbreakable().setLore(Arrays.asList(TEXT + "Kaufe ihn dir für " + PRIMARY + "50 " + IMPORTANT + "Coins", TEXT + "Er regeneriert dich sofort")).hideItemFlags().build(), 30, 50)
		{
			@Getter
			private Set<String> healer;
			
			@Override
			public void init()
			{
				this.healer = new HashSet<>();
			}
			
			@Override
			public void buy(Player player)
			{
				if (InventoryUtils.hasItems(player, Material.INK_SACK) >= 3)
				{
					player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Du darfst nur drei " + Colors.SECONDARY + "Magic Heal " + Colors.TEXT + "im " + Colors.IMPORTANT + "Inventar " + Colors.TEXT + "haben");
				}
				else
				{
					buy(player, getInGameListener().getCoinsAPI(), result -> {
						player.playSound(player.getLocation(), Sound.LEVEL_UP, 2, 1);
						player.getInventory().addItem(new ItemBuilder(getItemStack()).removeLore(0).hideItemFlags().build());
						player.closeInventory();
					});
				}
			}
			
			@EventHandler
			public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
			{
				if (getInGameListener().getUseHealer().contains(event.getEntity().getName()))
				{
					event.setDamage(0);
				}
			}
			
			@EventHandler
			public void onPlayerInteractEvent(PlayerInteractEvent event)
			{
				if (event.getItem() != null && event.getItem().getType() != null)
				{
					Player player = event.getPlayer();
					if (event.getItem().getType() == Material.INK_SACK)
					{
						event.setCancelled(true);
						if ((int) player.getHealth() == (int) player.getMaxHealth())
						{
							player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du hast schon volle Herzen");
						}
						else
						{
							InventoryUtils.removeItems(player, Material.INK_SACK, 1);
							getInGameListener().healPlayer(player);
						}
					}
				}
			}
		}, new ShopItemListener(javaPlugin, new ItemBuilder(Material.DIAMOND).setName(SECONDARY + "Level Up").setUnbreakable().setLore(Arrays.asList(TEXT + "Kaufe ihn dir für " + PRIMARY + "100 " + IMPORTANT + "Coins", TEXT + "Erhöht dein Level um 5")).hideItemFlags().build(), 60, 100)
		{
			@Getter
			private Set<String> updater;
			
			@Override
			public void init()
			{
				this.updater = new HashSet<>();
			}
			
			@Override
			public void buy(Player player)
			{
				if (getInGameListener().getKitManager().getPlayer().get(player.getName()) == Kits.values().length)
				{
					player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Du hast schon das maximahle Level (" + IMPORTANT + Kits.values().length + TEXT + ") erreicht");
				}
				else
				{
					buy(player, getInGameListener().getCoinsAPI(), result -> {
						player.playSound(player.getLocation(), Sound.LEVEL_UP, 2, 1);
						new Thread(() ->
						{
							for (int i = 0; i < 5; i++)
							{
								getInGameListener().getKitManager().update(player);
								try
								{
									Thread.sleep(50);
								} catch (InterruptedException ex)
								{
									ex.printStackTrace();
								}
							}
						}).start();
						player.closeInventory();
					});
				}
			}
		}, new ShopItemListener(javaPlugin, new ItemBuilder(Material.FIREBALL).setName(SECONDARY + "Instant Killer").setUnbreakable().setLore(Arrays.asList(TEXT + "Kaufe ihn dir für " + PRIMARY + "500 " + IMPORTANT + "Coins", TEXT + "Töte einen Spieler sofort")).addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1000).hideItemFlags().hideItemFlags().build(), 300, 500)
		{
			@Getter
			private Set<String> killer;
			
			@Override
			public void init()
			{
				this.killer = new HashSet<>();
			}
			
			@Override
			public void buy(Player player)
			{
				buy(player, getInGameListener().getCoinsAPI(), result -> {
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 2, 1);
					player.getInventory().addItem(new ItemBuilder(getItemStack()).removeLore(0).hideItemFlags().build());
					player.closeInventory();
				});
			}
			
			@EventHandler
			public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
			{
				if (event.getDamager() instanceof Player)
				{
					Player damager = (Player) event.getDamager();
					if (damager.getItemInHand().getType() == Material.FIREBALL && !event.isCancelled())
					{
						InventoryUtils.removeItems(damager, Material.FIREBALL, 1);
					}
				}
			}
		}, new ShopItemListener(javaPlugin, new ItemBuilder(Material.PAPER).setName(SECONDARY + "KeepInventory").setUnbreakable().setLore(Arrays.asList(TEXT + "Kaufe ihn dir für " + PRIMARY + "500 " + IMPORTANT + "Coins", TEXT + "Behalte nach deinem Tot deine Items")).hideItemFlags().build(), 300, 500)
		{
			@Getter
			private Set<String> keepInv;
			
			@Override
			public void init()
			{
				this.keepInv = new HashSet<>();
			}
			
			@Override
			public void buy(Player player)
			{
				if (getKeepInv().contains(player.getName()))
				{
					player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Du hast " + IMPORTANT + "KeepInventory " + TEXT + "schon aktiviert");
				}
				else
				{
					buy(player, getInGameListener().getCoinsAPI(), result -> {
						player.playSound(player.getLocation(), Sound.LEVEL_UP, 2, 1);
						getKeepInv().add(player.getName());
						player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Du hast jetzt " + IMPORTANT + "KeepInventory " + TEXT + " aktiviert");
						player.closeInventory();
					});
				}
			}
			
			@EventHandler
			public void onPlayerDisconnectEvent(PlayerDisconnectEvent event)
			{
				getKeepInv().remove(event.getPlayer().getName());
			}
			
			@EventHandler
			public void onPlayerRespawnEvent(PlayerRespawnEvent event)
			{
				Player player = event.getPlayer();
				if (!getKeepInv().contains(player.getName()))
				{
					getInGameListener().getKitManager().downgrade(player);
				}
				else
				{
					getKeepInv().remove(player.getName());
					Bukkit.getScheduler().scheduleSyncDelayedTask(getJavaPlugin(), () -> player.setLevel(getInGameListener().getKitManager().getPlayer().get(player.getName())), 3);
				}
			}
		});
		this.inventory = new InventoryBuilder(null, InventoryType.HOPPER, SECONDARY + "Shop").setDesign(new ArrayList<>()).build();
		int i = 0;
		for (ShopItemListener shopItemListener : getItems())
		{
			if (i == 2)
			{
				i++;
			}
			switch (i)
			{
				case 0:
				case 1:
				case 3:
				case 4:
					this.inventory.setItem(i, shopItemListener.getItemStack());
					break;
			}
			i++;
		}
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event)
	{
		if (event.getInventory() != null && event.getInventory() != null && event.getInventory().getName().equalsIgnoreCase(SECONDARY + "Shop") && event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta().getDisplayName() != null)
		{
			for (ShopItemListener shopItemListener : getItems())
			{
				if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(shopItemListener.getDisplayName()))
				{
					shopItemListener.buy((Player) event.getWhoClicked());
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event)
	{
		Entity rightClicked = event.getRightClicked();
		if (rightClicked != null && rightClicked.getType() != null && rightClicked instanceof ArmorStand)
		{
			event.setCancelled(true);
			if (event.getPlayer().isSneaking())
			{
				editArmorStand((ArmorStand) rightClicked, getInGameListener().getRandom());
			}
			else
			{
				editArmorStand((ArmorStand) rightClicked, getInGameListener().getRandom());
				event.getPlayer().openInventory(this.inventory);
			}
		}
	}
	
	private void editArmorStand(ArmorStand armorStand, Random random)
	{
		ItemStack itemStack = new ItemBuilder(armorStand.getChestplate()).setColor(Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256))).hideItemFlags().build();
		armorStand.setChestplate(itemStack);
		armorStand.setLeggings(itemStack);
		armorStand.setBoots(itemStack);
	}
}
