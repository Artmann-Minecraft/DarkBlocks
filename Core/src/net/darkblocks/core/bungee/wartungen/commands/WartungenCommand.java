package net.darkblocks.core.bungee.wartungen.commands;

import lombok.Getter;
import lombok.Setter;
import net.darkblocks.core.bungee.wartungen.Wartungen;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import static net.darkblocks.dark.universal.messages.Colors.IMPORTANT;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

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
		this.run = false;
		CommandUtils.register(wartungen.getPlugin(), this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		String servername = Messages.getInstance().getShortMessage(getClass(), "servername");
		if (getWartungen().isOn())
		{
			BungeeCord.getInstance().broadcast(Messages.getInstance().getShortTextComponent(getClass(), "prefix", servername + TEXT + " ist jetzt nicht mehr im " + IMPORTANT + "Wartungsmodus"));
			getWartungen().setOn(false);
		}
		else if (args.length == 1)
		{
			BungeeCord.getInstance().broadcast(Messages.getInstance().getShortTextComponent(getClass(), "prefix", servername + TEXT + " ist jetzt im " + IMPORTANT + "Wartungsmodus"));
			getWartungen().setOn(true);
		}
		else if (isRun())
		{
			sender.sendMessage(Messages.getInstance().getShortTextComponent(getClass(), "prefix", TEXT + "Der " + IMPORTANT + "Wartungen-Countdown " + TEXT + "läuft schon"));
		}
		else
		{
			new Thread(() -> {
				setRun(true);
				for (int i = 10; i > -1; i--)
				{
					switch (i)
					{
						case 10:
						case 5:
						case 4:
						case 3:
						case 2:
							BungeeCord.getInstance().broadcast(Messages.getInstance().getShortTextComponent(getClass(), "prefix", servername + TEXT + " wird in " + IMPORTANT + i + " Sekunden " + TEXT + "in den " + IMPORTANT + "Wartungsmodus " + TEXT + "gesetzt"));
							break;
						case 1:
							BungeeCord.getInstance().broadcast(Messages.getInstance().getShortTextComponent(getClass(), "prefix", servername + TEXT + " wird in " + IMPORTANT + "einer" + " Sekunde " + TEXT + "in den " + IMPORTANT + "Wartungsmodus " + TEXT + "gesetzt"));
							break;
						case 0:
							BungeeCord.getInstance().broadcast(Messages.getInstance().getShortTextComponent(getClass(), "prefix", servername + TEXT + " ist jetzt im " + IMPORTANT + "Wartungsmodus"));
							getWartungen().setOn(false);
					}
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException ex)
					{
						ex.printStackTrace();
						setRun(false);
					}
				}
				setRun(false);
			}).start();
		}
	}
}
