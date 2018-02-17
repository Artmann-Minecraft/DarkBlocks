/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblock.lobby.extras;

import lombok.Getter;
import net.darkblock.lobby.extras.belohnung.Belohnung;
import net.darkblock.lobby.extras.cookieclicker.CookieClicker;
import net.darkblock.lobby.extras.lobbygames.LobbyGames;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.mysql.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 13.02.2018  00:22.
 */
@Getter
public class Extras
{
	private final CookieClicker cookieClicker;
	private final Belohnung belohnung;
	
	public Extras(JavaPlugin javaPlugin, UserManager userManager, MySQL mySQL, CoinsAPI coinsAPI)
	{
		this.cookieClicker = new CookieClicker(javaPlugin, mySQL, coinsAPI);
		this.belohnung = new Belohnung(javaPlugin, userManager, mySQL, coinsAPI);
		new LobbyGames(javaPlugin);
	}
	
	public void disable(MySQL mySQL)
	{
		this.cookieClicker.disable(mySQL);
		this.belohnung.disable(mySQL);
	}
}
