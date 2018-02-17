/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.core.bungee.onlinetime.commands;

import lombok.Getter;
import net.darkblocks.core.bungee.onlinetime.OnlineTime;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.SQLException;
import java.util.UUID;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 02.10.2017  22:16.
 */
@Getter
public class OnlineTimeCommand extends Command
{
	private final OnlineTime onlineTime;
	
	public OnlineTimeCommand(Plugin plugin, OnlineTime onlineTime)
	{
		super(CommandUtils.getName(OnlineTimeCommand.class), null, "ot");
		this.onlineTime = onlineTime;
		CommandUtils.register(plugin, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (!(sender instanceof ProxiedPlayer))
		{
			sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "onlyforplayers"));
		}
		else
		{
			switch (args.length)
			{
				case 0:
					ProxiedPlayer player = (ProxiedPlayer) sender;
					UUID uuid = player.getUniqueId();
					getOnlineTime().updateTime(uuid, player.getName(), player.getAddress().getHostString(), () -> getOnlineTime().getMySQL().query("SELECT `time` FROM OnlineTime WHERE `uuid` = '" + uuid + "'", result -> {
						try
						{
							if (result.next())
							{
								sender.sendMessage(new TextComponent(TEXT + "Du hast schon " + getZeit(result.getLong(1)) + TEXT + " auf " + Messages.getInstance().getShortMessage(getClass(), "servername") + " " + TEXT + "verbracht"));
							}
						} catch (SQLException ex)
						{
							ex.printStackTrace();
						}
					}));
					break;
				case 1:
					ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
					if (target == null)
					{
						sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "notonline"));
					}
					else
					{
						getOnlineTime().updateTime(target.getUniqueId(), target.getName(), target.getAddress().getHostString(), () -> getOnlineTime().getMySQL().query("SELECT `time` FROM OnlineTime WHERE `uuid` = '" + target.getUniqueId() + "'", result -> {
							try
							{
								if (result.next())
								{
									sender.sendMessage(new TextComponent(IMPORTANT + target.getName() + TEXT + " hat schon " + getZeit(result.getLong(1)) + TEXT + " auf " + Messages.getInstance().getShortMessage(getClass(), "servername") + " " + TEXT + "verbracht"));
								}
							} catch (SQLException ex)
							{
								ex.printStackTrace();
							}
						}));
					}
					break;
				default:
					sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName() + TEXT + " [Spieler]"));
					break;
			}
		}
	}
	
	private String getZeit(long time)
	{
		String remainingTime = "";
		long minutes = 0;
		long hours = 0;
		while (time >= 60)
		{
			time -= 60;
			minutes++;
		}
		while (minutes >= 60)
		{
			minutes -= 60;
			hours++;
		}
		if (hours == 1)
		{
			remainingTime = remainingTime + "" + IMPORTANT + "eine" + TEXT + " Stunde ";
		}
		else if (hours != 0)
		{
			remainingTime = remainingTime + "" + IMPORTANT + hours + TEXT + " Stunden ";
		}
		if (minutes == 1)
		{
			remainingTime = remainingTime + "" + IMPORTANT + "eine" + TEXT + " Minute ";
		}
		else if (minutes != 0)
		{
			remainingTime = remainingTime + "" + IMPORTANT + minutes + TEXT + " Minuten ";
		}
		if (time == 1)
		{
			remainingTime = remainingTime + "" + IMPORTANT + time + TEXT + " Sekunde ";
		}
		else if (time != 0)
		{
			remainingTime = remainingTime + "" + IMPORTANT + time + TEXT + " Sekunden ";
		}
		if (remainingTime.equalsIgnoreCase(""))
		{
			return IMPORTANT + "0 " + TEXT + "Sekunden";
		}
		return remainingTime.substring(0, remainingTime.length() - 1);
	}
}
