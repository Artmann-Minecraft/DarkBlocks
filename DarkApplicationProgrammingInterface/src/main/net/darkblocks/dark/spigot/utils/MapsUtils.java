package net.darkblocks.dark.spigot.utils;

import lombok.NonNull;
import net.darkblocks.dark.java.config.PropertiesConfig;
import net.darkblocks.dark.spigot.config.Configuration;
import net.darkblocks.dark.spigot.team.GameTeam;
import net.darkblocks.dark.spigot.team.SpectatorManager;
import net.darkblocks.dark.spigot.team.TeamManager;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * Created by LartyHD on 04.01.2018  20:31.
 */
public class MapsUtils
{
	public static List<String> loadMapNames(JavaPlugin javaPlugin) throws IndexOutOfBoundsException
	{
		List<String> mapNamesList = Configuration.loadConfiguration(new File(javaPlugin.getDataFolder() + File.separator + "data.yml")).getStringList("maps");
		if (mapNamesList.isEmpty())
		{
			throw new IndexOutOfBoundsException("No Maps in Config");
		}
		return mapNamesList;
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
	
	public static void loadSpawns(@NonNull Configuration configuration, @NonNull TeamManager teamManager, SpectatorManager spectatorManager)
	{
		for (GameTeam team : teamManager.getTeams())
		{
			team.setLocation(new Location(Bukkit.getWorld(configuration.getString("spawns." + team.getName() + ".World")), configuration.getDouble("spawns." + team.getName() + ".X"), configuration.getDouble("spawns." + team.getName() + ".Y"), configuration.getDouble("spawns." + team.getName() + ".Z"), (float) configuration.getDouble("spawns." + team.getName() + ".yaw"), (float) configuration.getDouble("spawns." + team.getName() + ".pitch")));
		}
		if (spectatorManager != null)
		{
			spectatorManager.getSpectators().setLocation(new Location(Bukkit.getWorld(configuration.getString("spawns.spectator.World")), configuration.getDouble("spawns.spectator.X"), configuration.getDouble("spawns.spectator.Y"), configuration.getDouble("spawns.spectator.Z"), (float) configuration.getDouble("spawns.spectator.yaw"), (float) configuration.getDouble("spawns.spectator.pitch")));
		}
	}
	
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
		return location1.getWorld() == location2.getWorld() && location1.getBlockX() == location2.getBlockX() && location1.getBlockY() == location2.getBlockY() && location1.getBlockZ() == location2.getBlockZ();
	}
}
