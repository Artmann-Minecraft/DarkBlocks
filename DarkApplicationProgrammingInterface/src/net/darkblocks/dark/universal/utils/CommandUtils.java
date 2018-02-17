/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.universal.utils;

/**
 * Created by LartyHD on 22.01.2018  01:51.
 */
public class CommandUtils
{
	public static String getName(Class clazz)
	{
		return clazz.getSimpleName().replaceAll("Command", "");
	}
	
	public static String getPermission(Class clazz)
	{
		return clazz.getName();
	}
	
	public static void register(net.md_5.bungee.api.plugin.Plugin plugin, net.md_5.bungee.api.plugin.Command command)
	{
		net.md_5.bungee.BungeeCord.getInstance().getPluginManager().registerCommand(plugin, command);
	}
	
	public static void register(org.bukkit.plugin.java.JavaPlugin javaPlugin, org.bukkit.command.CommandExecutor commandExecutor)
	{
		javaPlugin.getCommand(getName(commandExecutor.getClass())).setExecutor(commandExecutor);
	}
}
