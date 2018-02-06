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
	PRIMARY(ChatColor.AQUA),
	/**
	 * Wird bei Inventar-Namen, Item-Namen und Namen für Entitys verwendet
	 */
	SECONDARY(ChatColor.BLUE),
	/**
	 * Wird bei wichtigen Sachen (vor allem in Texten) verwendet
	 */
	IMPORTANT(ChatColor.DARK_GRAY),
	/**
	 * Wird bei Texten verwendet
	 */
	TEXT(ChatColor.GRAY),
	/**
	 * Für Prefixe
	 */
	EXTRA(ChatColor.BOLD),
	/**
	 * Für Designs
	 */
	DESIGN(ChatColor.STRIKETHROUGH);
	@NonNull
	@Setter
	private ChatColor chatColor;
	
	@Override
	public String toString()
	{
		return this.chatColor.toString();
	}
}
