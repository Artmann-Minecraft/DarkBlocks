/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.lobby.profil;

import net.darkblocks.dark.spigot.events.cashed.CashedEventsManager;
import net.darkblocks.lobby.profil.listener.ProfilListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

/**
 * Created by LartyHD on 08.02.2018  10:11.
 */
public class Profil
{
	public Profil(JavaPlugin javaPlugin, CashedEventsManager cashedEventsManager, Map<String, Boolean> navigatorAnimation)
	{
		new ProfilListener(javaPlugin, cashedEventsManager, navigatorAnimation);
	}
}
