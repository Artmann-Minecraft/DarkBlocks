/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.dark.spigot.utils;

import lombok.Getter;
import lombok.NonNull;
import net.darkblocks.dark.universal.messages.Colors;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LartyHD on 16.01.2018  22:17.
 */
public class ScoreBoardUtils
{
	@Getter
	private static final Scoreboard scoreboard;
	@Getter
	private static final ScoreboardObjective objective;
	
	static
	{
		scoreboard = new Scoreboard();
		objective = scoreboard.registerObjective("object", IScoreboardCriteria.b);
	}
	
	public static void sendLobbyScoreBoard(@NonNull Player player, String mapName, String displayName)
	{
		List<ScoreboardScore> score = new ArrayList<>();
		score.add(setScoreboardScore(" ", 4));
		score.add(setScoreboardScore(Colors.TEXT + "Map" + Colors.IMPORTANT + ":", 3));
		score.add(setScoreboardScore(Colors.PRIMARY + mapName, 2));
		score.add(setScoreboardScore("  ", 1));
		sendScoreBoard(player, displayName, score);
	}
	
	protected static void sendScoreBoard(@NonNull Player player, String displayName, List<ScoreboardScore> scoreboardScores)
	{
		if (displayName.length() >= 32)
		{
			objective.setDisplayName(displayName.substring(0, 31));
		}
		else
		{
			objective.setDisplayName(displayName);
		}
		/*
		 * Remove Packet
		 */
		sendPacket(player, new PacketPlayOutScoreboardObjective(objective, 1));
		/*
		 * Create Packet
		 */
		sendPacket(player, new PacketPlayOutScoreboardObjective(objective, 0));
		/*
		 * Display Packet
		 */
		sendPacket(player, new PacketPlayOutScoreboardDisplayObjective(1, objective));
		for (ScoreboardScore score : scoreboardScores)
		{
			/*
			 * Score Packet
			 */
			sendPacket(player, new PacketPlayOutScoreboardScore(score));
		}
	}
	
	protected static ScoreboardScore setScoreboardScore(String name, int score)
	{
		ScoreboardScore scoreboardScore = new ScoreboardScore(getScoreboard(), getObjective(), name);
		scoreboardScore.setScore(score);
		return scoreboardScore;
	}
	
	private static void sendPacket(@NonNull Player player, Packet packet)
	{
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
}
