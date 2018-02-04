package net.darkblocks.dark.universal.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum Colors
{
	/**
	 * Wird bei SEHR wichtigen Sachen verwendet
	 */
	PRIMARY(org.bukkit.ChatColor.DARK_RED)
			{
				public net.md_5.bungee.api.ChatColor asBungee()
				{
					return PRIMARY.getChatColor().asBungee();
				}
			},
	/**
	 * Wird bei Inventar-Namen, Item-Namen und Namen für Entitys verwendet
	 */
	SECONDARY(org.bukkit.ChatColor.RED)
			{
				public net.md_5.bungee.api.ChatColor asBungee()
				{
					return SECONDARY.getChatColor().asBungee();
				}
			},
	/**
	 * Wird bei wichtigen Sachen (vor allem in Texten) verwendet
	 */
	IMPORTANT(org.bukkit.ChatColor.DARK_GRAY)
			{
				public net.md_5.bungee.api.ChatColor asBungee()
				{
					return IMPORTANT.getChatColor().asBungee();
				}
			},
	/**
	 * Wird bei Texten verwendet
	 */
	TEXT(org.bukkit.ChatColor.GRAY)
			{
				public net.md_5.bungee.api.ChatColor asBungee()
				{
					return TEXT.getChatColor().asBungee();
				}
			},
	/**
	 * Für Prefixe
	 */
	EXTRA(org.bukkit.ChatColor.BOLD)
			{
				public net.md_5.bungee.api.ChatColor asBungee()
				{
					return EXTRA.getChatColor().asBungee();
				}
			},
	/**
	 * Für Designs
	 */
	DESIGN(org.bukkit.ChatColor.STRIKETHROUGH)
			{
				public net.md_5.bungee.api.ChatColor asBungee()
				{
					return DESIGN.getChatColor().asBungee();
				}
			};
	@NonNull
	@Setter
	private org.bukkit.ChatColor chatColor;
	
	public net.md_5.bungee.api.ChatColor asBungee()
	{
		return org.bukkit.ChatColor.RESET.asBungee();
	}
	
	@Override
	public String toString()
	{
		return "§" + getChatColor().getChar();
	}
}
