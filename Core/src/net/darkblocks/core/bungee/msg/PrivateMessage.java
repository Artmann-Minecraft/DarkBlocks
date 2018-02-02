package net.darkblocks.core.bungee.msg;

import lombok.Getter;
import net.darkblocks.core.bungee.msg.commands.MSGCommand;
import net.darkblocks.core.bungee.msg.commands.ReplayCommand;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LartyHD on 14.01.2018  03:36.
 */
@Getter
public class PrivateMessage
{
	private final Map<String, String> replay;
	
	public PrivateMessage(Plugin plugin)
	{
		this.replay = new HashMap<>();
		new MSGCommand(plugin, this);
		new ReplayCommand(plugin, this);
	}
}
