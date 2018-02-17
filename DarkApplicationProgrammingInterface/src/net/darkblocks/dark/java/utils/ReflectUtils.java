/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.dark.java.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectUtils
{
	public static void setValue(Object object, String name, Object value)
	{
		try
		{
			Field field = object.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(object, value);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("SameParameterValue")
	protected static Object getValue(Object object, String name)
	{
		try
		{
			Field field = object.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(object);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("ConstantConditions")
	public static Field getField(Class<?> clazz, String name)
	{
		try
		{
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			if (Modifier.isFinal(field.getModifiers()))
			{
				getField(Field.class, "modifiers").set(field, field.getModifiers() & ~Modifier.FINAL);
			}
			return field;
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
}