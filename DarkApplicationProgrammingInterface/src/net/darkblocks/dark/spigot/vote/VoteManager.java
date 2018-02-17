/*
 * © Copyright - Lars Artmann | LartyHD 2018.
 */

package net.darkblocks.dark.spigot.vote;

import lombok.Getter;
import lombok.Setter;
import net.darkblocks.dark.java.utils.ServerState;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.controller.GameController;
import net.darkblocks.dark.spigot.events.LobbyCountdownLastTenSecondsEvent;
import net.darkblocks.dark.spigot.events.ServerStateChangeEvent;
import net.darkblocks.dark.spigot.utils.InventoryUtils;
import net.darkblocks.dark.spigot.utils.MapsUtils;
import net.darkblocks.dark.spigot.utils.ScoreBoardUtils;
import net.darkblocks.dark.universal.messages.Colors;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Created by LartyHD on 04.01.2018  16:53.
 */
@Getter
@Setter
public class VoteManager implements Listener
{
	private final Set<String> maps;
	private final Set<String> voteMaps;
	private final GameController gameController;
	private Inventory mapVoteInventory;
	private String mapName;
	private Votes votes;
	
	public VoteManager(JavaPlugin javaPlugin, GameController gameController, Set<String> maps)
	{
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
		this.maps = maps;
		this.voteMaps = new HashSet<>();
		this.mapVoteInventory = Bukkit.createInventory(null, 9, Colors.SECONDARY + "Map Vote");
		InventoryUtils.setDesign(this.mapVoteInventory, new ArrayList<>());
		this.gameController = gameController;
		Set<Vote> votes = new HashSet<>();
		for (String map : maps)
		{
			votes.add(new Vote(map));
		}
		this.votes = new Votes(votes)
		{
			@Override
			public void finishVotes(String winner)
			{
				if (VoteManager.this.mapName == null || VoteManager.this.mapName.equalsIgnoreCase("") || VoteManager.this.mapName.equalsIgnoreCase(" "))
				{
					int count = 0;
					for (Vote vote : getVotes())
					{
						if (vote.getName().equalsIgnoreCase(winner))
						{
							count = vote.getVoter().size();
						}
					}
					for (Player players : Bukkit.getOnlinePlayers())
					{
						players.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + " ");
						players.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "     Das Voting ist beendet");
						players.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "     Gewinner" + Colors.IMPORTANT + ": " + Colors.PRIMARY + winner + Colors.TEXT + " (" + Colors.IMPORTANT + count + Colors.TEXT + ")");
						players.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + " ");
					}
					VoteManager.this.mapName = winner;
					MapsUtils.loadMap(winner);
				}
				else
				{
					for (Player players : Bukkit.getOnlinePlayers())
					{
						players.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + " ");
						players.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "     Map" + Colors.IMPORTANT + ": " + Colors.PRIMARY + VoteManager.this.mapName);
						players.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + " ");
					}
					MapsUtils.loadMap(VoteManager.this.mapName);
				}
			}
		};
		mapItems();
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		PlayerInventory inventory = player.getInventory();
		if (player.hasPermission("dark.settings"))
		{
			inventory.setItem(1, new ItemBuilder(Material.PAPER).setName(Colors.SECONDARY + "Settings").build());
		}
		else
		{
			inventory.setItem(1, new ItemBuilder(Material.PAPER).setName(Colors.SECONDARY + "Map Vote").build());
		}
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event)
	{
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		ItemStack currentItem = event.getCurrentItem();
		Inventory clickedInventory = event.getClickedInventory();
		if (clickedInventory != null && currentItem != null)
		{
			String title = clickedInventory.getTitle();
			if (title != null)
			{
				ItemMeta itemMeta = currentItem.getItemMeta();
				if (itemMeta != null)
				{
					if (title.equalsIgnoreCase(Colors.SECONDARY + "Map Vote"))
					{
						player.sendMessage(this.votes.addVote(player.getName(), ChatColor.stripColor(itemMeta.getDisplayName())));
						updateMapVoteInventory();
						player.closeInventory();
					}
					else
					{
						String displayName = itemMeta.getDisplayName();
						if (displayName != null)
						{
							Material type = currentItem.getType();
							if (title.equalsIgnoreCase(Colors.SECONDARY + "Settings"))
							{
								switch (type)
								{
									case ENDER_PEARL:
										this.gameController.getLobbyCountdowns().forEach(countdown ->
										{
											if (countdown.getSeconds() > 11)
											{
												if (Bukkit.getOnlinePlayers().size() > 1)
												{
													countdown.setSeconds(11);
													player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Die Runde wurde " + Colors.IMPORTANT + "gestartet");
												}
												else
												{
													player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Es braucht min. 2 " + Colors.IMPORTANT + "Spieler" + Colors.TEXT + " um die " + Colors.IMPORTANT + "Runde " + Colors.TEXT + "zu starten");
												}
											}
											else
											{
												player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Die Runde ist schon " + Colors.IMPORTANT + "gestartet");
											}
											player.closeInventory();
										});
										break;
									case MAP:
										if (this.mapName == null)
										{
											updateMapVoteInventory();
											player.openInventory(this.mapVoteInventory);
										}
										else
										{
											player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Die Map wurde schon " + Colors.IMPORTANT + "festgelegt");
											player.closeInventory();
										}
										break;
									case PAPER:
										Inventory inventory = Bukkit.createInventory(null, InventoryUtils.getInventorySize(this.maps.size()), Colors.SECONDARY + "ForceMap");
										InventoryUtils.setDesign(inventory, new ArrayList<>());
										List<ItemStack> itemStacks = new ArrayList<>();
										for (String maps : this.maps)
										{
											itemStacks.add(new ItemBuilder(Material.PAPER).setName(Colors.SECONDARY + maps).build());
										}
										InventoryUtils.sortChestInventory(inventory, itemStacks);
										player.openInventory(inventory);
										break;
								}
							}
							else if (title.equalsIgnoreCase(Colors.SECONDARY + "ForceMap") && type == Material.PAPER)
							{
								if (Bukkit.getOnlinePlayers().size() > 1)
								{
									this.mapName = ChatColor.stripColor(displayName);
									for (Player players : Bukkit.getOnlinePlayers())
									{
										ScoreBoardUtils.sendLobbyScoreBoard(players, this.mapName, Messages.getInstance().getShortMessage(getClass(), "servername"));
									}
									Bukkit.broadcastMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Map" + Colors.IMPORTANT + ": " + this.mapName);
								}
								else
								{
									player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Es braucht min. 2 " + Colors.IMPORTANT + "Spieler" + Colors.TEXT + " um eine " + Colors.IMPORTANT + "Map " + Colors.TEXT + "auszuwählen");
								}
								player.closeInventory();
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onServerStateChangeEvent(ServerStateChangeEvent event)
	{
		if (event.getNewServerState() != ServerState.LOBBY && event.getNewServerState() != ServerState.LOBBYFULL)
		{
			HandlerList.unregisterAll(this);
		}
	}
	
	@EventHandler
	public void onLobbyCountdownLastTenSecondsEvent(LobbyCountdownLastTenSecondsEvent event)
	{
		for (Player players : Bukkit.getOnlinePlayers())
		{
			InventoryView openInventory = players.getOpenInventory();
			if (openInventory != null)
			{
				Inventory topInventory = openInventory.getTopInventory();
				if (topInventory != null)
				{
					String title = topInventory.getTitle();
					if ((title.equalsIgnoreCase(Colors.SECONDARY + "Map Vote") || title.equalsIgnoreCase(Colors.SECONDARY + "Settings")))
					{
						players.closeInventory();
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && event.getMaterial() == Material.PAPER)
		{
			event.setCancelled(true);
			Player player = event.getPlayer();
			if (player.hasPermission("dark.settings"))
			{
				this.gameController.getLobbyCountdowns().forEach(countdown ->
				{
					if (this.mapName == null)
					{
						if (countdown.getSeconds() > 11)
						{
							Inventory inventory = Bukkit.createInventory(null, InventoryType.BREWING, Colors.SECONDARY + "Settings");
							inventory.setItem(3, new ItemBuilder(Material.HOPPER).setName(Colors.SECONDARY + "Settings").build());
							inventory.setItem(0, new ItemBuilder(Material.MAP).setName(Colors.SECONDARY + "Map Vote").hideItemFlags().build());
							inventory.setItem(1, new ItemBuilder(Material.ENDER_PEARL).setName(Colors.SECONDARY + "Start").build());
							inventory.setItem(2, new ItemBuilder(Material.PAPER).setName(Colors.SECONDARY + "ForceMap").build());
							player.openInventory(inventory);
							return;
						}
					}
					else
					{
						if (countdown.getSeconds() > 11)
						{
							Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, Colors.SECONDARY + "Settings");
							InventoryUtils.setDesign(inventory, new ArrayList<>());
							inventory.setItem(2, new ItemBuilder(Material.ENDER_PEARL).setName(Colors.SECONDARY + "Start").build());
							player.openInventory(inventory);
							return;
						}
					}
					player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Die Settings wurde schon " + Colors.IMPORTANT + "gesetzt");
				});
			}
			else
			{
				if (this.mapName == null)
				{
					this.gameController.getLobbyCountdowns().forEach(countdown ->
					{
						if (countdown.getSeconds() > 11)
						{
							updateMapVoteInventory();
							player.openInventory(this.mapVoteInventory);
						}
					});
				}
				else
				{
					player.closeInventory();
				}
				player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Die Map wurde schon " + Colors.IMPORTANT + "festgelegt");
			}
		}
	}
	
	private void mapItems() throws IndexOutOfBoundsException
	{
		if (this.maps.size() < 3)
		{
			throw new IndexOutOfBoundsException("Not enough maps available (at least 3)");
		}
		if (this.voteMaps.size() != 3)
		{
			Random random = new Random();
			List<String> mapNames = new ArrayList<>(this.maps);
			int size = mapNames.size();
			this.voteMaps.add(mapNames.get(random.nextInt(size)));
			this.voteMaps.add(mapNames.get(random.nextInt(size)));
			this.voteMaps.add(mapNames.get(random.nextInt(size)));
			if (this.voteMaps.size() != 3)
			{
				mapItems();
			}
		}
	}
	
	private void updateMapVoteInventory()
	{
		Object[] voteMaps = this.voteMaps.toArray();
		ItemBuilder itemBuilder = new ItemBuilder(Material.MAP).hideItemFlags();
		for (Vote vote : this.votes.getVotes())
		{
			String name = vote.getName();
			int size = vote.getVoter().size();
			if (name.equals(voteMaps[0]))
			{
				this.mapVoteInventory.setItem(1, itemBuilder.setName(Colors.SECONDARY + voteMaps[0].toString()).setLore(getLore(size)).setAmount(((short) size)).build());
			}
			else if (name.equals(voteMaps[1]))
			{
				this.mapVoteInventory.setItem(4, itemBuilder.setName(Colors.SECONDARY + voteMaps[1].toString()).setLore(getLore(size)).setAmount((short) size).build());
			}
			else if (name.equals(voteMaps[2]))
			{
				this.mapVoteInventory.setItem(7, itemBuilder.setName(Colors.SECONDARY + voteMaps[2].toString()).setLore(getLore(size)).setAmount((short) size).build());
			}
		}
	}
	
	private String getLore(int count)
	{
		return Colors.TEXT + "Diese Map hat " + Colors.IMPORTANT + count + " Votes";
	}
}
