package net.darkblock.lobby.extras.cookieclicker;

import lombok.Getter;
import net.darkblock.lobby.extras.cookieclicker.commands.CookiesCommand;
import net.darkblock.lobby.extras.cookieclicker.listener.CookieListener;
import net.darkblocks.dark.java.mysql.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Created by LartyHD on 13.02.2018  00:23.
 */
@Getter
public class CookieClicker
{
	private final Map<UUID, Double> cookies;
	private final Map<UUID, Double> cookiesPerClick;
	private final Set<UUID> blockedClicks;
	
	public CookieClicker(JavaPlugin javaPlugin, MySQL mySQL)
	{
		this.cookies = new HashMap<>();
		this.cookiesPerClick = new HashMap<>();
		this.blockedClicks = new HashSet<>();
		mySQL.update("CREATE TABLE IF NOT EXISTS Cookies(uuid varchar(19), coins DOUBLE, `name` varchar(16), primary key(uuid))", () -> {
			new CookiesCommand(javaPlugin, this);
			new CookieListener(javaPlugin, this);
		});
	}
	
	public void disable(MySQL mySQL)
	{
		for (UUID uuid : getCookies().keySet())
		{
			mySQL.updateSync("UPDATE Cookies SET coins='" + getCookies().get(uuid) + "' WHERE uuid='" + uuid.toString() + "'");
		}
		for (UUID uuid : getCookiesPerClick().keySet())
		{
			mySQL.updateSync("UPDATE CookiesPerClick SET coins='" + getCookiesPerClick().get(uuid) + "' WHERE uuid='" + uuid + "'");
		}
	}
}
