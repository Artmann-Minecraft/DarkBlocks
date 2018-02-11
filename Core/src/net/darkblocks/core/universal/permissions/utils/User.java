package net.darkblocks.core.universal.permissions.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

/**
 * Created by LartyHD on 07.02.2018  21:54.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class User
{
	private Set<Group> groups;
	private Set<String> permissions;
	private UUID uuid;
	private String prefix;
	private String suffix;
	private int lowestSortID;
}
