package net.darkblocks.cores.utils;

import net.darkblocks.dark.spigot.team.GameTeam;
import net.darkblocks.dark.spigot.utils.ScoreBoardUtils;
import net.minecraft.server.v1_8_R3.ScoreboardScore;
import org.bukkit.ChatColor;
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
			GameTeam gameTeam = core.getGameTeam();
			ChatColor chatColor = gameTeam.getChatColor();
			score.add(setScoreboardScore(chatColor + "Team " + gameTeam.getName() + " " + IMPORTANT + "[" + TEXT + gameTeam.getPlayers().size() + IMPORTANT + "]", 4 + i));
			i++;
			if (core.isAttacked())
			{
				if (core.getLocation().getBlock().getType() == Material.BEACON)
				{
					score.add(setScoreboardScore(IMPORTANT + "§e⚠" + TEXT + chatColor + core.getName(), 3 + i));
				}
				else
				{
					score.add(setScoreboardScore(IMPORTANT + "§c✖" + TEXT + chatColor + core.getName(), 3 + i));
				}
			}
			else if (core.getLocation().getBlock().getType() == Material.BEACON)
			{
				score.add(setScoreboardScore(IMPORTANT + "§a✔" + TEXT + chatColor + core.getName(), 3 + i));
			}
		}
		score.add(setScoreboardScore(" ", i + 4));
		score.add(setScoreboardScore("  ", 2));
		score.add(setScoreboardScore(TEXT + "Kills" + IMPORTANT + ":", 1));
		score.add(setScoreboardScore("" + IMPORTANT + kills, 0));
		sendScoreBoard(player, displayName, score);
	}
}
