package net.darkblocks.cores.utils;

import net.darkblocks.dark.spigot.team.GameTeam;
import net.darkblocks.dark.spigot.team.TeamManager;
import net.darkblocks.dark.spigot.utils.ScoreBoardUtils;
import net.minecraft.server.v1_8_R3.ScoreboardScore;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 06.02.2018  01:56.
 */
public class ScoreBoard extends ScoreBoardUtils
{
	public static void update(Player player, String displayName, Set<Core> cores, TeamManager teamManager, int kills)
	{
		List<ScoreboardScore> score = new ArrayList<>();
		int i = 15;
		score.add(setScoreboardScore(" ", i));
		for (GameTeam gameTeam : teamManager.getTeams())
		{
			i--;
			score.add(setScoreboardScore("  ", i--));
			score.add(setScoreboardScore(gameTeam.getChatColor() + "Team " + gameTeam.getName() + " " + IMPORTANT + "[" + TEXT + gameTeam.getPlayers().size() + IMPORTANT + "]", i));
			for (Core core : cores)
			{
				if (core.getGameTeam().getName().equalsIgnoreCase(gameTeam.getName()))
				{
					i--;
					if (core.isAttacked())
					{
						score.add(setScoreboardScore(IMPORTANT + "§e⚠ " + TEXT + gameTeam.getChatColor() + core.getName(), i));
					}
					else
					{
						if (core.getLocation().getBlock().getType() == Material.BEACON)
						{
							score.add(setScoreboardScore(IMPORTANT + "§a✔ " + TEXT + gameTeam.getChatColor() + core.getName(), i));
						}
						else
						{
							score.add(setScoreboardScore(IMPORTANT + "§c✖ " + TEXT + gameTeam.getChatColor() + core.getName(), i));
						}
					}
				}
			}
		}
		score.add(setScoreboardScore("   ", 2));
		score.add(setScoreboardScore(TEXT + "Kills" + IMPORTANT + ":", 1));
		score.add(setScoreboardScore("" + IMPORTANT + kills, 0));
		sendScoreBoard(player, displayName, score);
	}
}
