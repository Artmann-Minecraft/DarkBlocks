/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.dark.spigot.vote;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by LartyHD on 04.01.2018  15:20.
 */
@Getter
@Setter
class Vote
{
	private String name;
	private Set<String> voter;
	
	Vote(String name)
	{
		this.name = name;
		this.voter = new HashSet<>();
	}
}
