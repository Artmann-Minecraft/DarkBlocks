/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.gungame.utils;

import net.darkblocks.dark.java.mysql.StatsAPI;
import net.darkblocks.dark.universal.messages.Messages;
import net.minecraft.server.v1_8_R3.ScoreboardScore;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 17.02.2018  16:09.
 */
public class ScoreBoardUtils extends net.darkblocks.core.spigot.utils.ScoreBoardUtils
{
	public static void sendInGameScoreBoard(Player player, String mapName, StatsAPI statsAPI, boolean allowTeams)
	{
		statsAPI.getRank(player.getUniqueId(), "Punkte", rank -> statsAPI.get(player.getUniqueId(), "Punkte", punkte -> {
			List<ScoreboardScore> score = new ArrayList<>();
			score.add(setScoreboardScore(" ", 15));
			score.add(setScoreboardScore(TEXT + "Map" + IMPORTANT + ":", 14));
			score.add(setScoreboardScore(IMPORTANT + mapName, 13));
			score.add(setScoreboardScore("  ", 12));
			score.add(setScoreboardScore(TEXT + "Teams" + IMPORTANT + ":", 11));
			if (allowTeams)
			{
				score.add(setScoreboardScore(IMPORTANT + "erlaubt", 10));
			}
			else
			{
				score.add(setScoreboardScore(IMPORTANT + "verboten", 10));
			}
			score.add(setScoreboardScore("   ", 9));
			score.add(setScoreboardScore(TEXT + "Rang" + IMPORTANT + ":", 8));
			score.add(setScoreboardScore(IMPORTANT + Integer.toString(rank), 7));
			score.add(setScoreboardScore("    ", 6));
			score.add(setScoreboardScore(TEXT + "Punkte" + IMPORTANT + ":", 5));
			score.add(setScoreboardScore(IMPORTANT + Integer.toString(punkte), 4));
			score.add(setScoreboardScore("     ", 3));
			score.add(setScoreboardScore(TEXT + "Weitere Stats", 2));
			score.add(setScoreboardScore(TEXT + "mit " + IMPORTANT + "/Stats", 1));
			sendScoreBoard(player, Messages.getInstance().getShortMessage(ScoreBoardUtils.class, "servername"), score);
		}));
	}
}
