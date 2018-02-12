package net.darkblock.lobby.scoreboard.listener;

import net.darkblock.lobby.scoreboard.utils.ScoreBoard;
import net.darkblocks.core.spigot.permissions.events.PlayerPermissionsLoadedEvent;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.spigot.events.PlayerUpdateCoinsEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 08.02.2018  08:06.
 */
public class ScoreBoardListener implements Listener
{
	private final MySQL mySQL;
	private final CoinsAPI coinsAPI;
	
	public ScoreBoardListener(JavaPlugin javaPlugin, MySQL mySQL, CoinsAPI coinsAPI)
	{
		this.mySQL = mySQL;
		this.coinsAPI = coinsAPI;
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
	
	@EventHandler
	public void onPlayerPermissionsLoadedEvent(PlayerPermissionsLoadedEvent event)
	{
		ScoreBoard.sendScoreBoard(event.getPlayer(), this.mySQL, this.coinsAPI);
		ScoreBoard.sendTab(event.getUser());
	}
	
	@EventHandler
	public void onPlayerUpdateCoinsEvent(PlayerUpdateCoinsEvent event)
	{
		ScoreBoard.sendScoreBoard(event.getPlayer(), this.mySQL, this.coinsAPI);
	}
}
