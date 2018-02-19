/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblock.lobby.extras.belohnung.listener;

import lombok.Getter;
import lombok.NonNull;
import net.darkblock.lobby.extras.belohnung.Belohnung;
import net.darkblock.lobby.extras.belohnung.utils.CaseOpeningItem;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.spigot.builder.InventoryBuilder;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.events.PlayerDisconnectEvent;
import net.darkblocks.dark.spigot.events.PlayerUpdateCoinsEvent;
import net.darkblocks.dark.spigot.utils.InventoryUtils;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.*;
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
public class CaseOpeningListener implements Listener
{
	private final Map<String, Integer> chests;
	private final LinkedList<CaseOpeningItem> items;
	private final List<String> player;
	private final Belohnung belohnung;
	private final ItemBuilder againItem;
	private final Random random;
	
	public CaseOpeningListener(JavaPlugin javaPlugin, Belohnung belohnung)
	{
		this.chests = new HashMap<>();
		this.items = new LinkedList<>();
		this.player = new ArrayList<>();
		this.belohnung = belohnung;
		this.againItem = new ItemBuilder(Material.CHEST).setName(SECONDARY + "Weitere Kiste öffnen");
		this.random = new Random();
		getBelohnung().getMySQL().update("CREATE TABLE IF NOT EXISTS Chests(uuid VARCHAR (50), `chests` INT, PRIMARY KEY (uuid))", () -> {
			addItem(new CaseOpeningItem("1000 Coins", new ItemBuilder(Material.GOLD_NUGGET).setName(SECONDARY + "1000 Coins").build())
			{
				@Override
				public void execute(Player player)
				{
					player.sendMessage(belohnung.getCoinsAPI().addCoins(player.getUniqueId(), String.valueOf(1000), null));
				}
			}, 80);
			addItem(new CaseOpeningItem("5000 Coins", new ItemBuilder(Material.GOLD_INGOT).setName(SECONDARY + "5000 Coins").build())
			{
				@Override
				public void execute(Player player)
				{
					player.sendMessage(belohnung.getCoinsAPI().addCoins(player.getUniqueId(), String.valueOf(5000), null));
				}
			}, 40);
			addItem(new CaseOpeningItem("10000 Coins", new ItemBuilder(Material.GOLD_BLOCK).setName(SECONDARY + "10000 Coins").build())
			{
				@Override
				public void execute(Player player)
				{
					player.sendMessage(belohnung.getCoinsAPI().addCoins(player.getUniqueId(), String.valueOf(10000), null));
				}
			}, 20);
			addItem(new CaseOpeningItem("2 Kisten", new ItemBuilder(Material.CHEST).setName(SECONDARY + "2 Kisten").build())
			{
				@Override
				public void execute(Player player)
				{
					CaseOpeningListener.this.chests.put(player.getName(), CaseOpeningListener.this.chests.get(player.getName()) + 2);
				}
			}, 10);
			Bukkit.getPluginManager().registerEvents(this, javaPlugin);
		});
		for (int i = 0; i < 10; i++)
		{
			Collections.shuffle(this.items);
		}
	}
	
