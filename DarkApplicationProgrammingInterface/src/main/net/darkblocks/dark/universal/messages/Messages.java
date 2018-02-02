package net.darkblocks.dark.universal.messages;

import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.Map;

import static net.craftplugin.craftpluginapi.universal.messages.Colors.IMPORTANT;
import static net.craftplugin.craftpluginapi.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 14.11.2017  17:44.
 */
@Getter
public class Messages
{
	private static Messages instance;
	private final Map<String, String> messages;
	
	private Messages()
	{
		instance = this;
		this.messages = new HashMap<>();
	}
	
	private Messages(@NonNull Map<String, String> messages)
	{
		instance = this;
		this.messages = messages;
		add(messages);
	}
	
	public static Messages getInstance()
	{
		return instance;
	}
	
	public String getMessage(@NonNull String key)
	{
		String lowerCaseKey = key.toLowerCase();
		if (getMessages().get(lowerCaseKey) == null)
		{
			return IMPORTANT + lowerCaseKey + TEXT + " wurde nicht gefunden";
		}
		else
		{
			return getMessages().get(lowerCaseKey);
		}
	}
	
	public String getMessage(@NonNull String... keys)
	{
		StringBuilder resultKeys = new StringBuilder();
		for (String key : keys)
		{
			String lowerCaseKey = key.toLowerCase();
			if (getMessages().get(lowerCaseKey) != null)
			{
				return getMessages().get(lowerCaseKey);
			}
			else
			{
				resultKeys.append(", ").append(lowerCaseKey);
			}
		}
		return TEXT + "Keiner der Nachichten " + IMPORTANT + resultKeys.substring(2) + TEXT + " wurden gefunden ";
	}
	
	public void add(@NonNull Map<String, String> messages)
	{
		for (String msg : messages.keySet())
		{
			getMessages().put(msg, changeColors(messages.get(msg)));
		}
	}
	
	public void add(@NonNull String key, String messages)
	{
		getMessages().put(key, changeColors(messages));
	}
	
	private String changeColors(String text)
	{
		return text.toLowerCase().replaceAll("&", "§");
	}
	
	public String getPathPrefix()
	{
		return "dark.";
	}
	
	public String getPath(Class clazz)
	{
		return (clazz.getPackage().getName() + clazz.getName() + ".").toLowerCase().replaceAll("net.darkblocks.", getPathPrefix());
	}
	
	public String getShortMessage(Class clazz, String name)
	{
		return getMessage(getPath(clazz) + name, getPathPrefix() + name);
	}
	
	public TextComponent getShortTextComponent(Class clazz, String name)
	{
		return new TextComponent(getShortMessage(clazz, name));
	}
	
	public TextComponent getShortTextComponent(Class clazz, String name, String message)
	{
		return new TextComponent(getShortMessage(clazz, name) + message);
	}
}
