package net.darkblocks.cores.utils;

import net.darkblocks.dark.spigot.utils.ScoreBoardUtils;
import net.minecraft.server.v1_8_R3.ScoreboardScore;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 06.02.2018  01:56.
 */
public class ScoreBoard extends ScoreBoardUtils
{
	public static void update(Player player, String displayName, List<Core> cores, int kills)
	{
		List<ScoreboardScore> score = new ArrayList<>();
		int i = 0;
		for (Core core : cores)
		{
			score.add(setScoreboardScore(core.getGameTeam().getName() + TEXT + "[" + core.getGameTeam().getPlayers().size() + TEXT + "]", 4 + i));
			if (core.isAttacked())
			{
				score.add(setScoreboardScore(IMPORTANT + "⚠" + TEXT + core.getName(), 3 + i));
			}
			else
			{
				if (core.getLocation().getBlock().getType() == Material.BEACON)
				{
					score.add(setScoreboardScore(IMPORTANT + "✔" + TEXT + core.getName(), 3 + i));
				}
				else
				{
					score.add(setScoreboardScore(IMPORTANT + "✖" + TEXT + core.getName(), 3 + i));
				}
			}
			i++;
		}
		score.add(setScoreboardScore(" ", i + 4));
		score.add(setScoreboardScore("  ", 2));
		score.add(setScoreboardScore(TEXT + "Kills" + IMPORTANT + ":", 1));
		score.add(setScoreboardScore("" + IMPORTANT + kills, 0));
		sendScoreBoard(player, displayName, score);
	}
}
