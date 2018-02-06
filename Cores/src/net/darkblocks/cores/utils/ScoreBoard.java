package net.darkblocks.cores.utils;

import net.darkblocks.dark.spigot.team.GameTeam;
import net.darkblocks.dark.spigot.utils.ScoreBoardUtils;
import net.minecraft.server.v1_8_R3.ScoreboardScore;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 06.02.2018  01:56.
 */
public class ScoreBoard extends ScoreBoardUtils
{
	public static void update(Player player, String displayName, List<Core> cores, int kills)
	{
		Set<GameTeam> teams = new HashSet<>();
		for (Core core : cores)
		{
			teams.add(core.getGameTeam());
		}
		int size = cores.size() / teams.size();
		List<ScoreboardScore> score = new ArrayList<>();
		score.add(setScoreboardScore(" ", 15));
		int i = 15;
		for (GameTeam gameTeam : teams)
		{
			i--;
			score.add(setScoreboardScore(gameTeam.getChatColor() + "Team " + gameTeam.getName() + " " + IMPORTANT + "[" + TEXT + gameTeam.getPlayers().size() + IMPORTANT + "]", i));
			for (Core core : cores)
			{
				i--;
				if (core.isAttacked())
				{
					if (core.getLocation().getBlock().getType() == Material.BEACON)
					{
						score.add(setScoreboardScore(IMPORTANT + "§e⚠" + TEXT + gameTeam.getChatColor() + core.getName(), i));
					}
					else
					{
						score.add(setScoreboardScore(IMPORTANT + "§c✖" + TEXT + gameTeam.getChatColor() + core.getName(), i));
					}
				}
				else if (core.getLocation().getBlock().getType() == Material.BEACON)
				{
					score.add(setScoreboardScore(IMPORTANT + "§a✔" + TEXT + gameTeam.getChatColor() + core.getName(), i));
				}
			}
		}
		score.add(setScoreboardScore("  ", 2));
		score.add(setScoreboardScore(TEXT + "Kills" + IMPORTANT + ":", 1));
		score.add(setScoreboardScore("" + IMPORTANT + kills, 0));
		sendScoreBoard(player, displayName, score);
	}
}
