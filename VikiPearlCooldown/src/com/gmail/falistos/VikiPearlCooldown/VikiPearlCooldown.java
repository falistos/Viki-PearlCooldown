package com.gmail.falistos.VikiPearlCooldown;

import java.text.DecimalFormat;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VikiPearlCooldown extends JavaPlugin implements Listener {
	
	static Cooldown cd = new Cooldown(10);
	public static Permission perm = null;

	public void onEnable() {
		getLogger().info("Enabled");

		getServer().getPluginManager().registerEvents(this, this);
		
		setupPermissions();
	}
	
	private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            perm = permissionProvider.getProvider();
        }
        return (perm != null);
    }

	public void onDisable() {
		getLogger().info("Disabled");
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		
		Action eAction = e.getAction();
		
		if (eAction == Action.RIGHT_CLICK_AIR || eAction == Action.RIGHT_CLICK_BLOCK) {

			Player player = e.getPlayer();
			ItemStack inHand = e.getItem();
			
			if (inHand == null || inHand.getType() == Material.AIR)
			{
				return;
			}
			
			if (inHand.getType().equals(Material.ENDER_PEARL))
			{
				if (!player.hasPermission("vikipearls.bypass")) {
					if (cd.isUnderCooldown(player))
					{
						e.setCancelled(true);
						player.sendMessage(ChatColor.GOLD +this.getRoundDouble(cd.getCurrentCooldown(player))+ " secondes pour utiliser une enderpearl");
					
						player.updateInventory();
					}
					else
					{
						cd.resetCooldown(player);
					}
				}
			}
		}
	}
	  
    protected String getRoundDouble(double value)
    {
    	DecimalFormat df = new DecimalFormat("#");
		return df.format(value);
    }
}