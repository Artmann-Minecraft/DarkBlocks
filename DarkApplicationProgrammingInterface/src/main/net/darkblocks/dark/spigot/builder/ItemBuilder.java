package net.darkblocks.dark.spigot.builder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.NonNull;
import net.craftplugin.craftpluginapi.java.builder.Builder;
import net.craftplugin.craftpluginapi.java.utils.ReflectUtils;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LartyHD on 17.01.2018  01:32.
 */
@Getter
public class ItemBuilder implements Builder<ItemStack>
{
	private final ItemStack itemStack;
	
	/*
	 *CREATE
	 */
	public ItemBuilder(ItemStack itemStack)
	{
		this.itemStack = itemStack;
	}
	
	public ItemBuilder(Material material)
	{
		this.itemStack = new ItemStack(material);
	}
	
	public ItemBuilder(Material material, int amount)
	{
		this.itemStack = new ItemStack(material, amount);
	}
	
	public ItemBuilder(Material material, int amount, short damage)
	{
		this.itemStack = new ItemStack(material, amount, damage);
	}
	/*
	 *CREATE
	 */
	
	/*
	 *ItemStack Setter
	 */
	public ItemBuilder setMaterial(Material material)
	{
		getItemStack().setType(material);
		return this;
	}
	
	public ItemBuilder setType(Material material)
	{
		getItemStack().setType(material);
		return this;
	}
	
	public ItemBuilder setAmount(int amount)
	{
		getItemStack().setAmount(amount);
		return this;
	}
	
	public ItemBuilder setDamage(short damage)
	{
		getItemStack().setDurability(damage);
		return this;
	}
	
	public ItemBuilder setDurability(short durability)
	{
		getItemStack().setDurability(durability);
		return this;
	}
	/*
	 *ItemStack Setter
	 */
	
	/*
	 *ItemStack Add
	 */
	public ItemBuilder addEnchantment(Enchantment enchantment, int level)
	{
		getItemStack().addEnchantment(enchantment, level);
		return this;
	}
	
