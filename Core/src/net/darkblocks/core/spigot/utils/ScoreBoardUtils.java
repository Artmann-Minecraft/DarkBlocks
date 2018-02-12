package net.darkblocks.core.spigot.utils;

import net.darkblocks.core.universal.permissions.utils.User;
import net.darkblocks.dark.spigot.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by LartyHD on 12.02.2018  12:59.
 */
public class ScoreBoardUtils extends net.darkblocks.dark.spigot.utils.ScoreBoardUtils
{
	@SuppressWarnings("deprecation")
	public static void sendTab(User user)
	{
		org.bukkit.scoreboard.Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		int lowestSortID = user.getLowestSortID();
		org.bukkit.scoreboard.Team team = board.getTeam(lowestSortID + "");
		if (team == null)
		{
			board.registerNewTeam(lowestSortID + "");
			team = board.getTeam(lowestSortID + "");
		}
		team.setPrefix(user.getPrefix());
		Player player = Bukkit.getPlayer(user.getUuid());
		team.addPlayer(player);
		player.setScoreboard(board);
	}
	
	public static void sendLobbyScoreBoard(Player player, String mapName, String displayName, TeamManager teamManager, User user)
	{
		net.darkblocks.dark.spigot.utils.ScoreBoardUtils.sendLobbyScoreBoard(player, mapName, displayName);
		if (teamManager == null || teamManager.getTeam(player) == null)
		{
			sendTab(user);
		}
	}
}
