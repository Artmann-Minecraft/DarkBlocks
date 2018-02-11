package net.darkblocks.core.universal.permissions.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.darkblocks.dark.universal.messages.ChatColor;

import java.util.Set;

/**
 * Created by LartyHD on 07.02.2018  21:55.
 */
@Setter
@Getter
@AllArgsConstructor
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
}
