package net.darkblocks.dark.spigot.fakeplayer;

import com.mojang.authlib.GameProfile;
import net.darkblocks.dark.java.utils.ReflectUtils;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * Created by LartyHD on 17.01.2018  00:30.
 */
public class FakePlayer extends ReflectUtils
{
	private final int entityID;
	private final Location location;
	private final GameProfile gameprofile;
	
	public FakePlayer(String name, Location location)
	{
		this.entityID = (int) Math.ceil(Math.random() * 1000) + 2000;
		this.location = location;
		this.gameprofile = new GameProfile(UUID.randomUUID(), name);
	}
	
	public void spawn(Player player)
	{
		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
		setValue(packet, "a", this.entityID);
		setValue(packet, "b", this.gameprofile.getId());
		setValue(packet, "c", MathHelper.floor(this.location.getX() * 32.0D));
		setValue(packet, "d", MathHelper.floor(this.location.getY() * 32.0D));
		setValue(packet, "e", MathHelper.floor(this.location.getZ() * 32.0D));
		setValue(packet, "f", (byte) ((int) (this.location.getYaw() * 256.0F / 360.0F)));
		setValue(packet, "g", (byte) ((int) (this.location.getPitch() * 256.0F / 360.0F)));
		setValue(packet, "h", 0);
		DataWatcher dataWatcher = new DataWatcher(null);
		dataWatcher.a(6, (float) 20);
		dataWatcher.a(10, (byte) 127);
		setValue(packet, "i", dataWatcher);
		sendPacket(player, packet);
	}
	
	public void spawnAndAddToTabList(Player player)
	{
		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
		setValue(packet, "a", this.entityID);
		setValue(packet, "b", this.gameprofile.getId());
		setValue(packet, "c", MathHelper.floor(this.location.getX() * 32.0D));
		setValue(packet, "d", MathHelper.floor(this.location.getY() * 32.0D));
		setValue(packet, "e", MathHelper.floor(this.location.getZ() * 32.0D));
		setValue(packet, "f", (byte) ((int) (this.location.getYaw() * 256.0F / 360.0F)));
		setValue(packet, "g", (byte) ((int) (this.location.getPitch() * 256.0F / 360.0F)));
		setValue(packet, "h", 0);
		DataWatcher dataWatcher = new DataWatcher(null);
		dataWatcher.a(6, (float) 20);
		dataWatcher.a(10, (byte) 127);
		setValue(packet, "i", dataWatcher);
		addToTabList(player);
		sendPacket(player, packet);
	}
	
	public void destroyAndRemoveFromTabList(Player player)
	{
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(this.entityID);
		removeFromTabList(player);
		sendPacket(player, packet);
	}
	
	public void destroy(Player player)
	{
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(this.entityID);
		sendPacket(player, packet);
	}
	
	public void addToTabList(Player player)
	{
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(this.gameprofile, 1, EnumGamemode.NOT_SET, CraftChatMessage.fromString(this.gameprofile.getName())[0]);
		@SuppressWarnings("unchecked")
		List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
		//noinspection ConstantConditions
		players.add(data);
		setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
		setValue(packet, "b", players);
		sendPacket(player, packet);
	}
	
	public void removeFromTabList(Player player)
	{
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(this.gameprofile, 1, EnumGamemode.NOT_SET, CraftChatMessage.fromString(this.gameprofile.getName())[0]);
		@SuppressWarnings("unchecked")
		List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
		//noinspection ConstantConditions
		players.add(data);
		setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
		setValue(packet, "b", players);
		sendPacket(player, packet);
	}
	
	private void sendPacket(Player player, Packet packet)
	{
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
}
