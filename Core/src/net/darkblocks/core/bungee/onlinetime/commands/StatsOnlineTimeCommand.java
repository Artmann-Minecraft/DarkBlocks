package net.darkblocks.core.bungee.onlinetime.commands;

import lombok.Getter;
import net.darkblocks.core.bungee.onlinetime.OnlineTime;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.SQLException;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 02.10.2017  22:16.
 */
@Getter
public class StatsOnlineTimeCommand extends Command
{
	private final OnlineTime onlineTime;
	
	public StatsOnlineTimeCommand(Plugin plugin, OnlineTime onlineTime)
	{
		super(CommandUtils.getName(StatsOnlineTimeCommand.class), CommandUtils.getPermission(StatsOnlineTimeCommand.class), "sot", "statsot", "sonlinetime");
		this.onlineTime = onlineTime;
		CommandUtils.register(plugin, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (args.length == 0)
		{
			getOnlineTime().getMySQL().query("SELECT `time` FROM OnlineTime", result -> {
				long time = 0;
				long player = 0;
				try
				{
					while (result.next())
					{
						time += result.getLong(1);
						player++;
					}
					sender.sendMessage(new TextComponent(TEXT + "Insgesamt wurden schon " + getZeit(time) + TEXT + " auf " + Messages.getInstance().getShortMessage(getClass(), "servername") + TEXT + " verbracht"));
					sender.sendMessage(new TextComponent(TEXT + "Im durschnitt wurden " + getZeit(time / player) + TEXT + " auf " + Messages.getInstance().getShortMessage(getClass(), "servername") + TEXT + " verbracht"));
				} catch (SQLException ex)
				{
					ex.printStackTrace();
				}
			});
		}
		else
		{
			sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName()));
		}
	}
	
	private String getZeit(long time)
	{
		String remainingTime = "";
		long minutes = 0;
		long hours = 0;
		long days = 0;
		long years = 0;
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
		while (hours >= 24)
		{
			hours -= 24;
			days++;
		}
		while (days >= 365)
		{
			days -= 365;
			years++;
		}
		if (years == 1)
		{
			remainingTime = "" + IMPORTANT + "ein " + TEXT + "Jahre ";
		}
		else if (years != 0)
		{
			remainingTime = "" + IMPORTANT + years + " " + TEXT + "Jahr ";
		}
		if (days == 1)
		{
			remainingTime = "" + IMPORTANT + "ein " + TEXT + "Tag ";
		}
		else if (days != 0)
		{
			remainingTime = "" + IMPORTANT + days + " " + TEXT + "Tage ";
		}
		if (hours == 1)
		{
			remainingTime = remainingTime + "" + IMPORTANT + "eine " + TEXT + "Stunde ";
		}
		else if (hours != 0)
		{
			remainingTime = remainingTime + "" + IMPORTANT + hours + " " + TEXT + "Stunden ";
		}
		if (minutes == 1)
		{
			remainingTime = remainingTime + "" + IMPORTANT + "eine " + TEXT + "Minute ";
		}
		else if (minutes != 0)
		{
			remainingTime = remainingTime + "" + IMPORTANT + minutes + " " + TEXT + "Minuten ";
		}
		if (time == 1)
		{
			remainingTime = remainingTime + "" + IMPORTANT + "eine " + TEXT + "Sekunde ";
		}
		else if (time != 0)
		{
			remainingTime = remainingTime + "" + IMPORTANT + time + " " + TEXT + "Sekunden ";
		}
		if (remainingTime.equalsIgnoreCase(""))
		{
			return IMPORTANT + "0 " + TEXT + "Sekunden";
		}
		return remainingTime.substring(0, remainingTime.length() - 1);
	}
}
