/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.gungame.kits.utils;

import lombok.Getter;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import static net.darkblocks.dark.universal.messages.Colors.SECONDARY;

/**
 * Created by LartyHD on 20.08.2017  18:19.
 * Project: GunGame [Red]
 */
@Getter
public enum Kits
{
	/*BASIC*/
	KIT1(null, null, null, null, getWaffe(Material.WOOD_AXE)),
	KIT2(null, null, null, null, getWaffe(Material.WOOD_SWORD)),
	/*LEATHER*/
	KIT3(null, getArmor(Material.LEATHER_CHESTPLATE), null, null, getWaffe(Material.WOOD_SWORD)),
	KIT4(null, getArmor(Material.LEATHER_CHESTPLATE), null, getArmor(Material.LEATHER_BOOTS), getWaffe(Material.WOOD_SWORD)),
	KIT5(getArmor(Material.LEATHER_HELMET), getArmor(Material.LEATHER_CHESTPLATE), null, getArmor(Material.LEATHER_BOOTS), getWaffe(Material.WOOD_SWORD)),
	KIT6(getArmor(Material.LEATHER_HELMET), getArmor(Material.LEATHER_CHESTPLATE), getArmor(Material.LEATHER_LEGGINGS), getArmor(Material.LEATHER_BOOTS), getWaffe(Material.WOOD_SWORD)),
	KIT7(getArmor(Material.LEATHER_HELMET), getArmor(Material.LEATHER_CHESTPLATE), getArmor(Material.LEATHER_LEGGINGS), getArmor(Material.LEATHER_BOOTS), getWaffe(Material.WOOD_AXE, 1)),
	KIT8(getArmor(Material.LEATHER_HELMET), getArmor(Material.LEATHER_CHESTPLATE, 1), getArmor(Material.LEATHER_LEGGINGS), getArmor(Material.LEATHER_BOOTS), getWaffe(Material.WOOD_AXE, 1)),
	KIT9(getArmor(Material.LEATHER_HELMET), getArmor(Material.LEATHER_CHESTPLATE, 1), getArmor(Material.LEATHER_LEGGINGS), getArmor(Material.LEATHER_BOOTS, 1), getWaffe(Material.WOOD_AXE, 1)),
	KIT10(getArmor(Material.LEATHER_HELMET, 1), getArmor(Material.LEATHER_CHESTPLATE, 1), getArmor(Material.LEATHER_LEGGINGS), getArmor(Material.LEATHER_BOOTS, 1), getWaffe(Material.WOOD_AXE, 1)),
	KIT11(getArmor(Material.LEATHER_HELMET, 1), getArmor(Material.LEATHER_CHESTPLATE, 1), getArmor(Material.LEATHER_LEGGINGS, 1), getArmor(Material.LEATHER_BOOTS, 1), getWaffe(Material.WOOD_AXE, 1)),
	KIT12(getArmor(Material.LEATHER_HELMET, 1), getArmor(Material.LEATHER_CHESTPLATE, 1), getArmor(Material.LEATHER_LEGGINGS, 1), getArmor(Material.LEATHER_BOOTS, 1), getWaffe(Material.WOOD_SWORD, 1)),
	/*CHAINMAIL*/
	KIT13(getArmor(Material.LEATHER_HELMET, 1), getArmor(Material.CHAINMAIL_CHESTPLATE), getArmor(Material.LEATHER_LEGGINGS, 1), getArmor(Material.LEATHER_BOOTS, 1), getWaffe(Material.STONE_SWORD)),
	KIT14(getArmor(Material.LEATHER_HELMET, 1), getArmor(Material.CHAINMAIL_CHESTPLATE), getArmor(Material.LEATHER_LEGGINGS, 1), getArmor(Material.CHAINMAIL_BOOTS), getWaffe(Material.STONE_SWORD)),
	KIT15(getArmor(Material.CHAINMAIL_HELMET), getArmor(Material.CHAINMAIL_CHESTPLATE), getArmor(Material.CHAINMAIL_LEGGINGS), getArmor(Material.CHAINMAIL_BOOTS), getWaffe(Material.STONE_SWORD)),
	KIT16(getArmor(Material.CHAINMAIL_HELMET), getArmor(Material.CHAINMAIL_CHESTPLATE), getArmor(Material.CHAINMAIL_LEGGINGS), getArmor(Material.CHAINMAIL_BOOTS), getWaffe(Material.STONE_SWORD)),
	KIT17(getArmor(Material.CHAINMAIL_HELMET), getArmor(Material.CHAINMAIL_CHESTPLATE), getArmor(Material.CHAINMAIL_LEGGINGS), getArmor(Material.CHAINMAIL_BOOTS), getWaffe(Material.STONE_AXE, 1)),
	KIT18(getArmor(Material.CHAINMAIL_HELMET), getArmor(Material.CHAINMAIL_CHESTPLATE, 1), getArmor(Material.CHAINMAIL_LEGGINGS), getArmor(Material.CHAINMAIL_BOOTS), getWaffe(Material.STONE_AXE, 1)),
	KIT19(getArmor(Material.CHAINMAIL_HELMET), getArmor(Material.CHAINMAIL_CHESTPLATE, 1), getArmor(Material.CHAINMAIL_LEGGINGS), getArmor(Material.CHAINMAIL_BOOTS, 1), getWaffe(Material.STONE_AXE, 1)),
	KIT20(getArmor(Material.CHAINMAIL_HELMET, 1), getArmor(Material.CHAINMAIL_CHESTPLATE, 1), getArmor(Material.CHAINMAIL_LEGGINGS), getArmor(Material.CHAINMAIL_BOOTS, 1), getWaffe(Material.STONE_AXE, 1)),
	KIT21(getArmor(Material.CHAINMAIL_HELMET, 1), getArmor(Material.CHAINMAIL_CHESTPLATE, 1), getArmor(Material.CHAINMAIL_LEGGINGS, 1), getArmor(Material.CHAINMAIL_BOOTS, 1), getWaffe(Material.STONE_AXE, 1)),
	KIT22(getArmor(Material.CHAINMAIL_HELMET, 1), getArmor(Material.CHAINMAIL_CHESTPLATE, 1), getArmor(Material.CHAINMAIL_LEGGINGS, 1), getArmor(Material.CHAINMAIL_BOOTS, 1), getWaffe(Material.STONE_SWORD, 1)),
	/*IRON*/
	KIT23(getArmor(Material.CHAINMAIL_HELMET, 1), getArmor(Material.IRON_CHESTPLATE), getArmor(Material.CHAINMAIL_LEGGINGS, 1), getArmor(Material.CHAINMAIL_BOOTS, 1), getWaffe(Material.IRON_SWORD)),
	KIT24(getArmor(Material.CHAINMAIL_HELMET, 1), getArmor(Material.IRON_CHESTPLATE), getArmor(Material.CHAINMAIL_LEGGINGS, 1), getArmor(Material.IRON_BOOTS), getWaffe(Material.IRON_SWORD)),
	KIT25(getArmor(Material.IRON_HELMET), getArmor(Material.IRON_CHESTPLATE), getArmor(Material.IRON_LEGGINGS), getArmor(Material.IRON_BOOTS), getWaffe(Material.IRON_SWORD)),
	KIT26(getArmor(Material.IRON_HELMET), getArmor(Material.IRON_CHESTPLATE), getArmor(Material.IRON_LEGGINGS), getArmor(Material.IRON_BOOTS), getWaffe(Material.IRON_SWORD)),
	KIT27(getArmor(Material.IRON_HELMET), getArmor(Material.IRON_CHESTPLATE), getArmor(Material.IRON_LEGGINGS), getArmor(Material.IRON_BOOTS), getWaffe(Material.IRON_AXE, 1)),
	KIT28(getArmor(Material.IRON_HELMET), getArmor(Material.IRON_CHESTPLATE, 1), getArmor(Material.IRON_LEGGINGS), getArmor(Material.IRON_BOOTS), getWaffe(Material.IRON_AXE, 1)),
	KIT29(getArmor(Material.IRON_HELMET), getArmor(Material.IRON_CHESTPLATE, 1), getArmor(Material.IRON_LEGGINGS), getArmor(Material.IRON_BOOTS, 1), getWaffe(Material.IRON_AXE, 1)),
	KIT30(getArmor(Material.IRON_HELMET, 1), getArmor(Material.IRON_CHESTPLATE, 1), getArmor(Material.IRON_LEGGINGS), getArmor(Material.IRON_BOOTS, 1), getWaffe(Material.IRON_AXE, 1)),
	KIT31(getArmor(Material.IRON_HELMET, 1), getArmor(Material.IRON_CHESTPLATE, 1), getArmor(Material.IRON_LEGGINGS, 1), getArmor(Material.IRON_BOOTS, 1), getWaffe(Material.IRON_AXE, 1)),
	KIT32(getArmor(Material.IRON_HELMET, 1), getArmor(Material.IRON_CHESTPLATE, 1), getArmor(Material.IRON_LEGGINGS, 1), getArmor(Material.IRON_BOOTS, 1), getWaffe(Material.IRON_SWORD, 1)),
	/*DIAMOND*/
	KIT33(getArmor(Material.IRON_HELMET, 1), getArmor(Material.DIAMOND_CHESTPLATE), getArmor(Material.IRON_LEGGINGS, 1), getArmor(Material.IRON_BOOTS, 1), getWaffe(Material.DIAMOND_SWORD)),
	KIT34(getArmor(Material.IRON_HELMET, 1), getArmor(Material.DIAMOND_CHESTPLATE), getArmor(Material.IRON_LEGGINGS, 1), getArmor(Material.DIAMOND_BOOTS), getWaffe(Material.DIAMOND_SWORD)),
	KIT35(getArmor(Material.DIAMOND_HELMET), getArmor(Material.DIAMOND_CHESTPLATE), getArmor(Material.DIAMOND_LEGGINGS), getArmor(Material.DIAMOND_BOOTS), getWaffe(Material.DIAMOND_SWORD)),
	KIT36(getArmor(Material.DIAMOND_HELMET), getArmor(Material.DIAMOND_CHESTPLATE), getArmor(Material.DIAMOND_LEGGINGS), getArmor(Material.DIAMOND_BOOTS), getWaffe(Material.DIAMOND_SWORD)),
	KIT37(getArmor(Material.DIAMOND_HELMET), getArmor(Material.DIAMOND_CHESTPLATE), getArmor(Material.DIAMOND_LEGGINGS), getArmor(Material.DIAMOND_BOOTS), getWaffe(Material.DIAMOND_AXE, 1)),
	KIT38(getArmor(Material.DIAMOND_HELMET), getArmor(Material.DIAMOND_CHESTPLATE, 1), getArmor(Material.DIAMOND_LEGGINGS), getArmor(Material.DIAMOND_BOOTS), getWaffe(Material.DIAMOND_AXE, 1)),
	KIT39(getArmor(Material.DIAMOND_HELMET), getArmor(Material.DIAMOND_CHESTPLATE, 1), getArmor(Material.DIAMOND_LEGGINGS), getArmor(Material.DIAMOND_BOOTS, 1), getWaffe(Material.DIAMOND_AXE, 1)),
	KIT40(getArmor(Material.DIAMOND_HELMET, 1), getArmor(Material.DIAMOND_CHESTPLATE, 1), getArmor(Material.DIAMOND_LEGGINGS), getArmor(Material.DIAMOND_BOOTS, 1), getWaffe(Material.DIAMOND_AXE, 1)),
	KIT41(getArmor(Material.DIAMOND_HELMET, 1), getArmor(Material.DIAMOND_CHESTPLATE, 1), getArmor(Material.DIAMOND_LEGGINGS, 1), getArmor(Material.DIAMOND_BOOTS, 1), getWaffe(Material.DIAMOND_AXE, 1)),
	KIT42(getArmor(Material.DIAMOND_HELMET, 1), getArmor(Material.DIAMOND_CHESTPLATE, 1), getArmor(Material.DIAMOND_LEGGINGS, 1), getArmor(Material.DIAMOND_BOOTS, 1), getWaffe(Material.DIAMOND_SWORD, 1)),
	KIT43(getArmor(Material.DIAMOND_HELMET, 1), getArmor(Material.DIAMOND_CHESTPLATE, 1), getArmor(Material.DIAMOND_LEGGINGS, 1), getArmor(Material.DIAMOND_BOOTS, 1), getWaffe(Material.DIAMOND_SWORD, 2)),
	KIT44(getArmor(Material.DIAMOND_HELMET, 1), getArmor(Material.DIAMOND_CHESTPLATE, 1), getArmor(Material.DIAMOND_LEGGINGS, 1), getArmor(Material.DIAMOND_BOOTS, 1), getWaffe(Material.DIAMOND_SWORD, 3)),
	KIT45(getArmor(Material.DIAMOND_HELMET, 1), getArmor(Material.DIAMOND_CHESTPLATE, 1), getArmor(Material.DIAMOND_LEGGINGS, 1), getArmor(Material.DIAMOND_BOOTS, 1), getWaffe(Material.DIAMOND_SWORD, 4)),
	KIT46(getArmor(Material.DIAMOND_HELMET, 1), getArmor(Material.DIAMOND_CHESTPLATE, 1), getArmor(Material.DIAMOND_LEGGINGS, 1), getArmor(Material.DIAMOND_BOOTS, 1), getWaffe(Material.DIAMOND_SWORD, 5)),
	KIT47(getArmor(Material.DIAMOND_HELMET, 1), getArmor(Material.DIAMOND_CHESTPLATE, 1), getArmor(Material.DIAMOND_LEGGINGS, 1), getArmor(Material.DIAMOND_BOOTS, 1), getWaffe(Material.DIAMOND_SWORD, 6)),
	KIT48(getArmor(Material.DIAMOND_HELMET, 1), getArmor(Material.DIAMOND_CHESTPLATE, 1), getArmor(Material.DIAMOND_LEGGINGS, 1), getArmor(Material.DIAMOND_BOOTS, 1), getWaffe(Material.DIAMOND_SWORD, 7)),
	KIT49(getArmor(Material.DIAMOND_HELMET, 1), getArmor(Material.DIAMOND_CHESTPLATE, 1), getArmor(Material.DIAMOND_LEGGINGS, 1), getArmor(Material.DIAMOND_BOOTS, 1), getWaffe(Material.DIAMOND_SWORD, 8)),
	KIT50(getArmor(Material.DIAMOND_HELMET, 1), getArmor(Material.DIAMOND_CHESTPLATE, 1), getArmor(Material.DIAMOND_LEGGINGS, 1), getArmor(Material.DIAMOND_BOOTS, 1), getWaffe(Material.DIAMOND_SWORD, 9));
	private final ItemStack helmet;
	private final ItemStack chestplate;
	private final ItemStack leggins;
	private final ItemStack boots;
	private final ItemStack item;
	
