package net.darkblocks.core.bungee.permissions.listener;

import net.md_5.bungee.api.plugin.Listener;

/**
 * Created by LartyHD on 06.10.2017  23:30.
 */
public class PermissisonsListener implements Listener
{
	/*private final Permissions permissions;
	
	public PermissisonsListener(Plugin plugin)
	{
		this.permissions = new Permissions();
		BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
	}
	
	@EventHandler
	public void onPostLogin(PostLoginEvent event)
	{
		this.permissions.loadPermissions(event.getPlayer());
	}
	
	@EventHandler
	public void onEvent(PermissionCheckEvent event)
	{
		CommandSender sender = event.getSender();
		if (!(sender instanceof ProxiedPlayer))
		{
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Set<String> userPermisions = this.permissions.loadPerms(player.getUniqueId()).keySet();
		if (userPermisions.contains("*"))
		{
			event.setHasPermission(true);
			return;
		}
		String permision = event.getPermission();
		if (permision == null || !permision.endsWith("*"))
		{
			return;
		}
		permision = permision.substring(0, permision.length() - 1);
		for (String s : userPermisions)
		{
			if (s.startsWith(permision))
			{
				event.setHasPermission(true);
				return;
			}
		}
	}*/
}
