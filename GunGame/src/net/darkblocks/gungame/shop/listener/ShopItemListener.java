/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */
package net.darkblocks.gungame.shop.listener;

import lombok.Getter;
import lombok.NonNull;
import net.darkblocks.dark.java.mysql.CoinsAPI;
import net.darkblocks.dark.java.utils.Callback;
import net.darkblocks.dark.spigot.listener.ItemListener;
import net.darkblocks.dark.spigot.utils.TimeUtils;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

import static net.darkblocks.dark.universal.messages.Colors.SECONDARY;
import static net.darkblocks.dark.universal.messages.Colors.TEXT;

/**
 * Created by LartyHD on 19.02.2018  02:33.
 */
@Getter
public abstract class ShopItemListener extends ItemListener
{
	private final Map<String, Integer> user;
	private final String displayName;
	private final int waitTime;
	private final int price;
	
	protected ShopItemListener(@NonNull JavaPlugin javaPlugin, @NonNull ItemStack itemStack, int waitTime, int price)
	{
		super(javaPlugin, itemStack);
		this.user = new HashMap<>();
		this.displayName = getItemStack().getItemMeta().getDisplayName();
		this.waitTime = waitTime;
		this.price = price;
	}
	
	private void used(@NonNull String name)
	{
		ShopItemListener.this.user.put(name, (int) (System.currentTimeMillis() / 1000 + this.waitTime + 1));
		new Thread(() -> {
			try
			{
				Thread.sleep(this.waitTime * 1000);
			} catch (InterruptedException ex)
			{
				ex.printStackTrace();
			} finally
			{
				ShopItemListener.this.user.remove(name);
			}
		}).start();
	}
	
	protected void buy(Player player, CoinsAPI coinsAPI, String price, Callback<Boolean> callback)
	{
		coinsAPI.getCoins(player.getUniqueId(), result -> {
			if (Integer.valueOf(result) >= getPrice())
			{
				if (getUser().get(player.getName()) != null)
				{
					player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + TEXT + "Du kannst " + SECONDARY + this.displayName + TEXT + " in " + TimeUtils.getZeit(getUser().get(player.getName()) - System.currentTimeMillis() / 1000) + " wieder kaufen");
					if (callback != null)
					{
						callback.call(false);
					}
				}
				else
				{
					used(player.getName());
					player.sendMessage(coinsAPI.removeCoins(player.getUniqueId(), price, result1 -> {
						if (callback != null)
						{
							callback.call(true);
						}
					}));
				}
			}
			else
			{
				player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "notenoughcoins"));
				if (callback != null)
				{
					callback.call(false);
				}
			}
		});
	}
	
	public abstract void buy(@NonNull Player player);
}
