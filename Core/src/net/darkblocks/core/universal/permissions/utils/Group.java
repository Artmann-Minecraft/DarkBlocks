package net.darkblocks.core.universal.permissions.utils;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import net.darkblocks.dark.universal.messages.ChatColor;

import java.util.Set;

/**
 * Created by LartyHD on 07.02.2018  21:55.
 */
@Setter
@Getter
@ToString
public class Group
{
	private Set<String> permissions;
	private Set<Integer> inherit;
	private String name;
	private String prefix;
	private String suffix;
	private ChatColor color;
	private final int saveID;
	private int sortID;
	
	public Group(@NonNull Set<String> permissions, @NonNull Set<Integer> inherit, @NonNull String name, String prefix, String suffix, @NonNull ChatColor color, int saveID, int sortID)
	{
		this.permissions = permissions;
		this.inherit = inherit;
		this.name = name;
		this.prefix = prefix == null ? "" : prefix;
		this.suffix = suffix == null ? "" : suffix;
		this.color = color;
		this.saveID = saveID;
		this.sortID = sortID;
	}
}
