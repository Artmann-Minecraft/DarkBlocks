package net.darkblocks.core.bungee.wartungen;

import lombok.Getter;
import lombok.Setter;
import net.darkblocks.core.bungee.wartungen.commands.WartungenCommand;
import net.darkblocks.core.bungee.wartungen.listener.WartungenListener;
import net.darkblocks.dark.java.mysql.MySQL;
import net.darkblocks.dark.universal.messages.Messages;
import net.darkblocks.dark.universal.utils.CommandUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static net.darkblocks.dark.universal.messages.Colors.*;

/**
 * Created by LartyHD on 09.01.2018  08:34.
 */
@Getter
@Setter
public class Wartungen
{
	private final Plugin plugin;
	private final MySQL mySQL;
	private Set<String> whitelist;
	private boolean on;
	
	public Wartungen(Plugin plugin, MySQL mySQL)
	{
		this.plugin = plugin;
		this.mySQL = mySQL;
		this.whitelist = new HashSet<>();
		this.on = false;
		getMySQL().update("CREATE TABLE IF NOT EXISTS Wartungen(`active` INT, PRIMARY KEY(active))", () -> getMySQL().query("SELECT `active` FROM Wartungen", result -> {
			try
			{
				if (result.next())
				{
					setOn(result.getInt("active") == 1);
				}
				else
				{
					getMySQL().update("INSERT INTO `active` VALUES ('1')");
					setOn(false);
				}
			} catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		}));
		new WartungenListener(this);
		new WartungenCommand(this);
	}
	
	public void setOn(boolean on)
	{
		this.on = on;
		if (on)
		{
			for (ProxiedPlayer players : BungeeCord.getInstance().getPlayers())
			{
				if (!players.hasPermission(CommandUtils.getPermission(getClass())) && !getWhitelist().contains(players.getName()))
				{
					players.disconnect(new TextComponent(PRIMARY + Messages.getInstance().getShortMessage(getClass(), "servername") + TEXT + " befindet sich im " + IMPORTANT + "Wartungsmodus\n" + TEXT + "Das Betreten des Netztwerkes ist derzeit deswegen nicht möglich"));
				}
			}
		}
	}
}
