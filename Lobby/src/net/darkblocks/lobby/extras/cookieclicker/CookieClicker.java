/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.lobby.extras.cookieclicker;

import lombok.Getter;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.config.Configuration;
import net.darkblocks.lobby.extras.cookieclicker.commands.CookiesCommand;
import net.darkblocks.lobby.extras.cookieclicker.listener.CookieListener;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

import static net.darkblocks.dark.universal.messages.Colors.SECONDARY;

/**
 * Created by LartyHD on 13.02.2018  00:23.
 */
@Getter
public class CookieClicker
{
	private final Map<UUID, Double> cookies;
	private final Map<UUID, Double> cookiesPerClick;
	private final Set<UUID> blockedClicks;
	private final MySQL mySQL;
	
	public CookieClicker(JavaPlugin javaPlugin, MySQL mySQL, CoinsAPI coinsAPI)
	{
		this.cookies = new HashMap<>();
		this.cookiesPerClick = new HashMap<>();
		this.blockedClicks = new HashSet<>();
		this.mySQL = mySQL;
		mySQL.update("CREATE TABLE IF NOT EXISTS Cookies(uuid VARCHAR(50), coins DOUBLE, PRIMARY KEY(uuid))", () -> mySQL.update("CREATE TABLE IF NOT EXISTS CookiesPerClick(uuid VARCHAR(50), coins DOUBLE, PRIMARY KEY(uuid))", () -> {
			new CookiesCommand(javaPlugin, this);
			new CookieListener(javaPlugin, this, coinsAPI);
		}));
		Configuration configuration = Configuration.loadConfiguration(javaPlugin.getDataFolder(), new File("extras.yml"));
		Location cookieClicker = new Location(Bukkit.getWorld(configuration.getString("Extras.CookieClicker.World")), configuration.getDouble("Extras.CookieClicker.X"), configuration.getDouble("Extras.CookieClicker.Y"), configuration.getDouble("Extras.CookieClicker.Z"));
		ArmorStand armorStand = cookieClicker.getWorld().spawn(cookieClicker.add(0, -1.5, 0), ArmorStand.class);
		armorStand.setGravity(false);
		armorStand.setVisible(false);
		armorStand.setBasePlate(false);
		armorStand.setCustomName(SECONDARY + "CookieClicker");
		armorStand.setCustomNameVisible(true);
		armorStand.setHelmet(new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setOwnerFromURL("http://textures.minecraft.net/texture/b592cf9f42a5a8c995968493fdd1b11e0b69aad6473ff45384abe58b7fc7c7", "QuadratCookie").build());
		Location cookieShop = new Location(Bukkit.getWorld(configuration.getString("Extras.CookieShop.World")), configuration.getDouble("Extras.CookieShop.X"), configuration.getDouble("Extras.CookieShop.Y"), configuration.getDouble("Extras.CookieShop.Z"));
		Villager villager = cookieShop.getWorld().spawn(cookieShop, Villager.class);
		NBTTagCompound compound = new NBTTagCompound();
		((CraftEntity) villager).getHandle().e(compound);
		compound.setInt("NoAI", 1);
		((CraftEntity) villager).getHandle().f(compound);
		villager.setProfession(Villager.Profession.FARMER);
		villager.setAdult();
		villager.setCustomName(SECONDARY + "CookieShop");
		villager.setCustomNameVisible(true);
	}
	
	public void disable(MySQL mySQL)
	{
		for (UUID uuid : getCookies().keySet())
		{
			mySQL.updateSync("UPDATE Cookies SET coins='" + getCookies().get(uuid) + "' WHERE uuid='" + uuid.toString() + "'");
		}
		for (UUID uuid : getCookiesPerClick().keySet())
		{
			mySQL.updateSync("UPDATE CookiesPerClick SET coins='" + getCookiesPerClick().get(uuid) + "' WHERE uuid='" + uuid + "'");
		}
	}
}
