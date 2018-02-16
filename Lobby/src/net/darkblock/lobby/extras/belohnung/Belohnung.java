package net.darkblock.lobby.extras.belohnung;

import lombok.Getter;
import net.darkblock.lobby.extras.belohnung.listener.BelohnungListener;
import net.darkblock.lobby.extras.belohnung.listener.ChestOpeningListener;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.mysql.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 16.02.2018  13:43.
 */
@Getter
public class Belohnung
{
	private final ChestOpeningListener chestOpeningListener;
	private final UserManager userManager;
	private final JavaPlugin javaPlugin;
	private final MySQL mySQL;
	private final CoinsAPI coinsAPI;
	
	public Belohnung(JavaPlugin javaPlugin, UserManager userManager, MySQL mySQL, CoinsAPI coinsAPI)
	{
		this.javaPlugin = javaPlugin;
		this.chestOpeningListener = new ChestOpeningListener(javaPlugin, this);
		this.userManager = userManager;
		this.mySQL = mySQL;
		this.coinsAPI = coinsAPI;
		new BelohnungListener(javaPlugin, this);
	}
	
	public void disable(MySQL mySQL)
	{
		this.chestOpeningListener.disable(mySQL);
	}
}
