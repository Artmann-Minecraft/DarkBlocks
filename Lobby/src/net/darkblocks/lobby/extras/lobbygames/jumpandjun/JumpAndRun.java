/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.lobby.extras.lobbygames.jumpandjun;

import net.darkblocks.lobby.extras.lobbygames.jumpandjun.listener.JumpAndRunListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 17.02.2018  04:47.
 */
public class JumpAndRun
{
	public JumpAndRun(JavaPlugin javaPlugin)
	{
		new JumpAndRunListener(javaPlugin);
	}
}
