package me.beans42.compass;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class compass extends JavaPlugin implements Listener {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("usage: /compass <tracking player> <tracked player>");
        	return true;
        }
    	
    	final Player player = Bukkit.getPlayerExact(args[0]);
        final Player target = Bukkit.getPlayerExact(args[1]);

        if (player == null) {
        	sender.sendMessage(args[0] + " isn't online or is not a valid player");
        	return true;
        }
        
        if (target == null || !player.canSee(target)) {
            sender.sendMessage(args[1] + " isn't online or is not a valid player");
            return true;
        }
        
        player.sendMessage("your compass is now tracking " + target.getName());

        new BukkitRunnable() {
            public void run() {
                if (!player.isOnline()) //if player is offline
                    this.cancel();

                else if (!target.isOnline()) { //if target is offline
                    player.sendMessage(target.getName() + " is offline. resetting compass to spawn");
                    sender.sendMessage(target.getName() + " is offline. resetting compass to spawn");
                    player.setCompassTarget(player.getWorld().getSpawnLocation());
                    this.cancel();
                }

                else
                    player.setCompassTarget(target.getLocation());
            }
        }.runTaskTimer(this, 5L, 100L);
        return true;
    }
}