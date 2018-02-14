package net.darkblock.lobby.extras;

import lombok.Getter;
import net.darkblock.lobby.extras.cookieclicker.CookieClicker;
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
	
	public Extras(JavaPlugin javaPlugin, MySQL mySQL, CoinsAPI coinsAPI)
	{
		this.cookieClicker = new CookieClicker(javaPlugin, mySQL, coinsAPI);
	}
	
	public void disable(MySQL mySQL)
	{
		this.cookieClicker.disable(mySQL);
	}
}
