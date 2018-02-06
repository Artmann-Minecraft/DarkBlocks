package net.darkblocks.dark.spigot.team;

import lombok.Getter;
import lombok.NonNull;
import net.darkblocks.dark.java.utils.Double;
import net.darkblocks.dark.java.utils.ServerState;
import net.darkblocks.dark.spigot.builder.ItemBuilder;
import net.darkblocks.dark.spigot.events.ServerStateChangeEvent;
import net.darkblocks.dark.spigot.utils.InventoryUtils;
import net.darkblocks.dark.universal.messages.Colors;
import net.darkblocks.dark.universal.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by LartyHD on 03.01.2018  12:30.
 */
@Getter
public class TeamManager implements Listener
{
	@NonNull
	private final List<GameTeam> teams;
	
	public TeamManager(@NonNull JavaPlugin javaPlugin, boolean colored, int teamsCount)
	{
		this.teams = new ArrayList<>();
		if (colored && teamsCount <= 14)
		{
			List<Double<String, ChatColor>> list = new ArrayList<>();
			list.add(new Double<>("Blau", ChatColor.DARK_BLUE));
			list.add(new Double<>("Rot", ChatColor.RED));
			list.add(new Double<>("Grün", ChatColor.DARK_GREEN));
			list.add(new Double<>("Gelb", ChatColor.YELLOW));
			list.add(new Double<>("Schwarz", ChatColor.BLACK));
			list.add(new Double<>("Weiß", ChatColor.WHITE));
			list.add(new Double<>("Orange", ChatColor.GOLD));
			list.add(new Double<>("Türkis", ChatColor.AQUA));
			list.add(new Double<>("Violett", ChatColor.DARK_PURPLE));
			list.add(new Double<>("Hellblau", ChatColor.BLUE));
			list.add(new Double<>("Hellgrün", ChatColor.GREEN));
			list.add(new Double<>("Hellgrau", ChatColor.GRAY));
			list.add(new Double<>("Grau", ChatColor.DARK_GRAY));
			list.add(new Double<>("Rosa", ChatColor.LIGHT_PURPLE));
			for (Double<String, ChatColor> teams : list)
			{
				if (this.teams.size() < teamsCount)
				{
					this.teams.add(new GameTeam(teams.getFirst(), teams.getSecond(), (Bukkit.getOnlinePlayers().size() / teamsCount) + 1, true));
				}
			}
		}
		else
		{
			for (int i = 0; i != teamsCount; i++)
			{
				this.teams.add(new GameTeam("T" + ((i) + 1), ChatColor.DARK_GRAY, (Bukkit.getOnlinePlayers().size() / teamsCount) + 1, false));
			}
		}
		Bukkit.getPluginManager().registerEvents(this, javaPlugin);
	}
	
	private ItemStack getItem(@NonNull GameTeam gameTeam, @NonNull Player player)
	{
		Set<Player> gameTeamPlayers = gameTeam.getPlayers();
		int size = gameTeamPlayers.size();
		String name = gameTeam.getName();
		ChatColor chatColor = gameTeam.getChatColor();
		ItemBuilder itemBuilder = new ItemBuilder(Material.BANNER, size, getBanner(name)).setName(chatColor + name + " §r| §7" + size + "§8/§7" + gameTeam.getSize());
		List<String> lore = new ArrayList<>();
		for (Player players : gameTeamPlayers)
		{
			if (players.getName().equalsIgnoreCase(player.getName()))
			{
				itemBuilder.addEnchant(Enchantment.LUCK, 10).hideItemFlags();
			}
			lore.add(Colors.TEXT + "- " + chatColor + players.getDisplayName());
		}
		if (lore.isEmpty())
		{
			lore.add(Colors.TEXT + "Leer");
		}
		return itemBuilder.setLore(lore).build();
	}
	
	public short getBanner(String name)
	{
		switch (name)
		{
			case "Weiß":
				return 15;
			case "Orange":
				return 14;
			case "Magenta":
				return 13;
			case "Hellblau":
				return 12;
			case "Gelb":
				return 11;
			case "Hellgrün":
				return 10;
			case "Rosa":
				return 9;
			case "Grau":
				return 8;
			case "Hellgrau":
				return 7;
			case "Türkis":
				return 6;
			case "Violettes":
				return 5;
			case "Blau":
				return 4;
			case "Braun":
				return 3;
			case "Grün":
				return 2;
			case "Rot":
				return 1;
			case "Schwarz":
				return 0;
			default:
				return 8;
		}
	}
	
