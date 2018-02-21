/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.lobby;

import lombok.Getter;
import net.darkblocks.core.spigot.Core;
import net.darkblocks.core.universal.permissions.manager.GroupManager;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.java.utils.ValueType;
import net.darkblocks.dark.spigot.events.cashed.CashedEventsManager;
import net.darkblocks.dark.spigot.plugin.DarkPlugin;
import net.darkblocks.dark.spigot.utils.MapsUtils;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.lobby.extras.Extras;
import net.darkblocks.lobby.listener.DoubleJumpListener;
import net.darkblocks.lobby.listener.MainListener;
import net.darkblocks.lobby.navigator.Navigator;
import net.darkblocks.lobby.profil.Profil;
import net.darkblocks.lobby.scoreboard.ScoreBoard;
import net.darkblocks.lobby.verstecker.Verstecker;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static net.darkblocks.dark.universal.messages.Colors.EXTRA;
import static net.darkblocks.dark.universal.messages.Colors.PRIMARY;

/**
 * Created by LartyHD on 2018
 */
@Getter
public class Lobby extends DarkPlugin
{
	private final Map<String, Boolean> navigatorAnimation;
	private final MySQL mySQL;
	private Extras extras;
	
	public Lobby()
	{
		this.navigatorAnimation = new HashMap<>();
		this.mySQL = new MySQL();
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		initWorld();
		Map<String, String> messages = new HashMap<>();
		messages.put("dark.prefix", "§f" + EXTRA + "[" + PRIMARY + EXTRA + "Lobby§f" + EXTRA + "] §r");
		messages.put("dark.servername", "" + PRIMARY + EXTRA + "DarkBlocks§f" + EXTRA + "." + PRIMARY + EXTRA + "Net");
		new Messages(messages);
		UserManager userManager = new UserManager(this.mySQL, null);
		new Core(this, this.mySQL, userManager, new GroupManager(this.mySQL, null));
		new DoubleJumpListener(this);
		new MainListener(this, MapsUtils.getLobbyLocation(this), userManager);
		CoinsAPI coinsAPI = new CoinsAPI("Coins", ValueType.INTEGER, this.mySQL);
		new ScoreBoard(this, this.mySQL, coinsAPI);
		CashedEventsManager cashedEventsManager = new CashedEventsManager(this);
		new Navigator(this, this.mySQL, this.navigatorAnimation, cashedEventsManager);
		new Verstecker(this, cashedEventsManager);
		new Profil(this, cashedEventsManager, this.navigatorAnimation);
//		new Sounds(this);
		this.extras = new Extras(this, userManager, this.mySQL, coinsAPI);
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
		for (Entity entity : world.getEntities())
		{
			entity.remove();
		}
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
		this.extras.disable(this.mySQL);
		for (Player players : Bukkit.getOnlinePlayers())
		{
			getMySQL().updateSync("UPDATE NavAnimation SET `on` = '" + (getNavigatorAnimation().get(players.getName()) ? 1 : 0) + "' WHERE `uuid` = '" + players.getUniqueId() + "'");
		}
	}
}