	private void addItem(@NonNull CaseOpeningItem caseOpeningItem, int chance)
	{
		for (int i = 0; i < chance; i++)
		{
			this.items.add(caseOpeningItem);
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
		if (event.getInventory().getName() != null)
		{
			if (this.player.contains(event.getPlayer().getName()) && event.getInventory().getName().equalsIgnoreCase(SECONDARY + "CaseOpening"))
			{
				new BukkitRunnable()
				{
					@Override
					public void run()
					{
						event.getPlayer().openInventory(finished((Player) event.getPlayer(), event.getInventory()));
					}
				}.runTaskLater(getBelohnung().getJavaPlugin(), 1);
				this.player.remove(event.getPlayer().getName());
			}
			else if (event.getInventory().getName().equalsIgnoreCase(SECONDARY + "CaseOpening | Finish"))
			{
				for (int i = 9; i < event.getPlayer().getInventory().getSize(); i++)
				{
					event.getPlayer().getInventory().setItem(i, new ItemStack(Material.AIR));
				}
			}
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
						if (this.chests.get(player.getName()) != null)
						{
							if (event.isShiftClick() || this.chests.get(player.getName()) < 1)
							{
								player.openInventory(new InventoryBuilder(player, InventoryType.HOPPER, SECONDARY + "CaseOpening | Buy")
										.setDesign(new ArrayList<>())
										.setItem(0, new ItemBuilder(Material.STAINED_CLAY).setDurability((short) 5).setName(SECONDARY + "Kaufen").setLore(TEXT + "Eine Kiste kostet" + IMPORTANT + " 5000 Coins").build())
										.setItem(4, new ItemBuilder(Material.STAINED_CLAY).setDurability((short) 14).setName(SECONDARY + "Abbrechen").build())
										.setItem(2, new ItemBuilder(Material.CHEST).setName(SECONDARY + "CaseOpening").setLore(Collections.singletonList(TEXT + "Du hast noch " + IMPORTANT + this.chests.get(player.getName()) + TEXT + " Kisten")).build())
										.build());
							}
							else
							{
								performCaseOpening(player);
							}
						}
						else
						{
							player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Es ist ein " + IMPORTANT + "Fehler " + TEXT + "aufgetreten");
							player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Bitte betreten den " + IMPORTANT + "Server " + TEXT + "neu");
						}
					}
					break;
				case "caseopening | buy":
					short durability = itemStack.getDurability();
					switch (durability)
					{
						case 5:
							getBelohnung().getCoinsAPI().getCoins(player.getUniqueId(), result -> {
								if (Integer.valueOf(result) < 5000)
								{
									player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du hast nicht genug Coins (dir fehlen " + IMPORTANT + result + " Coins" + TEXT + ")");
								}
								else
								{
									this.chests.put(player.getName(), this.chests.get(player.getName()) + 1);
									player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Dir wurde eine " + IMPORTANT + "Kiste " + TEXT + "hinzugefügt");
									player.getOpenInventory().setItem(2, new ItemBuilder(Material.CHEST).setName(SECONDARY + "CaseOpening").setLore(Collections.singletonList(TEXT + "Du hast noch " + IMPORTANT + this.chests.get(player.getName()) + TEXT + " Kisten")).build());
									getBelohnung().getCoinsAPI().removeCoins(player.getUniqueId(), String.valueOf(5000), result1 -> Bukkit.getPluginManager().callEvent(new PlayerUpdateCoinsEvent(player, result1)));
								}
							});
							break;
						case 14:
							player.closeInventory();
							getBelohnung().getBelohnungListener().openBelohungsInventory(player);
							break;
						case 0:
							if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.CHEST)
							{
								if (this.chests.get(player.getName()) != null)
								{
									if (event.isShiftClick() || this.chests.get(player.getName()) < 1)
									{
										player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du hast nicht genug " + IMPORTANT + "Kisten");
									}
									else
									{
										performCaseOpening(player);
									}
								}
								else
								{
									player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Es ist ein " + IMPORTANT + "Fehler " + TEXT + "aufgetreten");
									player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Bitte betreten den " + IMPORTANT + "Server " + TEXT + "neu");
								}
							}
							break;
					}
					break;
				case "caseopening | finish":
					if (event.getClickedInventory() == player.getOpenInventory().getBottomInventory() && event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.CHEST)
					{
						if (this.chests.get(player.getName()) != null)
						{
							if (this.chests.get(player.getName()) != null)
							{
								if (this.chests.get(player.getName()) < 1)
								{
									player.openInventory(new InventoryBuilder(player, InventoryType.HOPPER, SECONDARY + "CaseOpening | Buy")
											.setDesign(new ArrayList<>())
											.setItem(0, new ItemBuilder(Material.STAINED_CLAY).setDurability((short) 5).setName(SECONDARY + "Kaufen").setLore(TEXT + "Eine Kiste kostet" + IMPORTANT + " 5000 Coins").build())
											.setItem(4, new ItemBuilder(Material.STAINED_CLAY).setDurability((short) 14).setName(SECONDARY + "Abbrechen").build())
											.setItem(2, new ItemBuilder(Material.CHEST).setName(SECONDARY + "CaseOpening").setLore(Collections.singletonList(TEXT + "Du hast noch " + IMPORTANT + this.chests.get(player.getName()) + TEXT + " Kisten")).build())
											.build());
								}
								else
								{
									performCaseOpening(player);
								}
							}
							else
							{
								player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Es ist ein " + IMPORTANT + "Fehler " + TEXT + "aufgetreten");
								player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Bitte betreten den " + IMPORTANT + "Server " + TEXT + "neu");
							}
						}
						break;
					}
			}
		}
	}
	
	private void performCaseOpening(Player player)
	{
		Collections.shuffle(this.items);
		Inventory caseOpeningInventory = new InventoryBuilder(player, 27, SECONDARY + "CaseOpening").setDesign(new ArrayList<>()).setItem(4, new ItemBuilder(Material.HOPPER).setName(SECONDARY + "Dein Item").build()).build();
		player.openInventory(caseOpeningInventory);
		executeOneChestDelay(caseOpeningInventory, player);
		new Thread(() -> {
			this.player.add(player.getName());
			try
			{
				for (int i = 0; i < 100; i++)
				{
					if (!executeOneChestDelay(caseOpeningInventory, player))
					{
						return;
					}
					Thread.sleep((long) (25));
					player.playSound(player.getLocation(), Sound.BURP, 1, 1);
				}
				for (int i = 0; i < 50; i++)
				{
					if (!executeOneChestDelay(caseOpeningInventory, player))
					{
						return;
					}
					Thread.sleep((long) (50));
					player.playSound(player.getLocation(), Sound.BURP, 1, 1);
				}
				for (int i = 0; i < 25; i++)
				{
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
			this.player.remove(player.getName());
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.openInventory(finished(player, caseOpeningInventory));
				}
			}.runTask(getBelohnung().getJavaPlugin());
		}).start();
	}
	
	private Inventory finished(Player player, Inventory inventory)
	{
		CaseOpeningItem caseOpeningItem = getItems().get(3);
		if (caseOpeningItem.getDisplayItem().getItemMeta().getDisplayName().equalsIgnoreCase(inventory.getItem(13).getItemMeta().getDisplayName()))
		{
			Inventory finishedCaseOpeningInventory = Bukkit.createInventory(null, 27, SECONDARY + "CaseOpening | Finish");
			InventoryUtils.setDesign(finishedCaseOpeningInventory, new ArrayList<>());
			finishedCaseOpeningInventory.setItem(4, new ItemBuilder(Material.HOPPER).setName(SECONDARY + "Dein Item").build());
			finishedCaseOpeningInventory.setItem(13, caseOpeningItem.getDisplayItem());
			this.chests.put(player.getName(), this.chests.get(player.getName()) - 1);
			caseOpeningItem.execute(player);
			player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du hast " + PRIMARY + caseOpeningItem.getName() + TEXT + " gewonnen");
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
			InventoryUtils.setDesign(player.getInventory(), new ArrayList<>());
			player.getInventory().setItem(22, CaseOpeningListener.this.againItem.setLore(TEXT + "Du hast noch " + IMPORTANT + getBelohnung().getCaseOpeningListener().getChests().get(player.getName()) + TEXT + " Kisten").build());
			return finishedCaseOpeningInventory;
		}
		return null;
	}
	
	private boolean executeOneChestDelay(Inventory inventory, Player player)
	{
		if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase(SECONDARY + "CaseOpening"))
		{
			return false;
		}
		else
		{
			this.items.addLast(this.items.get(this.random.nextInt(this.items.size())));
			this.items.removeFirst();
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
