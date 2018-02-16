/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblock.lobby.extras.belohnung.listener;

import lombok.Getter;
import lombok.NonNull;
import net.darkblock.lobby.extras.belohnung.Belohnung;
import net.darkblock.lobby.extras.belohnung.utils.ChestOpeningItem;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import net.darkblocks.dark.spigot.utils.InventoryUtils;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCrafting;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.*;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 16.02.2018  13:47.
 */
@Getter
public class ChestOpeningListener implements Listener
{
	private final Map<String, Integer> chests;
	private final List<ChestOpeningItem> items;
	private final List<ChestOpeningItem> possibleItems;
	private final Belohnung belohnung;
	private final Inventory buyChestInventory;
	
	public ChestOpeningListener(JavaPlugin javaPlugin, Belohnung belohnung)
	{
		this.chests = new HashMap<>();
		this.items = new ArrayList<>();
		this.possibleItems = new ArrayList<>();
		this.belohnung = belohnung;
		this.buyChestInventory = Bukkit.createInventory(null, InventoryType.HOPPER, SECONDARY + "CaseOpening | Buy");
		InventoryUtils.setDesign(this.buyChestInventory, new ArrayList<>());
		this.buyChestInventory.setItem(0, new ItemBuilder(Material.STAINED_CLAY).setDurability((short) 5).setName(TEXT + "Kaufen").setLore(TEXT + "Eine Kiste kostet" + IMPORTANT + " 5000 Coins").build());
		this.buyChestInventory.setItem(4, new ItemBuilder(Material.STAINED_CLAY).setDurability((short) 14).setName(TEXT + "Abbrechen").build());
		getBelohnung().getMySQL().update("CREATE TABLE IF NOT EXISTS Chests(uuid VARCHAR (50), `chests` INT, PRIMARY KEY (uuid))", () -> {
			addItem(new ChestOpeningItem("1000 Coins", new ItemBuilder(Material.GOLD_NUGGET).setName(SECONDARY + "1000 Coins").build())
			{
				@Override
				public void execute(Player player)
				{
					player.sendMessage(belohnung.getCoinsAPI().addCoins(player.getUniqueId(), String.valueOf(1000), null));
				}
			}, 100);
			addItem(new ChestOpeningItem("5000 Coins", new ItemBuilder(Material.GOLD_INGOT).setName(SECONDARY + "5000 Coins").build())
			{
				@Override
				public void execute(Player player)
				{
					player.sendMessage(belohnung.getCoinsAPI().addCoins(player.getUniqueId(), String.valueOf(5000), null));
				}
			}, 40);
			addItem(new ChestOpeningItem("10000 Coins", new ItemBuilder(Material.GOLD_BLOCK).setName(SECONDARY + "10000 Coins").build())
			{
				@Override
				public void execute(Player player)
				{
					player.sendMessage(belohnung.getCoinsAPI().addCoins(player.getUniqueId(), String.valueOf(10000), null));
				}
			}, 20);
			addItem(new ChestOpeningItem("2 Kisten", new ItemBuilder(Material.CHEST).setName(SECONDARY + "2 Kisten").build())
			{
				@Override
				public void execute(Player player)
				{
					ChestOpeningListener.this.chests.put(player.getName(), ChestOpeningListener.this.chests.get(player.getName()) + 2);
				}
			}, 10);
			Bukkit.getPluginManager().registerEvents(this, javaPlugin);
		});
	}
	
	private void addItem(@NonNull ChestOpeningItem chestOpeningItem, int chance)
	{
		this.possibleItems.add(chestOpeningItem);
		for (int i = 0; i < chance; i++)
		{
			this.items.add(chestOpeningItem);
		}
	}
	
	@EventHandler
	private void onJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		getBelohnung().getMySQL().query("SELECT * FROM Chests WHERE uuid = '" + player.getUniqueId() + "'", resultSet -> {
			try
			{
				if (!resultSet.next())
				{
					getBelohnung().getMySQL().update("INSERT INTO Chests (`uuid`, `chests`) VALUES ('" + player.getUniqueId() + "','0')");
				}
				else
				{
					this.chests.put(player.getName(), resultSet.getInt("chests"));
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			} finally
			{
				this.chests.putIfAbsent(player.getName(), 0);
			}
		});
	}
	
	@EventHandler
	private void onPlayerDisconnect(PlayerDisconnectEvent event)
	{
		Player player = event.getPlayer();
		getBelohnung().getMySQL().update("UPDATE Chests SET chests='" + this.chests.get(player.getName()) + "' WHERE uuid='" + player.getUniqueId() + "'", () -> this.chests.remove(player.getName()));
	}
	
