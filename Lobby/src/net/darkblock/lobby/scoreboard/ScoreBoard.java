package net.darkblock.lobby.scoreboard;

import net.darkblock.lobby.scoreboard.listener.ScoreBoardListener;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.mysql.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 08.02.2018  08:06.
 */
public class ScoreBoard
{
	public ScoreBoard(JavaPlugin javaPlugin, MySQL mySQL, CoinsAPI coinsAPI)
	{
		new ScoreBoardListener(javaPlugin, mySQL, coinsAPI);
	}
}
