package net.darkblocks.cores.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.darkblocks.dark.spigot.team.GameTeam;
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
}
