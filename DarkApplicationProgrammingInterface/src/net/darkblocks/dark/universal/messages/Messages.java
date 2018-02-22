/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.universal.messages;

import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LartyHD on 14.11.2017  17:44.
 */
@Getter
public class Messages
{
	private static Messages instance;
	private final Map<String, String> messages;
	
	public Messages()
	{
		instance = this;
		this.messages = new HashMap<>();
	}
	
	public Messages(@NonNull Map<String, String> messages)
	{
		instance = this;
		this.messages = new HashMap<>();
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
			return lowerCaseKey + " wurde nicht gefunden";
		}
		else
		{
			return getMessages().get(lowerCaseKey);
		}
	}
	
	private String getMessage(@NonNull String... keys)
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
		return "Keiner der Nachichten " + resultKeys.substring(2) + " wurden gefunden";
	}
	
	private void add(@NonNull Map<String, String> messages)
	{
		for (String msg : messages.keySet())
		{
			getMessages().put(msg.toLowerCase(), changeColors(messages.get(msg)));
		}
	}
	
	public void add(@NonNull String key, String messages)
	{
		getMessages().put(key.toLowerCase(), changeColors(messages));
	}
	
	private String changeColors(String text)
	{
		return text.replaceAll("&", "§");
	}
	
	private String getPath(Class clazz)
	{
		return clazz.getName().toLowerCase().replaceAll("net.darkblocks.", "dark.") + ".";
	}
	
	public String getShortMessage(Class clazz, String name)
	{
		return getMessage(getPath(clazz) + name, "dark." + getPath(clazz).split(".")[1] + "." + name, "dark." + name);
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
