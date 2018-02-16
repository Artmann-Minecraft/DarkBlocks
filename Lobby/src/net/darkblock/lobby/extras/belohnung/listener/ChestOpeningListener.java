package net.darkblock.lobby.extras.belohnung.listener;

import lombok.NonNull;
import net.darkblock.lobby.extras.belohnung.utils.ChestOpeningItem;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static net.darkblocks.dark.universal.messages.Colors.SECONDARY;

/**
 * Created by LartyHD on 16.02.2018  13:47.
 */
public class ChestOpeningListener implements Listener
{
	private final Map<String, Integer> chests;
	private final List<ChestOpeningItem> items;
	
	public ChestOpeningListener(JavaPlugin javaPlugin, CoinsAPI coinsAPI)
	{
		this.chests = new HashMap<>();
		this.items = new ArrayList<>();
		addItem(new ChestOpeningItem("1000 Coins", new ItemBuilder(Material.GOLD_NUGGET).setName(SECONDARY + "1000 Coins").build())
		{
			@Override
			public void execute(Player player)
			{
				player.sendMessage(coinsAPI.addCoins(player.getUniqueId(), String.valueOf(1000), null));
			}
		}, 100);
		addItem(new ChestOpeningItem("5000 Coins", new ItemBuilder(Material.GOLD_INGOT).setName(SECONDARY + "5000 Coins").build())
		{
			@Override
			public void execute(Player player)
			{
				player.sendMessage(coinsAPI.addCoins(player.getUniqueId(), String.valueOf(5000), null));
			}
		}, 40);
		addItem(new ChestOpeningItem("10000 Coins", new ItemBuilder(Material.GOLD_BLOCK).setName(SECONDARY + "10000 Coins").build())
		{
			@Override
			public void execute(Player player)
			{
				player.sendMessage(coinsAPI.addCoins(player.getUniqueId(), String.valueOf(10000), null));
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
		for (int i = 0; i < 10; i++)
		{
			Collections.shuffle(this.items);
		}
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
	
	private void addItem(@NonNull ChestOpeningItem chestOpeningItem, int chance)
	{
		for (int i = 0; i < chance; i++)
		{
			this.items.add(chestOpeningItem);
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