	public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments)
	{
		getItemStack().addEnchantments(enchantments);
		return this;
	}
	
	public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level)
	{
		getItemStack().addUnsafeEnchantment(enchantment, level);
		return this;
	}
	
	public ItemBuilder addUnsafeEnchantments(Map<Enchantment, Integer> enchantments)
	{
		getItemStack().addUnsafeEnchantments(enchantments);
		return this;
	}
	/*
	 *ItemStack Add
	 */
	
	/*
	 *ItemStack Remove
	 */
	public ItemBuilder removeEnchantment(Enchantment enchantment)
	{
		getItemStack().removeEnchantment(enchantment);
		return this;
	}
	/*
	 *ItemStack Remove
	 */
	
	/*
	 *ItemMeta Setter
	 */
	public ItemBuilder setLore(List<String> lore)
	{
		ItemMeta itemMeta = getItemStack().getItemMeta();
		itemMeta.setLore(lore);
		getItemStack().setItemMeta(itemMeta);
		return this;
	}
	
	public ItemBuilder setLore(String lore)
	{
		ItemMeta itemMeta = getItemStack().getItemMeta();
		itemMeta.setLore(Collections.singletonList(lore));
		getItemStack().setItemMeta(itemMeta);
		return this;
	}
	
	public ItemBuilder setName(String name)
	{
		setDisplayName(name);
		return this;
	}
	
	public ItemBuilder setDisplayName(String displayName)
	{
		ItemMeta itemMeta = getItemStack().getItemMeta();
		itemMeta.setDisplayName(displayName);
		getItemStack().setItemMeta(itemMeta);
		return this;
	}
	
	public ItemBuilder setBreakable()
	{
		ItemMeta itemMeta = getItemStack().getItemMeta();
		itemMeta.spigot().setUnbreakable(false);
		getItemStack().setItemMeta(itemMeta);
		return this;
	}
	
	public ItemBuilder setUnbreakable()
	{
		ItemMeta itemMeta = getItemStack().getItemMeta();
		itemMeta.spigot().setUnbreakable(true);
		getItemStack().setItemMeta(itemMeta);
		return this;
	}
	
	public ItemBuilder setUnbreakable(boolean unbreakable)
	{
		ItemMeta itemMeta = getItemStack().getItemMeta();
		itemMeta.spigot().setUnbreakable(unbreakable);
		getItemStack().setItemMeta(itemMeta);
		return this;
	}
	/*
	 *ItemMeta Setter
	 */
	
	/*
	 *ItemMeta Add
	 */
	public ItemBuilder addEnchant(Enchantment enchantment, int level)
	{
		ItemMeta itemMeta = getItemStack().getItemMeta();
		itemMeta.addEnchant(enchantment, level, true);
		getItemStack().setItemMeta(itemMeta);
		return this;
	}
	
	public ItemBuilder addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction)
	{
		ItemMeta itemMeta = getItemStack().getItemMeta();
		itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
		getItemStack().setItemMeta(itemMeta);
		return this;
	}
	
	public ItemBuilder addItemFlags(ItemFlag... itemFlags)
	{
		ItemMeta itemMeta = getItemStack().getItemMeta();
		itemMeta.addItemFlags(itemFlags);
		getItemStack().setItemMeta(itemMeta);
		return this;
	}
	/*
	 *ItemMeta Add
	 */
	
	/*
	 *ItemMeta Remove
	 */
	public ItemBuilder removeEnchant(Enchantment enchantment)
	{
		ItemMeta itemMeta = getItemStack().getItemMeta();
		itemMeta.removeEnchant(enchantment);
		getItemStack().setItemMeta(itemMeta);
		return this;
	}
	
	public ItemBuilder removeItemFlags(ItemFlag... itemFlags)
	{
		ItemMeta itemMeta = getItemStack().getItemMeta();
		itemMeta.removeItemFlags(itemFlags);
		getItemStack().setItemMeta(itemMeta);
		return this;
	}
	/*
	 *ItemMeta Remove
	 */
	
	/*
	 *ItemMeta Clear
	 */
	public ItemBuilder clearEnchants()
	{
		ItemMeta itemMeta = getItemStack().getItemMeta();
		itemMeta.getEnchants().clear();
		getItemStack().setItemMeta(itemMeta);
		return this;
	}
	
	public ItemBuilder clearItemFlags()
	{
		ItemMeta itemMeta = getItemStack().getItemMeta();
		itemMeta.getItemFlags().clear();
		getItemStack().setItemMeta(itemMeta);
		return this;
	}
	/*
	 *ItemMeta Clear
	 */
	
	/*
	 *ItemMeta Hide
	 */
	public ItemBuilder hideItemFlags()
	{
		ItemMeta itemMeta = getItemStack().getItemMeta();
		itemMeta.addItemFlags(ItemFlag.values());
		getItemStack().setItemMeta(itemMeta);
		return this;
	}
	/*
	 *ItemMeta Hide
	 */
	
	/*
	 *BannerMeta Setter
	 */
	public ItemBuilder setBaseColor(DyeColor dyeColor)
	{
		try
		{
			BannerMeta bannerMeta = (BannerMeta) getItemStack().getItemMeta();
			bannerMeta.setBaseColor(dyeColor);
			getItemStack().setItemMeta(bannerMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	
	public ItemBuilder setPatterns(List<Pattern> patterns)
	{
		try
		{
			BannerMeta bannerMeta = (BannerMeta) getItemStack().getItemMeta();
			bannerMeta.setPatterns(patterns);
			getItemStack().setItemMeta(bannerMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	
	public ItemBuilder setPattern(int i, Pattern pattern)
	{
		try
		{
			BannerMeta bannerMeta = (BannerMeta) getItemStack().getItemMeta();
			bannerMeta.setPattern(i, pattern);
			getItemStack().setItemMeta(bannerMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *BannerMeta Setter
	 */
	
	/*
	 *BannerMeta Add
	 */
	public ItemBuilder addPattern(Pattern pattern)
	{
		try
		{
			BannerMeta bannerMeta = (BannerMeta) getItemStack().getItemMeta();
			bannerMeta.addPattern(pattern);
			getItemStack().setItemMeta(bannerMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *BannerMeta Add
	 */
	
	/*
	 *BannerMeta Remove
	 */
	public ItemBuilder removePattern(int i)
	{
		try
		{
			BannerMeta bannerMeta = (BannerMeta) getItemStack().getItemMeta();
			bannerMeta.removePattern(i);
			getItemStack().setItemMeta(bannerMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *BannerMeta Remove
	 */
	
	/*
	 *BlockStateMeta Setter
	 */
	public ItemBuilder setBlockState(BlockState blockState)
	{
		try
		{
			BlockStateMeta blockStateMeta = (BlockStateMeta) getItemStack().getItemMeta();
			blockStateMeta.setBlockState(blockState);
			getItemStack().setItemMeta(blockStateMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *BlockStateMeta Setter
	 */
	
	/*
	 *BookMeta Setter
	 */
	public ItemBuilder setTitle(String title)
	{
		try
		{
			BookMeta bookMeta = (BookMeta) getItemStack().getItemMeta();
			bookMeta.setTitle(title);
			getItemStack().setItemMeta(bookMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	
	public ItemBuilder setAuthor(String author)
	{
		try
		{
			BookMeta bookMeta = (BookMeta) getItemStack().getItemMeta();
			bookMeta.setAuthor(author);
			getItemStack().setItemMeta(bookMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	
	public ItemBuilder setPage(int i, String page)
	{
		try
		{
			BookMeta bookMeta = (BookMeta) getItemStack().getItemMeta();
			bookMeta.setPage(i, page);
			getItemStack().setItemMeta(bookMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	
	public ItemBuilder setPages(List<String> pages)
	{
		try
		{
			BookMeta bookMeta = (BookMeta) getItemStack().getItemMeta();
			bookMeta.setPages(pages);
			getItemStack().setItemMeta(bookMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	
	public ItemBuilder setPages(String... pages)
	{
		try
		{
			BookMeta bookMeta = (BookMeta) getItemStack().getItemMeta();
			bookMeta.setPages(pages);
			getItemStack().setItemMeta(bookMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *BookMeta Setter
	 */
	
	/*
	 *BookMeta Add
	 */
	public ItemBuilder addPage(String... page)
	{
		try
		{
			BookMeta bookMeta = (BookMeta) getItemStack().getItemMeta();
			bookMeta.addPage(page);
			getItemStack().setItemMeta(bookMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *BookMeta Add
	 */
	
	/*
	 *FireworkEffectMeta Setter
	 */
	public ItemBuilder setFireworkEffectMetaEffect(FireworkEffect fireworkEffect)
	{
		try
		{
			FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta) getItemStack().getItemMeta();
			fireworkEffectMeta.setEffect(fireworkEffect);
			getItemStack().setItemMeta(fireworkEffectMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *FireworkEffectMeta Setter
	 */
	
	/*
	 *FireworkMeta Setter
	 */
	public ItemBuilder setFireworkMetaEffect(FireworkEffect fireworkEffect)
	{
		try
		{
			FireworkMeta fireworkMeta = (FireworkMeta) getItemStack().getItemMeta();
			fireworkMeta.addEffect(fireworkEffect);
			getItemStack().setItemMeta(fireworkMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	
	public ItemBuilder setFireworkMetaEffect(FireworkEffect... fireworkEffect)
	{
		try
		{
			FireworkMeta fireworkMeta = (FireworkMeta) getItemStack().getItemMeta();
			fireworkMeta.addEffects(fireworkEffect);
			getItemStack().setItemMeta(fireworkMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	
	public ItemBuilder setFireworkMetaEffect(Iterable<FireworkEffect> fireworkEffect)
	{
		try
		{
			FireworkMeta fireworkMeta = (FireworkMeta) getItemStack().getItemMeta();
			fireworkMeta.addEffects(fireworkEffect);
			getItemStack().setItemMeta(fireworkMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	
	public ItemBuilder setPower(int power)
	{
		try
		{
			FireworkMeta fireworkMeta = (FireworkMeta) getItemStack().getItemMeta();
			fireworkMeta.setPower(power);
			getItemStack().setItemMeta(fireworkMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *FireworkMeta Setter
	 */
	
	/*
	 *FireworkMeta Remove
	 */
	public ItemBuilder removeEffect(int effect)
	{
		try
		{
			FireworkMeta fireworkMeta = (FireworkMeta) getItemStack().getItemMeta();
			fireworkMeta.removeEffect(effect);
			getItemStack().setItemMeta(fireworkMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *FireworkMeta Remove
	 */
	
	/*
	 *FireworkMeta Clear
	 */
	public ItemBuilder clearEffects()
	{
		try
		{
			FireworkMeta fireworkMeta = (FireworkMeta) getItemStack().getItemMeta();
			fireworkMeta.clearEffects();
			getItemStack().setItemMeta(fireworkMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *FireworkMeta Clear
	 */
	
	/*
	 *LeatherArmorMeta Setter
	 */
	public ItemBuilder setColor(Color color)
	{
		try
		{
			LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) getItemStack().getItemMeta();
			leatherArmorMeta.setColor(color);
			getItemStack().setItemMeta(leatherArmorMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *LeatherArmorMeta Setter
	 */
	
	/*
	 *MapMeta Setter
	 */
	public ItemBuilder setScaling(boolean value)
	{
		try
		{
			MapMeta mapMeta = (MapMeta) getItemStack().getItemMeta();
			mapMeta.setScaling(value);
			getItemStack().setItemMeta(mapMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *MapMeta Setter
	 */
	
	/*
	 *PotionMeta Setter
	 */
	public ItemBuilder setMainEffect(PotionEffectType potionEffectType)
	{
		try
		{
			PotionMeta potionMeta = (PotionMeta) getItemStack().getItemMeta();
			potionMeta.setMainEffect(potionEffectType);
			getItemStack().setItemMeta(potionMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *PotionMeta Setter
	 */
	
	/*
	 *PotionMeta Add
	 */
	public ItemBuilder addCustomEffect(PotionEffect potionEffect, boolean overwrite)
	{
		try
		{
			PotionMeta potionMeta = (PotionMeta) getItemStack().getItemMeta();
			potionMeta.addCustomEffect(potionEffect, overwrite);
			getItemStack().setItemMeta(potionMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *PotionMeta Add
	 */
	
	/*
	 *PotionMeta Remove
	 */
	public ItemBuilder removeCustomEffect(PotionEffectType potionEffectType)
	{
		try
		{
			PotionMeta potionMeta = (PotionMeta) getItemStack().getItemMeta();
			potionMeta.removeCustomEffect(potionEffectType);
			getItemStack().setItemMeta(potionMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *PotionMeta Remove
	 */
	
	/*
	 *PotionMeta Clear
	 */
	public ItemBuilder clearCustomEffects()
	{
		try
		{
			PotionMeta potionMeta = (PotionMeta) getItemStack().getItemMeta();
			potionMeta.clearCustomEffects();
			getItemStack().setItemMeta(potionMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *PotionMeta Clear
	 */
	
	/*
	 *SkullMeta Setter
	 */
	public ItemBuilder setOwner(String owner)
	{
		try
		{
			SkullMeta skullMeta = (SkullMeta) getItemStack().getItemMeta();
			skullMeta.setOwner(owner);
			getItemStack().setItemMeta(skullMeta);
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	/*
	 *SkullMeta Setter
	 */
	
	public ItemBuilder setOwnerFromURL(@NonNull String url, @NonNull String name)
	{
		try
		{
			if (!url.isEmpty())
			{
				ItemMeta itemMeta = getItemStack().getItemMeta();
				GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
				gameProfile.getProperties().put("textures", new Property("textures", new String(Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes()))));
				ReflectUtils.setValue(itemMeta, "profile", gameProfile);
				getItemStack().setItemMeta(itemMeta);
			}
		} catch (ClassCastException ignored)
		{
		}
		return this;
	}
	
	/*
	 *Extras
	 */
	@Override
	protected ItemBuilder clone()
	{
		return new ItemBuilder(getItemStack());
	}
	
	@Override
	public ItemStack build()
	{
		return getItemStack();
	}
	/*
	 *Extras
	 */
}
