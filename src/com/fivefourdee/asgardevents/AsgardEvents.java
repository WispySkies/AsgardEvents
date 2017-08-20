package com.fivefourdee.asgardevents;

import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class AsgardEvents extends JavaPlugin implements Listener{
	
	Logger logger;
	private Economy economy;
	String prefix = "&8[&bEvent&8]&9 ";
	int debug = 0;
	
	@Override
	public void onEnable(){
		logger = getLogger();
		Bukkit.getPluginManager().registerEvents(this,this);
		setupEconomy();
	}
	
	private boolean setupEconomy(){
		logger = getLogger();
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null){
		    economy = economyProvider.getProvider();
		    logger.info("Vault found!");
		}
		return economy != null;
	}
    
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		class predefinedMessage{
			String help(){
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"&b&lAsgardEvents"));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"/event debug - Toggles debug mode."));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"/event help - Shows this help dialogue."));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"/event info - Shows plugin information."));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"/event reward <player> - Gives default rewards to player."));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"/event winner <player> <event> - Declares winner of event."));
				return null;
			}
			String offlinePlayer(){
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"Player is offline!"));
				return null;
			}
			String paramAmount(){
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"Invalid amount of parameters given!"));
				return null;
			}
	    }
		PluginDescriptionFile pdf = getDescription();
		Server server = getServer();
	    predefinedMessage pdm = new predefinedMessage();
		logger = getLogger();
	    
		if(args.length<1){
			pdm.help();
			return true;
		}
		else if(args[0].equalsIgnoreCase("debug")){
			if(debug==0){
				debug=1;
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"Debug mode toggled on!"));
				logger.info("[Debug] "+sender.getName()+" toggled debug mode ON.");
			}
			else{
				debug=0;
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"Debug mode toggled off!"));
				logger.info("[Debug] "+sender.getName()+" toggled debug mode OFF.");
			}
			return true;
		}
		else if(args[0].equalsIgnoreCase("help")){
			pdm.help();
			return true;
		}
		else if(args[0].equalsIgnoreCase("info")){
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"&b&lAsgardEvents"));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"Plugin by &b54D&9 with help from &bXMen&9!"));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"Version &b")+pdf.getVersion());
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"Running on &b")+server.getServerName());
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"For details, visit https://github.com/ArceusMaster0493/AsgardEvents"));
			return true;
		}
		else if(args[0].equalsIgnoreCase("reward")){
			Player player = getServer().getPlayer(args[1]);
			if(args.length!=2){
				pdm.paramAmount();
				return true;
			}
			else if(getServer().getPlayer(args[1])!=null){
				// Gives player head.
				ItemStack head = new ItemStack(Material.SKULL_ITEM,1,(short)3);
				SkullMeta headmeta = (SkullMeta)head.getItemMeta();
				headmeta.setOwner(args[1]);
				head.setItemMeta(headmeta);
		        player.getInventory().addItem(head);
		        // Gives money.
		        economy.depositPlayer(player,7777777);
		        // Gives tokens.
		        getServer().dispatchCommand(getServer().getConsoleSender(),("token give "+player.getName()+" 7"));
		        // Notifications.
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"Given default event rewards to &b"+args[1]+"&9!"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"You have been rewarded!"));
				if(debug==1){
					logger.info("[Debug] "+sender.getName()+" successfully gave default event rewards to "+player.getName()+".");
				}
				return true;
			}
			else{
				pdm.offlinePlayer();
				return true;
			}
		}
		else if(args[0].equalsIgnoreCase("winner")){
			Player player = getServer().getPlayer(args[1]);
			if(args.length!=3){
				pdm.paramAmount();
				return true;
			}
			else if(getServer().getPlayer(args[1])==null){
				pdm.offlinePlayer();
				return true;
			}
			else if((!args[2].equals("Spleef"))&&(!args[2].equals("WaterPvP"))&&(!args[2].equals("AsgardianRush"))&&(!args[2].equals("BowLMS"))){
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"Invalid event specified!"));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"Valid events are: &bAsgardianRush&9, &bBowLMS&9, &bSpleef&9, &bWaterPvP"));
				return true;
			}
			else{
				getServer().dispatchCommand(getServer().getConsoleSender(),("bc "+prefix+"&b"+args[1]+"&9 has won this round of &b"+args[2]+"&9 -- Congratulations!"));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"Declared &b"+args[1]+"&9 as the winner of &b"+args[2]+"&9!"));
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix+"You have won the event!"));
				if(debug==1){
					logger.info("[Debug] "+sender.getName()+" successfully declared "+player.getName()+" as winner of "+args[2]+".");
				}return true;
			}
		}
		else{
			pdm.help();
			return true;
		}
	}
}
