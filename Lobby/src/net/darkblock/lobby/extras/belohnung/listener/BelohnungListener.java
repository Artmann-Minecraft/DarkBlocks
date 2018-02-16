package net.darkblock.lobby.extras.belohnung.listener;

import lombok.Getter;
import net.darkblock.lobby.extras.belohnung.Belohnung;
import net.darkblocks.core.universal.permissions.utils.User;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.events.PlayerUpdateCoinsEvent;
import net.darkblocks.dark.spigot.utils.InventoryUtils;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 16.02.2018  13:44.
 */
@Getter
public class BelohnungListener implements Listener
{
	private final Belohnung belohnung;
	
	public BelohnungListener(JavaPlugin javaPlugin, Belohnung belohnung)
	{
		this.belohnung = belohnung;
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
		belohnung.getMySQL().update("CREATE TABLE IF NOT EXISTS Belohnung(`uuid` VARCHAR(50), `ip` VARCHAR(100), `time` BIGINT, PRIMARY KEY(uuid))");
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		getBelohnung().getMySQL().query("SELECT * FROM Belohnung WHERE `uuid` = '" + player.getUniqueId() + "'", result -> {
			if (result.next())
			{
				if (result.getLong("time") - (System.currentTimeMillis() / 1000) < 1)
				{
					player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du kannst dir eine " + IMPORTANT + "Belohnung " + TEXT + "abholen");
				}
			}
			else
			{
				getBelohnung().getMySQL().update("INSERT INTO Belohnung(`uuid`, `time`) VALUES ('" + player.getUniqueId() + "','" + 0 + "')", () -> player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du kannst dir eine " + IMPORTANT + "Belohnung " + TEXT + "abholen"));
			}
		});
	}
	
	@EventHandler
	public void onInteractAtEntity(PlayerInteractAtEntityEvent event)
	{
		Entity entity = event.getRightClicked();
		if (entity instanceof ArmorStand && entity.getName().equalsIgnoreCase(SECONDARY + "Belohnung"))
		{
			event.setCancelled(true);
			Player player = event.getPlayer();
			getBelohnung().getMySQL().query("SELECT * FROM Belohnung WHERE `uuid` = '" + player.getUniqueId() + "'", result -> {
				Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, SECONDARY + "Belohnung");
				InventoryUtils.setDesign(inventory, new ArrayList<>());
				if (result.next())
				{
					if (result.getLong("time") - (System.currentTimeMillis() / 1000) < 1)
					{
						setBelohnungItem(inventory, true);
					}
					else
					{
						setBelohnungItem(inventory, false);
					}
					inventory.setItem(3, new ItemBuilder(Material.CHEST).setName(SECONDARY + "CaseOpening").setLore(Collections.singletonList(TEXT + "Du hast noch " + IMPORTANT + getBelohnung().getChestOpeningListener().getChests().get(player.getName()) + TEXT + " Kisten")).build());
					player.openInventory(inventory);
				}
			});
		}
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event)
	{
		Inventory inventory = event.getInventory();
		String title = inventory.getTitle();
		ItemStack itemStack = event.getCurrentItem();
		if (title != null && title.equalsIgnoreCase(SECONDARY + "Belohnung") && event.getWhoClicked() instanceof Player && itemStack != null && itemStack.getType() == Material.FIREWORK_CHARGE)
		{
			FireworkEffectMeta itemMeta = (FireworkEffectMeta) itemStack.getItemMeta();
			if (itemMeta != null)
			{
				Player player = (Player) event.getWhoClicked();
				UUID uuid = player.getUniqueId();
				if (itemMeta.getEffect() != null)
				{
					player.sendMessage(getBelohnung().getCoinsAPI().addCoins(uuid, String.valueOf(1000), result -> {
						try
						{
							getBelohnung().getChestOpeningListener().getChests().put(player.getName(), getBelohnung().getChestOpeningListener().getChests().get(player.getName()) + 1);
						} catch (NullPointerException ex)
						{
							player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Es ist ein Fehler aufgetreten bitte betrete den Server neu");
							return;
						}
						for (User user : getBelohnung().getUserManager().getUser())
						{
							if (user.getUuid() == player.getUniqueId())
							{
								switch (user.getLowestSortID())
								{
									case 11000:
										getBelohnung().getMySQL().update("UPDATE Belohnung SET `time` = '" + (System.currentTimeMillis() / 1000 + 86400) + "' WHERE `uuid` = '" + uuid + "'", BelohnungListener.this::notifyAll);
										break;
									case 10170:
										getBelohnung().getMySQL().update("UPDATE Belohnung SET `time` = '" + (System.currentTimeMillis() / 1000 + 43200) + "' WHERE `uuid` = '" + uuid + "'", BelohnungListener.this::notifyAll);
										break;
									case 10160:
										getBelohnung().getMySQL().update("UPDATE Belohnung SET `time` = '" + (System.currentTimeMillis() / 1000 + 43200) + "' WHERE `uuid` = '" + uuid + "'", BelohnungListener.this::notifyAll);
										break;
									case 10150:
										getBelohnung().getMySQL().update("UPDATE Belohnung SET `time` = '" + (System.currentTimeMillis() / 1000 + 21600) + "' WHERE `uuid` = '" + uuid + "'", BelohnungListener.this::notifyAll);
										break;
									default:
										getBelohnung().getMySQL().update("UPDATE Belohnung SET `time` = '" + (System.currentTimeMillis() / 1000 + 10800) + "' WHERE `uuid` = '" + uuid + "'", BelohnungListener.this::notifyAll);
										break;
								}
								try
								{
									wait();
								} catch (InterruptedException ex)
								{
									ex.printStackTrace();
								}
								player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du hast dir erfolgreich deine " + IMPORTANT + "Belohnung " + TEXT + "abgeholt");
							}
						}
						Bukkit.getPluginManager().callEvent(new PlayerUpdateCoinsEvent(player, result));
						getBelohnung().getMySQL().query("SELECT * FROM Belohnung WHERE `uuid` = '" + player.getUniqueId() + "'", result1 -> {
							if (result1.next())
							{
								player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du kannst dir wieder in " + getZeitMessage(result1.getLong("time") - (System.currentTimeMillis() / 1000)) + " die nächste Belohnung abholen");
							}
						});
						player.closeInventory();
					}));
				}
			}
		}
	}
	
