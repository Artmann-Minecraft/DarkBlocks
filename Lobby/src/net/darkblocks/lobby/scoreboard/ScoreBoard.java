/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.lobby.scoreboard;

import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.lobby.scoreboard.listener.ScoreBoardListener;
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
