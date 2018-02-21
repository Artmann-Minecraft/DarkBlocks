/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.lobby.extras.lobbygames.tiktaktoo;

import net.darkblocks.lobby.extras.lobbygames.tiktaktoo.listener.TikTakTooListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 17.02.2018  04:47.
 */
public class TikTakToo
{
	public TikTakToo(JavaPlugin javaPlugin)
	{
		new TikTakTooListener(javaPlugin);
	}
}
