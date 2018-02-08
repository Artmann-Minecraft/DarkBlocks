package net.darkblocks.core.bungee.permissions.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.darkblocks.dark.universal.messages.ChatColor;

import java.util.Set;

/**
 * Created by LartyHD on 07.02.2018  21:55.
 */
@Getter
@Setter
@AllArgsConstructor
public class Group
{
	private final int saveID;
	private Set<String> permissions;
	private Set<Integer> inherit;
	private String name;
	private String prefix;
	private String suffix;
	private ChatColor color;
	private int sortID;
}
