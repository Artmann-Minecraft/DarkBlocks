/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblock.lobby;

import lombok.Getter;
import net.darkblock.lobby.listener.DoubleJumpListener;
import net.darkblock.lobby.listener.MainListener;
import net.darkblock.lobby.navigator.Navigator;
import net.darkblock.lobby.profil.Profil;
import net.darkblock.lobby.scoreboard.ScoreBoard;
import net.darkblock.lobby.verstecker.Verstecker;
import net.darkblocks.core.spigot.Core;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.java.utils.ValueType;
import net.darkblocks.dark.segdocloud.manager.CloudManager;
import net.darkblocks.dark.spigot.events.cashed.CashedEventsManager;
import net.darkblocks.dark.spigot.plugin.DarkPlugin;
import net.darkblocks.dark.spigot.utils.MapsUtils;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static net.darkblocks.dark.universal.messages.Colors.EXTRA;
import static net.darkblocks.dark.universal.messages.Colors.PRIMARY;

/**
 * Created by LartyHD on 07.07.2017  19:14.
 * Project: Lobby 2.0
 */
@Getter
public class Lobby extends DarkPlugin
{
	private final Map<String, Boolean> navigatorAnimation;
	private final MySQL mySQL;
	
	public Lobby()
	{
		this.navigatorAnimation = new HashMap<>();
		this.mySQL = new MySQL();
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		Map<String, String> messages = new HashMap<>();
		messages.put("dark.prefix", "§f" + EXTRA + "[" + PRIMARY + EXTRA + "Lobby§f" + EXTRA + "] §r");
		messages.put("dark.servername", "" + PRIMARY + EXTRA + "DarkBlocks§f" + EXTRA + "." + PRIMARY + EXTRA + "Net");
		new Messages(messages);
		new Core(this, this.mySQL);
		new DoubleJumpListener(this);
		new MainListener(this, MapsUtils.getLobbyLocation(this));
		new ScoreBoard(this, this.mySQL, new CoinsAPI("Coins", ValueType.INTEGER, this.mySQL));
		CashedEventsManager cashedEventsManager = new CashedEventsManager(this);
		new Navigator(this, this.mySQL, this.navigatorAnimation, cashedEventsManager);
		new Verstecker(this, cashedEventsManager);
		new Profil(this, cashedEventsManager, this.navigatorAnimation);
		initWorld();
		new CloudManager(this, "");
	}
	
	private void initWorld()
	{
		World world = Bukkit.getWorld("Lobby");
		world.setTime(6000);
		world.setGameRuleValue("spawnRadius", "0");
		world.setGameRuleValue("doDaylightCycle", "false");
		world.setGameRuleValue("doMobSpawning", "false");
		world.setGameRuleValue("doFireTick", "false");
		world.setWeatherDuration(-1);
		world.setThundering(false);
		world.setStorm(false);
		world.setAutoSave(false);
		world.setDifficulty(Difficulty.PEACEFUL);
		world.setKeepSpawnInMemory(true);
		WorldBorder worldBorder = world.getWorldBorder();
		worldBorder.setCenter(-20.5, -4.5);
		worldBorder.setSize(400);
		worldBorder.setDamageAmount(10000);
		worldBorder.setWarningDistance(0);
		worldBorder.setWarningTime(0);
	}
	
	@Override
	public void onDisable()
	{
		for (Player players : Bukkit.getOnlinePlayers())
		{
			getMySQL().updateSync("UPDATE NavAnimation SET `on` = '" + (getNavigatorAnimation().get(players.getName()) ? 1 : 0) + "' WHERE `uuid` = '" + players.getUniqueId() + "'");
		}
	}
}
