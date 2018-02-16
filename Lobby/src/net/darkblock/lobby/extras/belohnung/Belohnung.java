package net.darkblock.lobby.extras.belohnung;

import net.darkblock.lobby.extras.belohnung.listener.BelohnungListener;
import net.darkblock.lobby.extras.belohnung.listener.ChestOpeningListener;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.mysql.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 16.02.2018  13:43.
 */
public class Belohnung
{
	private final ChestOpeningListener chestOpeningListener;
	
	public Belohnung(JavaPlugin javaPlugin, CoinsAPI coinsAPI)
	{
		new BelohnungListener(javaPlugin);
		this.chestOpeningListener = new ChestOpeningListener(javaPlugin, coinsAPI);
	}
	
	public void disable(MySQL mySQL)
	{
		this.chestOpeningListener.disable(mySQL);
	}
}