	@EventHandler
	private void onInventoryClose(InventoryCloseEvent event)
	{
		if (event.getInventory().getName() != null && event.getInventory().getName().equalsIgnoreCase(SECONDARY + "CaseOpening"))
		{
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					event.getPlayer().openInventory(finished((Player) event.getPlayer(), event.getInventory()));
				}
			}.runTaskLater(getBelohnung().getJavaPlugin(), 1);
		}
	}
	
	@EventHandler
	private void onInventoryClickEvent(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		Inventory inventory = event.getInventory();
		String title = inventory.getTitle();
		ItemStack itemStack = event.getCurrentItem();
		if (title != null && itemStack != null)
		{
			switch (ChatColor.stripColor(title).toLowerCase())
			{
				case "belohnung":
					Material type = itemStack.getType();
					if (type != null && type == Material.CHEST)
					{
						if (this.chests.get(player.getName()) != null && (event.isShiftClick() || this.chests.get(player.getName()) < 1))
						{
							this.buyChestInventory.setItem(2, new ItemBuilder(Material.CHEST).setName(SECONDARY + "CaseOpening").setLore(Collections.singletonList(TEXT + "Du hast noch " + IMPORTANT + this.chests.get(player.getName()) + TEXT + " Kisten")).build());
							player.openInventory(this.buyChestInventory);
						}
						else
						{
							Inventory caseOpeningInventory = Bukkit.createInventory(null, 27, SECONDARY + "CaseOpening");
							InventoryUtils.setDesign(caseOpeningInventory, new ArrayList<>());
							caseOpeningInventory.setItem(4, new ItemBuilder(Material.HOPPER).setName(SECONDARY + "Dein Item").build());
							Collections.shuffle(this.items);
							executeOneChestDelay(caseOpeningInventory, player);
							player.openInventory(caseOpeningInventory);
							new Thread(() -> {
								System.out.println(1);
								try
								{
									for (int i = 0; i < 100; i++)
									{
										System.out.println(11);
										if (!executeOneChestDelay(caseOpeningInventory, player))
										{
											return;
										}
										Thread.sleep((long) (25));
										player.playSound(player.getLocation(), Sound.BURP, 1, 1);
									}
									for (int i = 0; i < 50; i++)
									{
										System.out.println(12);
										if (!executeOneChestDelay(caseOpeningInventory, player))
										{
											return;
										}
										Thread.sleep((long) (50));
										player.playSound(player.getLocation(), Sound.BURP, 1, 1);
									}
									for (int i = 0; i < 25; i++)
									{
										System.out.println(13);
										if (!executeOneChestDelay(caseOpeningInventory, player))
										{
											return;
										}
										Thread.sleep((long) (100));
										player.playSound(player.getLocation(), Sound.BURP, 1, 1);
									}
								} catch (InterruptedException ex)
								{
									ex.printStackTrace();
								}
								System.out.println(2);
								player.openInventory(finished(player, caseOpeningInventory));
								System.out.println(3);
							}).start();
						}
					}
					break;
				case "caseopening | buy":
					short durability = itemStack.getDurability();
					switch (durability)
					{
						case 5:
							getBelohnung().getCoinsAPI().getCoins(player.getUniqueId(), result -> {
								if (result < 5000)
								{
									player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du hast nicht genug Coins (dir fehlen " + IMPORTANT + result + " Coins" + TEXT + ")");
								}
								else
								{
									getBelohnung().getCoinsAPI().removeCoins(player.getUniqueId(), 5000, result1 -> {
										this.chests.put(player.getName(), this.chests.get(player.getName()) + 1);
										player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Dir wurde eine Kiste hinzugefügt");
										this.buyChestInventory.setItem(2, new ItemBuilder(Material.CHEST).setName(SECONDARY + "CaseOpening").setLore(Collections.singletonList(TEXT + "Du hast noch " + IMPORTANT + this.chests.get(player.getName()) + TEXT + " Kisten")).build());
									});
								}
							});
							break;
						case 14:
							player.closeInventory();
							break;
					}
					break;
			}
		}
	}
	
	private Inventory finished(Player player, Inventory inventory)
	{
		ChestOpeningItem chestOpeningItem = getItems().get(3);
		System.out.println(4);
		if (chestOpeningItem.getDisplayItem().getItemMeta().getDisplayName().equalsIgnoreCase(inventory.getItem(13).getItemMeta().getDisplayName()))
		{
			System.out.println(5);
			Inventory finishedCaseOpeningInventory = Bukkit.createInventory(null, 27, SECONDARY + "CaseOpening | Finish");
			InventoryUtils.setDesign(finishedCaseOpeningInventory, new ArrayList<>());
			finishedCaseOpeningInventory.setItem(4, new ItemBuilder(Material.HOPPER).setName(SECONDARY + "Dein Item").build());
			finishedCaseOpeningInventory.setItem(13, chestOpeningItem.getDisplayItem());
			this.chests.put(player.getName(), this.chests.get(player.getName()) - 1);
			chestOpeningItem.executeCommand();
			chestOpeningItem.execute(player);
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					Firework firework = player.getLocation().getWorld().spawn(player.getLocation(), Firework.class);
					FireworkMeta fireworkMeta = firework.getFireworkMeta();
					fireworkMeta.addEffect(FireworkEffect.builder().flicker(false).trail(true).with(FireworkEffect.Type.BALL_LARGE).withColor(Color.RED).withFade(Color.BLACK).withColor(Color.RED).build());
					fireworkMeta.setPower(1);
					firework.setFireworkMeta(fireworkMeta);
				}
			}.runTaskLater(getBelohnung().getJavaPlugin(), 20);
			System.out.println(6);
			return finishedCaseOpeningInventory;
		}
		System.out.println(7);
		return null;
	}
	
	private boolean executeOneChestDelay(Inventory inventory, Player player)
	{
		if (player.getOpenInventory().getTopInventory() instanceof CraftInventoryCrafting)
		{
			return false;
		}
		else
		{
			ChestOpeningItem chestOpeningItem = this.items.get(0);
			this.items.add(chestOpeningItem);
			this.items.remove(chestOpeningItem);
			for (int i = 0; i < 7; i++)
			{
				inventory.setItem(i + 10, this.items.get(i).getDisplayItem());
			}
			return true;
		}
	}
	
	public void disable(MySQL mySQL)
	{
		for (Player players : Bukkit.getOnlinePlayers())
		{
			String name = players.getName();
			mySQL.updateSync("UPDATE Chests SET chests='" + this.chests.get(name) + "' WHERE uuid='" + players.getUniqueId() + "'");
			this.chests.remove(name);
		}
	}
}
