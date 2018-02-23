/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.gungame;

import net.darkblocks.core.spigot.Core;
import net.darkblocks.core.universal.permissions.manager.GroupManager;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.java.mysql.StatsAPI;
import net.darkblocks.dark.java.utils.ValueType;
import net.darkblocks.dark.segdocloud.manager.CloudManager;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.config.Configuration;
import net.darkblocks.dark.spigot.plugin.DarkPlugin;
import net.darkblocks.dark.spigot.utils.Items;
import net.darkblocks.dark.spigot.utils.MapsUtils;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.gungame.kits.manager.KitManager;
import net.darkblocks.gungame.listener.InGameListener;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 17.02.2018  15:27.
 */
public class GunGame extends DarkPlugin
{
	@Override
	public synchronized void onEnable()
	{
		super.onEnable();
		Map<String, String> messages = new HashMap<>();
		messages.put("dark.prefix", "§f" + EXTRA + "[" + PRIMARY + EXTRA + "GunGame§f" + EXTRA + "] §r");
		messages.put("dark.servername", "" + PRIMARY + EXTRA + "DarkBlocks§f" + EXTRA + "." + PRIMARY + EXTRA + "Net");
		messages.put("dark.notenoughcoins", messages.get("dark.prefix") + TEXT + "Du hast nicht " + PRIMARY + "genug " + IMPORTANT + "Coins");
		new Messages(messages);
		MySQL mySQL = new MySQL();
		UserManager userManager = new UserManager(mySQL, null);
		new Core(this, mySQL, userManager, new GroupManager(mySQL, null));
		List<String> maps = MapsUtils.loadMapNames(this);
		String map = maps.get(new Random().nextInt(maps.size()));
		MapsUtils.loadMap(map);
		new InGameListener(this, new KitManager(this), MapsUtils.loadSpawn(Configuration.loadConfiguration(new File(getDataFolder(), "spawns.yml")), map), new StatsAPI(this, Arrays.asList("Punkte", "Kills", "Tode", "MaxKillStreak")), new CoinsAPI("Coins", ValueType.INTEGER, mySQL), map);
		Random random = new Random();
		ItemStack itemStack = new ItemBuilder(Material.LEATHER_BOOTS).setColor(Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256))).build();
		Configuration configuration = Configuration.loadConfiguration(new File(getDataFolder(), "shop.yml"));
		for (int i = 1; i < configuration.getInt("count"); i++)
		{
			Location location = new Location(Bukkit.getWorld(configuration.getString("Shop." + i + ".World")), configuration.getDouble("Shop." + i + ".X"), configuration.getDouble("Shop." + i + ".Y"), configuration.getDouble("Shop." + i + ".Z"), (float) configuration.getDouble("Shop." + i + ".Yaw"), (float) configuration.getDouble("Shop." + i + ".Pitch"));
			ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
			armorStand.setCustomName(SECONDARY + "Shop");
			armorStand.setCustomNameVisible(true);
			armorStand.setGravity(false);
			armorStand.setVisible(false);
			armorStand.setSmall(true);
			armorStand.setHelmet(Items.CHEST.getItemStack());
			armorStand.setChestplate(itemStack);
			armorStand.setLeggings(itemStack);
			armorStand.setBoots(itemStack);
		}
		new CloudManager(this, "- - -");
	}
}
