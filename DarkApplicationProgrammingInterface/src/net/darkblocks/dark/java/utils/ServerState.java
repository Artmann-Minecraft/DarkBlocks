/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.dark.java.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by LartyHD on 29.11.2017  14:20.
 */
@Getter
@AllArgsConstructor
public enum ServerState
{
	OFFLINE(com.segdogames.segdocloudplugin.spigot.utils.ServerState.OFFLINE),
	STARTUP(com.segdogames.segdocloudplugin.spigot.utils.ServerState.STARTUP),
	LOBBY(com.segdogames.segdocloudplugin.spigot.utils.ServerState.LOBBY),
	LOBBYFULL(com.segdogames.segdocloudplugin.spigot.utils.ServerState.LOBBYFULL),
	PREGAME(com.segdogames.segdocloudplugin.spigot.utils.ServerState.PREGAME),
	INGAME(com.segdogames.segdocloudplugin.spigot.utils.ServerState.INGAME),
	ENDGAME(com.segdogames.segdocloudplugin.spigot.utils.ServerState.ENDGAME),
	STOPPING(com.segdogames.segdocloudplugin.spigot.utils.ServerState.STOPPING),
	RESTART(com.segdogames.segdocloudplugin.spigot.utils.ServerState.RESTART),
	SAVETIME(com.segdogames.segdocloudplugin.spigot.utils.ServerState.PREGAME);
	public com.segdogames.segdocloudplugin.spigot.utils.ServerState serverState;
}
