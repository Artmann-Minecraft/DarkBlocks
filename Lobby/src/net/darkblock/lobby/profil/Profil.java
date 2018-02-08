package net.darkblock.lobby.profil;

import net.darkblock.lobby.profil.listener.ProfilListener;
import net.darkblocks.dark.spigot.events.cashed.CashedEventsManager;
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
