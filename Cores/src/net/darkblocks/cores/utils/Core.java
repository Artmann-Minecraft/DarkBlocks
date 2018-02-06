package net.darkblocks.cores.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.darkblocks.cores.events.CoreAttackedEvent;
import net.darkblocks.dark.spigot.team.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by LartyHD on 04.01.2018  13:58.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Core
{
	private String name;
	private Location location;
	private GameTeam gameTeam;
	private boolean attacked;
	
	public void setAttacked(boolean attacked)
	{
		this.attacked = attacked;
		Bukkit.getPluginManager().callEvent(new CoreAttackedEvent(this));
	}
}