	Kits(ItemStack helmet, ItemStack chestplate, ItemStack leggins, ItemStack boots, ItemStack item)
	{
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggins = leggins;
		this.boots = boots;
		this.item = item;
	}
	
	public static ItemStack getWaffe(Material material)
	{
		return getWaffe(material, 0);
	}
	
	public static ItemStack getWaffe(Material material, int level)
	{
		if (level != 0)
		{
			return new ItemBuilder(material).setName(SECONDARY + "Waffe").addUnsafeEnchantment(Enchantment.DAMAGE_ALL, level).setUnbreakable().hideItemFlags().build();
		}
		else
		{
			return new ItemBuilder(material).setName(SECONDARY + "Waffe").setUnbreakable().hideItemFlags().build();
		}
	}
	
	public static ItemStack getArmor(Material material)
	{
		return getArmor(material, 0);
	}
	
	public static ItemStack getArmor(Material material, int level)
	{
		String name = "";
		String typeName = material.toString().toLowerCase();
		if (typeName.endsWith("helmet"))
		{
			typeName = SECONDARY + "Helm";
		}
		else if (typeName.endsWith("chestplate"))
		{
			typeName = SECONDARY + "Brustpanzer";
		}
		else if (typeName.endsWith("leggings"))
		{
			typeName = SECONDARY + "Hose";
		}
		else if (typeName.endsWith("boots"))
		{
			typeName = SECONDARY + "Schuhe";
		}
		if (level != 0)
		{
			return new ItemBuilder(material).setName(SECONDARY + typeName).addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level).setUnbreakable().hideItemFlags().build();
		}
		else
		{
			return new ItemBuilder(material).setName(SECONDARY + typeName).setUnbreakable().hideItemFlags().build();
		}
	}
}