	private void setBelohnungItem(Inventory inventory, boolean belohnung)
	{
		ItemStack itemStack = new ItemBuilder(Material.FIREWORK_CHARGE).setName(SECONDARY + "Belohnung").hideItemFlags().build();
		if (belohnung)
		{
			FireworkEffectMeta itemMeta = (FireworkEffectMeta) itemStack.getItemMeta();
			itemMeta.setEffect(FireworkEffect.builder().withColor(Color.RED).withFade(Color.BLACK).build());
			itemStack.setItemMeta(itemMeta);
			inventory.setItem(1, itemStack);
			return;
		}
		inventory.setItem(1, itemStack);
	}
	
	private String getZeitMessage(long time)
	{
		String remainingTime = "";
		long minutes = 0;
		long hours = 0;
		while (time >= 60)
		{
			time -= 60;
			minutes++;
		}
		while (minutes >= 60)
		{
			minutes -= 60;
			hours++;
		}
		if (hours == 1)
		{
			remainingTime = remainingTime + "" + IMPORTANT + hours + TEXT + " Stunde ";
		}
		else if (hours != 0)
		{
			remainingTime = remainingTime + "" + IMPORTANT + hours + TEXT + " Stunden ";
		}
		if (minutes == 1)
		{
			remainingTime = remainingTime + "" + IMPORTANT + minutes + TEXT + " Minute ";
		}
		else if (minutes != 0)
		{
			remainingTime = remainingTime + "" + IMPORTANT + minutes + TEXT + " Minuten ";
		}
		if (time == 1)
		{
			remainingTime = remainingTime + "" + IMPORTANT + time + TEXT + " Sekunde ";
		}
		else
		{
			remainingTime = remainingTime + "" + IMPORTANT + time + TEXT + " Sekunden ";
		}
		return remainingTime.substring(0, remainingTime.length() - 1);
	}
}
