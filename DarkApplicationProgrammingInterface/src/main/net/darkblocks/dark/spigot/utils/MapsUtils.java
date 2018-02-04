package net.darkblocks.dark.spigot.utils;

import net.darkblocks.dark.java.config.PropertiesConfig;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;

/**
 * Created by LartyHD on 04.01.2018  20:31.
 */
@SuppressWarnings("ALL")
public class MapsUtils
{
	@SuppressWarnings("unchecked")
	public static String[] loadMapNames(JavaPlugin javaPlugin) throws IndexOutOfBoundsException
	{
		PropertiesConfig propertiesConfig = new PropertiesConfig(javaPlugin.getDataFolder(), "data.properties");
		if (propertiesConfig != null)
		{
			Object o = propertiesConfig.get("Maps");
			if (o != null && o instanceof String)
			{
				String maps = (String) propertiesConfig.get("Maps");
				return maps.split(" ");
			}
		}
		throw new IndexOutOfBoundsException("No Maps in Config");
	}
	
	public static String getRandomMap(List<String> mapNames) throws IndexOutOfBoundsException
	{
		if (mapNames.isEmpty())
		{
			throw new IndexOutOfBoundsException("Maps sind nicht eingetragen");
		}
		else
		{
			int i = new Random().nextInt(mapNames.size());
			return mapNames.get(i);
		}
	}
	
	public static void loadMap(String mapName)
	{
		World world = Bukkit.getWorld(mapName);
		if (world == null)
		{
			world = Bukkit.createWorld(new WorldCreator(mapName));
		}
		world.setWeatherDuration(-1);
		world.setTime(6000);
		world.setMonsterSpawnLimit(0);
		world.setDifficulty(Difficulty.PEACEFUL);
		world.setDifficulty(Difficulty.EASY);
		world.setKeepSpawnInMemory(false);
		world.setAutoSave(false);
		world.setGameRuleValue("spawnRadius", "0");
		world.setGameRuleValue("doDaylightCycle", "false");
		world.setGameRuleValue("doMobSpawning", "false");
		world.setGameRuleValue("doFireTick", "false");
		world.getEntities().clear();
	}
	
	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	public static Location getLobbyLocation(JavaPlugin javaPlugin)
	{
		PropertiesConfig conf = new PropertiesConfig(javaPlugin.getDataFolder(), "lobby.properties");
		Location location = new Location(Bukkit.getWorld((String) conf.get("lobby.world")), Double.valueOf((String) conf.get("lobby.x")), Double.valueOf((String) conf.get("lobby.y")), Double.valueOf((String) conf.get("lobby.z")), Float.valueOf((String) conf.get("lobby.yaw")), Float.valueOf((String) conf.get("lobby.pitch")));
		location.getWorld().setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		System.out.println("Der Lobby Spawnpoint wurde gesetzt " + location.toString());
		return location;
	}
	
	public static boolean equalsLocation(Location location1, Location location2)
	{
		if (location1.getWorld() == location2.getWorld() && location1.getBlockX() == location2.getBlockX() && location1.getBlockY() == location2.getBlockY() && location1.getBlockZ() == location2.getBlockZ())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
