package net.darkblock.lobby.extras.cookieclicker.commands;

import lombok.Getter;
import net.darkblock.lobby.extras.cookieclicker.CookieClicker;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 13.02.2018  00:28.
 */
@Getter
public class CookiesCommand implements CommandExecutor
{
	private final CookieClicker cookieClicker;
	
	public CookiesCommand(JavaPlugin javaPlugin, CookieClicker cookieClicker)
	{
		javaPlugin.getCommand(CommandUtils.getName(getClass())).setExecutor(this);
		this.cookieClicker = cookieClicker;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (args == null || args.length == 0)
		{
			if (!(sender instanceof Player))
			{
				sender.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + IMPORTANT + "/" + command.getName() + TEXT + " <Spieler>");
			}
			else
			{
				sender.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du hast " + PRIMARY + getCookieClicker().getCookies().get(((Player) sender).getUniqueId()) + IMPORTANT + " Cookies");
			}
		}
		else if (args.length == 1)
		{
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null)
			{
				sender.sendMessage(Messages.getInstance().getShortMessage(getClass(), "notonline"));
			}
			else
			{
				sender.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + target.getName() + " hat " + IMPORTANT + getCookieClicker().getCookies().get(target.getUniqueId()) + " Cookies");
			}
		}
		else
		{
			sender.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + IMPORTANT + "/" + command.getName() + TEXT + " [Spieler]");
		}
		return true;
	}
}
