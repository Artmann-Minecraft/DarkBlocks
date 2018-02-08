package net.darkblocks.core.bungee.permissions.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.connection.PendingConnection;

import java.util.Set;

/**
 * Created by LartyHD on 07.02.2018  21:54.
 */
@Getter
@Setter
@AllArgsConstructor
public class User
{
	private Set<Group> groups;
	private Set<String> permissions;
	private PendingConnection connection;
	private String prefix;
	private String suffix;
	private int lowestSortID;
}
