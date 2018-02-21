/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.lobby.extras.lobbygames;

import net.darkblocks.lobby.extras.lobbygames.jumpandjun.JumpAndRun;
import net.darkblocks.lobby.extras.lobbygames.tiktaktoo.TikTakToo;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 17.02.2018  04:46.
 */
public class LobbyGames
{
	public LobbyGames(JavaPlugin javaPlugin)
	{
		new JumpAndRun(javaPlugin);
		new TikTakToo(javaPlugin);
	}
}
