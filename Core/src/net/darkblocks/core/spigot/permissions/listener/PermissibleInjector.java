/*
 * © Copyright - MineWar.net | Lars Artmann aka. LartyHD 2017
 */
package net.darkblocks.core.spigot.permissions.listener;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;

import java.lang.reflect.Field;
import java.util.List;

@SuppressWarnings({"ALL", "unused"})
public class PermissibleInjector
{
	protected final String clazzName;
	protected final String fieldName;
	protected final boolean copyValues;
	
	public PermissibleInjector(String clazzName, String fieldName, boolean copyValues)
	{
		this.clazzName = clazzName;
		this.fieldName = fieldName;
		this.copyValues = copyValues;
	}
	
	public Permissible inject(Player player, Permissible permissible) throws NoSuchFieldException, IllegalAccessException
	{
		Field permField = getPermissibleField(player);
		if (permField == null)
		{
			return null;
		}
		Permissible old = (Permissible) permField.get(player);
		if (this.copyValues && permissible instanceof PermissibleBase)
		{
			PermissibleBase newBase = (PermissibleBase) permissible;
			PermissibleBase oldBase = (PermissibleBase) old;
			copyValues(oldBase, newBase);
		}
		permField.set(player, permissible);
		return old;
	}
	
	private void copyValues(PermissibleBase old, PermissibleBase newPerm) throws NoSuchFieldException, IllegalAccessException
	{
		Field attachmentField = PermissibleBase.class.getDeclaredField("attachments");
		attachmentField.setAccessible(true);
		List<Object> attachmentPerms = (List) attachmentField.get(newPerm);
		attachmentPerms.clear();
		attachmentPerms.addAll((List) attachmentField.get(old));
		newPerm.recalculatePermissions();
	}
	
	private Field getPermissibleField(Player player) throws NoSuchFieldException
	{
		Class humanEntity;
		try
		{
			humanEntity = Class.forName(this.clazzName);
		} catch (ClassNotFoundException var4)
		{
			System.out.println("Unknown server implementation being used!");
			return null;
		}
		if (!humanEntity.isAssignableFrom(player.getClass()))
		{
			System.out.println("Strange error while injecting permissible!");
			return null;
		}
		else
		{
			Field permField = humanEntity.getDeclaredField(this.fieldName);
			permField.setAccessible(true);
			return permField;
		}
	}
}
