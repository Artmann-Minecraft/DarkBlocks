/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.dark.spigot.listener;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LartyHD on 19.02.2018  02:35.
 */
@Getter
@Setter
public abstract class ItemListener extends Listener
{
	private ItemStack itemStack;
	
	@SuppressWarnings("WeakerAccess")
	public ItemListener(@NonNull JavaPlugin javaPlugin)
	{
		super(javaPlugin);
	}
	
	public ItemListener(@NonNull JavaPlugin javaPlugin, @NonNull ItemStack itemStack)
	{
		this(javaPlugin);
		this.itemStack = itemStack;
	}
	
	public ItemListener(@NonNull JavaPlugin javaPlugin, @NonNull ItemBuilder itemBuilder)
	{
		this(javaPlugin);
		this.itemStack = itemBuilder.build();
	}
	
	public ItemListener(@NonNull JavaPlugin javaPlugin, Material material)
	{
		this(javaPlugin);
		this.itemStack = new ItemStack(material);
	}
}
