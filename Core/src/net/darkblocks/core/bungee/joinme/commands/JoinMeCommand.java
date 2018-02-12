package net.darkblocks.core.bungee.joinme.commands;

import lombok.Getter;
import net.darkblocks.core.bungee.joinme.utils.SkullImage;
import net.darkblocks.core.universal.permissions.manager.UserManager;
import net.darkblocks.core.universal.permissions.utils.User;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 15.01.2018  01:12.
 */
@Getter
public class JoinMeCommand extends Command
{
	private final UserManager userManager;
	
	public JoinMeCommand(Plugin plugin, UserManager userManager)
	{
		super(CommandUtils.getName(JoinMeCommand.class), CommandUtils.getPermission(JoinMeCommand.class), "join");
		this.userManager = userManager;
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
			if (args.length == 0)
			{
				try
				{
					URL playerHead = new URL("https://minotar.net/avatar/" + sender.getName() + "/128");
					for (ProxiedPlayer all : BungeeCord.getInstance().getPlayers())
					{
						try
						{
							all.sendMessage(new TextComponent(""));
							for (User user : this.userManager.getUser())
							{
								if (user.getUuid() == ((ProxiedPlayer) sender).getUniqueId())
								{
									SkullImage.imgMessage(all, ImageIO.read(playerHead), 8, SkullImage.ImgChar.BLOCK.getChar(), false, (ProxiedPlayer) sender, TEXT + "Hier klicken zum Joinen", "", "", user.getPrefix() + sender.getName() + user.getSuffix(), TEXT + "Spielt auf " + IMPORTANT + ((ProxiedPlayer) sender).getServer().getInfo().getName(), PRIMARY + "Klicke um den Server zu betreten", "", "", "");
								}
							}
							all.sendMessage(new TextComponent(""));
						} catch (IOException ex)
						{
							ex.printStackTrace();
						}
					}
				} catch (MalformedURLException ex)
				{
					sender.sendMessage(new TextComponent(Messages.getInstance().getShortTextComponent(getClass(), "prefix", "Es ist ein Fehler aufgetreten")));
					sender.sendMessage(new TextComponent(Messages.getInstance().getShortTextComponent(getClass(), "prefix", "Bitte versuche es erneut")));
				}
			}
			else
			{
				sender.sendMessage(new TextComponent(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName() + TEXT + "")));
			}
		}
	}
}
