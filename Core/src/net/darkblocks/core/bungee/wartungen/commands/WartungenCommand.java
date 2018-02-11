package net.darkblocks.core.bungee.wartungen.commands;

import lombok.Getter;
import lombok.Setter;
import net.darkblocks.core.bungee.wartungen.Wartungen;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 09.01.2018  09:44.
 */
@Getter
@Setter
public class WartungenCommand extends Command
{
	private final Wartungen wartungen;
	private boolean run;
	
	public WartungenCommand(Wartungen wartungen)
	{
		super(CommandUtils.getName(WartungenCommand.class), CommandUtils.getPermission(WartungenCommand.class), "wartung", "maintenance", "Wartungsarbeiten");
		this.wartungen = wartungen;
		CommandUtils.register(wartungen.getPlugin(), this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		String servername = Messages.getInstance().getShortMessage(getClass(), "servername");
		if (args.length == 0)
		{
			if (getWartungen().isOn())
			{
				BungeeCord.getInstance().broadcast(new TextComponent(servername + TEXT + " ist jetzt nicht mehr im " + IMPORTANT + "Wartungsmodus"));
				getWartungen().setOn(false);
			}
			else if (isRun())
			{
				sender.sendMessage(new TextComponent(TEXT + "Der " + IMPORTANT + "Wartungen-Countdown " + TEXT + "läuft schon"));
			}
			else
			{
				new Thread(() -> {
					setRun(true);
					try
					{
						for (int i = 10; i > -1; i--)
						{
							switch (i)
							{
								case 10:
								case 5:
								case 4:
								case 3:
								case 2:
									BungeeCord.getInstance().broadcast(new TextComponent(servername + TEXT + " wird in " + PRIMARY + i + IMPORTANT + " Sekunden " + TEXT + "in den " + IMPORTANT + "Wartungsmodus " + TEXT + "gesetzt"));
									break;
								case 1:
									BungeeCord.getInstance().broadcast(new TextComponent(servername + TEXT + " wird in " + PRIMARY + "einer" + IMPORTANT + " Sekunde " + TEXT + "in den " + IMPORTANT + "Wartungsmodus " + TEXT + "gesetzt"));
									break;
								case 0:
									BungeeCord.getInstance().broadcast(new TextComponent(servername + TEXT + " ist jetzt im " + IMPORTANT + "Wartungsmodus"));
									getWartungen().setOn(true);
							}
							Thread.sleep(1000);
						}
					} catch (InterruptedException ex)
					{
						ex.printStackTrace();
					} finally
					{
						setRun(false);
					}
				}).start();
			}
			return;
		}
		else if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("now"))
			{
				if (!getWartungen().isOn())
				{
					BungeeCord.getInstance().broadcast(new TextComponent(servername + TEXT + " ist jetzt im " + IMPORTANT + "Wartungsmodus"));
					getWartungen().setOn(true);
				}
				else
				{
					sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Der " + IMPORTANT + "Wartungsmodus " + TEXT + "sind schon aktive"));
				}
				return;
			}
		}
		else if (args.length == 2)
		{
			switch (args[0].toLowerCase())
			{
				case "add":
					getWartungen().getWhitelist().add(args[1].toLowerCase());
					sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + args[1] + TEXT + " wurde zur " + IMPORTANT + "Whitelist " + TEXT + "hinzugefügt"));
					return;
				case "remove":
					getWartungen().getWhitelist().remove(args[1].toLowerCase());
					sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + args[1] + TEXT + " wurde von der " + IMPORTANT + "Whitelist " + TEXT + "entfernt"));
					return;
			}
		}
		sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName() + TEXT + " [now]"));
		sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", IMPORTANT + "/" + getName() + TEXT + " [add/remove] <Spieler>"));
	}
}
