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
import net.darkblocks.dark.java.utils.ServerState;
import net.darkblocks.dark.java.utils.ValueType;
import net.darkblocks.dark.segdocloud.manager.CloudManager;
import net.darkblocks.dark.spigot.config.Configuration;
import net.darkblocks.dark.spigot.events.ServerStateChangeEvent;
import net.darkblocks.dark.spigot.plugin.DarkPlugin;
import net.darkblocks.dark.spigot.utils.MapsUtils;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.gungame.kits.KitManager;
import net.darkblocks.gungame.listener.InGameListener;
import net.darkblocks.gungame.listener.ShopListener;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.*;

import static net.darkblocks.dark.universal.messages.Colors.EXTRA;
import static net.darkblocks.dark.universal.messages.Colors.PRIMARY;

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
		messages.put("dark.prefix", "§f" + EXTRA + "[" + PRIMARY + EXTRA + "Cores§f" + EXTRA + "] §r");
		messages.put("dark.servername", "" + PRIMARY + EXTRA + "DarkBlocks§f" + EXTRA + "." + PRIMARY + EXTRA + "Net");
		new Messages(messages);
		MySQL mySQL = new MySQL();
		UserManager userManager = new UserManager(mySQL, null);
		new Core(this, mySQL, userManager, new GroupManager(mySQL, null));
		List<String> maps = new ArrayList<>(MapsUtils.loadMapNames(this));
		String map = maps.get(new Random().nextInt(maps.size()));
		MapsUtils.loadMap(map);
		new InGameListener(this, new KitManager(this), new ShopListener(this), MapsUtils.loadSpawn(Configuration.loadConfiguration(new File(this.getDataFolder() + File.separator + "maps" + File.separator + map + ".yml")), "OneSpawn"), new StatsAPI(this, Arrays.asList("Punkte", "Kills", "Tode", "MaxKillStreak")), new CoinsAPI("Coins", ValueType.INTEGER, mySQL), map);
		new CloudManager(this, "- - -");
		Bukkit.getPluginManager().callEvent(new ServerStateChangeEvent(ServerState.STARTUP, ServerState.INGAME));
	}
}
