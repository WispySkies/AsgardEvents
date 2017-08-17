package com.fivefourdee.asgardevents;

import net.md_5.bungee.api.ChatColor;

import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class AsgardEvents extends JavaPlugin implements Listener{

	@Override
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(this,this);
		}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		PluginDescriptionFile pdf = getDescription();
		Server server = getServer();
		if(args.length<1){
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&bEvent&8]&9 No subcommand specified!"));
		    return true;
		}
		else if(args[0].equalsIgnoreCase("info")){
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&bEvents&8]&b&l AsgardEvents"));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&bEvents&8]&9 Plugin by &b54D&9 with help from &bXMen&9!"));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&bEvents&8]&9 Version &b")+pdf.getVersion());
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&bEvents&8]&9 Running on &b")+server.getServerName());
			return true;
		}
		else if(args[0].equalsIgnoreCase("reward")){
			if(args.length<2){
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&bEvent&8]&9 No player specified!"));
				return true;
			}
			else if(getServer().getPlayer(args[1])!=null){
				Player player = getServer().getPlayer(args[1]);
				// Gives player head.
				ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
				SkullMeta headmeta = (SkullMeta)head.getItemMeta();
				headmeta.setOwner(args[1]);
				head.setItemMeta(headmeta);
		        player.getInventory().addItem(head);
		        // Gives money.
		        getServer().dispatchCommand(getServer().getConsoleSender(), ("token give "+player.getName()+" 7"));
		        // Notifies executor.
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&bEvent&8]&9 Given default event rewards to &b" + args[1] + "&9!"));
				return true;
			}
			else{
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&bEvent&8]&9 Player is offline!"));
				return true;
			}
		}
		else{
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&bEvent&8]&9 Invalid subcommand specified!"));
			return true;
		}
	}
}
