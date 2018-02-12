package net.darkblock.lobby.scoreboard.utils;

import lombok.NonNull;
import net.darkblocks.core.spigot.utils.ScoreBoardUtils;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.universal.messages.Messages;
import net.minecraft.server.v1_8_R3.ScoreboardScore;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 08.02.2018  07:30.
 */
public class ScoreBoard extends ScoreBoardUtils
{
	public static void sendScoreBoard(@NonNull Player player, MySQL mySQL, CoinsAPI coinsAPI)
	{
		mySQL.query("SELECT `time` FROM OnlineTime WHERE `uuid` = '" + player.getUniqueId() + "'", result -> {
			try
			{
				if (result.next())
				{
					long time = result.getLong(1);
					long hours = 0;
					while (time >= 3600)
					{
						time -= 3600;
						hours++;
					}
					String onlinetime;
					if (hours == 1)
					{
						onlinetime = "" + IMPORTANT + "eine" + TEXT + " Stunde";
					}
					else if (hours != 0)
					{
						onlinetime = "" + IMPORTANT + hours + " " + TEXT + "Stunden";
					}
					else
					{
						onlinetime = IMPORTANT + "0 " + TEXT + "Stunden";
					}
					coinsAPI.getCoins(player.getUniqueId(), coins -> {
						List<ScoreboardScore> score = new ArrayList<>();
						score.add(setScoreboardScore(" ", 12));
						score.add(setScoreboardScore(PRIMARY + "OnlineTime", 11));
						score.add(setScoreboardScore(TEXT + "" + onlinetime, 10));
						score.add(setScoreboardScore("  ", 9));
						score.add(setScoreboardScore(PRIMARY + "Coins", 8));
						score.add(setScoreboardScore(TEXT + "" + coins, 7));
						score.add(setScoreboardScore("   ", 6));
						score.add(setScoreboardScore(PRIMARY + "TeamSpeak", 5));
						score.add(setScoreboardScore(TEXT + "DarkBlocks" + IMPORTANT + "." + TEXT + "Net", 4));
						score.add(setScoreboardScore("    ", 3));
						score.add(setScoreboardScore(PRIMARY + "Webseite", 2));
						score.add(setScoreboardScore(TEXT + "DarkBlocks" + IMPORTANT + "." + TEXT + "Net ", 1));
						sendScoreBoard(player, Messages.getInstance().getShortMessage(ScoreBoard.class, "servername"), score);
					});
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		});
	}
}