	public boolean finishTeams()
	{
		for (Player players : Bukkit.getOnlinePlayers())
		{
			if (getTeam(players) == null)
			{
				GameTeam gameTeam = getLowestTeam();
				String teamWithColors = gameTeam.getChatColor() + gameTeam.getName();
				if (!gameTeam.add(players))
				{
					players.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Das Team " + teamWithColors + Colors.TEXT + "ist" + Colors.IMPORTANT + " voll");
				}
				else
				{
					players.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Du bist nun im Team " + Colors.IMPORTANT + teamWithColors);
				}
			}
		}
		int size = 0;
		for (GameTeam gameTeam : getTeams())
		{
			if (gameTeam.getPlayers().size() > 0)
			{
				size++;
			}
		}
		if (size < 2)
		{
			Bukkit.broadcastMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Es müssen mindestens " + Colors.IMPORTANT + "zwei " + Colors.TEXT + "Teams mit Spieler existieren");
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private GameTeam getLowestTeam()
	{
		ArrayList<GameTeam> resortedTeams = new ArrayList<>(this.teams);
		resortedTeams.sort((o1, o2) -> o1.getPlayers().size() < o2.getPlayers().size() ? -1 : 1);
		return resortedTeams.get(0);
	}
	
	public Set<Player> getNoTeamPlayers()
	{
		Set<Player> noTeam = new HashSet<>();
		for (Player players : Bukkit.getOnlinePlayers())
		{
			if (getTeam(players) == null)
			{
				noTeam.add(players);
			}
		}
		return noTeam;
	}
	
	public GameTeam getTeam(@NonNull Player player)
	{
		for (GameTeam gameTeam : this.teams)
		{
			if (gameTeam.getPlayers().contains(player))
			{
				return gameTeam;
			}
		}
		return null;
	}
	
	public GameTeam getTeam(@NonNull String name)
	{
		for (GameTeam gameTeam : getTeams())
		{
			if (gameTeam.getName().equalsIgnoreCase(name))
			{
				return gameTeam;
			}
		}
		return null;
	}
	
	public int getLivingTeams()
	{
		int living = 0;
		for (GameTeam gameTeam : this.teams)
		{
			if (gameTeam.getPlayers().size() > 0)
			{
				living++;
			}
		}
		return living;
	}
	
	public GameTeam getLastLivingTeam()
	{
		if (getLivingTeams() > 1)
		{
			return null;
		}
		for (GameTeam gameTeam : this.teams)
		{
			if (gameTeam.getPlayers().size() > 0)
			{
				return gameTeam;
			}
		}
		return null;
	}
	
	public void openTeamGUI(@NonNull Player player)
	{
		Inventory inventory = Bukkit.createInventory(null, InventoryUtils.getInventorySize(this.teams.size()), Colors.SECONDARY + "Teams");
		InventoryUtils.setDesign(inventory, new ArrayList<>());
		List<ItemStack> itemStacks = new ArrayList<>();
		for (GameTeam gameTeam : getTeams())
		{
			itemStacks.add(getItem(gameTeam, player));
		}
		InventoryUtils.sortChestInventory(inventory, itemStacks);
		player.openInventory(inventory);
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
		{
			ItemStack item = event.getItem();
			if (item != null && item.getType() == Material.ENDER_CHEST)
			{
				openTeamGUI(event.getPlayer());
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		for (GameTeam team : this.teams)
		{
			team.setSize((Bukkit.getOnlinePlayers().size() / this.teams.size()) + 1);
		}
		for (Player players : Bukkit.getOnlinePlayers())
		{
			InventoryView openInventory = players.getOpenInventory();
			if (openInventory != null)
			{
				String title = openInventory.getTitle();
				if (title != null && title.equalsIgnoreCase(Colors.SECONDARY + "Teams"))
				{
					players.closeInventory();
					openTeamGUI(players);
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		Inventory clickedInventory = event.getClickedInventory();
		ItemStack currentItem = event.getCurrentItem();
		if (currentItem != null && clickedInventory != null && clickedInventory.getName() != null && clickedInventory == player.getOpenInventory().getTopInventory() && clickedInventory.getName().equalsIgnoreCase(Colors.SECONDARY + "Teams"))
		{
			String itemName = currentItem.getItemMeta().getDisplayName();
			if (itemName != null)
			{
				event.setCancelled(true);
				GameTeam gameTeam;
				if (itemName.startsWith("§7T"))
				{
					if (getTeams().size() > 9)
					{
						gameTeam = getTeam(itemName.substring(0, 3));
					}
					else
					{
						gameTeam = getTeam(itemName.substring(0, 2));
					}
				}
				else
				{
					gameTeam = getTeam(ChatColor.stripColor(itemName).split(" ")[0]);
				}
				if (gameTeam == null)
				{
					player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Es ist ein " + Colors.IMPORTANT + "Fehler " + Colors.TEXT + "aufgetreten bitte versuche es noch mal");
				}
				else
				{
					GameTeam team = getTeam(player);
					if (team != null)
					{
						team.remove(player);
					}
					if (!gameTeam.add(player))
					{
						player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Das Team ist bereits " + Colors.IMPORTANT + "voll");
						player.closeInventory();
						openTeamGUI(player);
					}
					else
					{
						player.sendMessage(Messages.getInstance().getShortMessage(getClass(), "prefix") + Colors.TEXT + "Du bist nun im Team " + gameTeam.getChatColor() + gameTeam.getName());
						player.closeInventory();
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
}
