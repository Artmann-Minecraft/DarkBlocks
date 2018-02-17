/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblock.lobby.extras.belohnung;

import lombok.Getter;
import net.darkblock.lobby.extras.belohnung.listener.BelohnungListener;
import net.darkblock.lobby.extras.belohnung.listener.CaseOpeningListener;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static net.darkblocks.dark.universal.messages.Colors.SECONDARY;

/**
 * Created by LartyHD on 16.02.2018  13:43.
 */
@Getter
public class Belohnung
{
	private final CaseOpeningListener caseOpeningListener;
	private final UserManager userManager;
	private final JavaPlugin javaPlugin;
	private final MySQL mySQL;
	private final CoinsAPI coinsAPI;
	
	public Belohnung(JavaPlugin javaPlugin, UserManager userManager, MySQL mySQL, CoinsAPI coinsAPI)
	{
		this.javaPlugin = javaPlugin;
		this.userManager = userManager;
		this.mySQL = mySQL;
		this.coinsAPI = coinsAPI;
		this.caseOpeningListener = new CaseOpeningListener(javaPlugin, this);
		new BelohnungListener(javaPlugin, this);
		Configuration configuration = Configuration.loadConfiguration(new File(javaPlugin.getDataFolder(), "extras.yml"));
		Location location = new Location(Bukkit.getWorld(configuration.getString("Extras.Belohnung.World")), configuration.getDouble("Extras.Belohnung.X"), configuration.getDouble("Extras.Belohnung.Y"), configuration.getDouble("Extras.Belohnung.Z"), (float) configuration.getDouble("Extras.Belohnung.Yaw"), (float) configuration.getDouble("Extras.Belohnung.Pitch"));
		ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
		armorStand.setGravity(false);
		armorStand.setVisible(true);
		armorStand.setSmall(true);
		armorStand.setBasePlate(false);
		armorStand.setCustomName(SECONDARY + "Belohnung");
		armorStand.setCustomNameVisible(true);
		armorStand.setHelmet(new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setOwnerFromURL("http://textures.minecraft.net/texture/6f68d509b5d1669b971dd1d4df2e47e19bcb1b33bf1a7ff1dda29bfc6f9ebf", "MHF_Chest").build());
		ItemStack itemStack = new ItemStack(Material.IRON_BOOTS);
		armorStand.setChestplate(itemStack);
		armorStand.setLeggings(itemStack);
		armorStand.setBoots(itemStack);
	}
	
	public void disable(MySQL mySQL)
	{
		this.caseOpeningListener.disable(mySQL);
	}
}
