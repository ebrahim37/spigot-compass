package me.beans42.compass;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class compass extends JavaPlugin implements Listener {
	static public HashMap<Player, Player> hm = new HashMap<Player, Player>();
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		new BukkitRunnable() {
			public void run() {
					for (HashMap.Entry<Player, Player> element : hm.entrySet()) {
						final Player tracker = element.getKey();
						final Player target = element.getValue();
						
						if (!tracker.isOnline() || !target.isOnline()) //if player or target is offline
							continue;
						else
							tracker.setCompassTarget(target.getLocation());
					}
			}
		}.runTaskTimer(this, 5L, 20L);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 3) {
			sender.sendMessage("usage: /compass <track/stop> <tracking player> <tracked player (ignored if first arg is stop)>");
			return true;
		}
		
		final Player player = Bukkit.getPlayerExact(args[1]);
		final Player target = Bukkit.getPlayerExact(args[2]);
		
		if (player == null) {
			sender.sendMessage(args[1] + " isn't online or is not a valid player");
			return true;
		}
		
		if (target == null || !player.canSee(target)) {
			sender.sendMessage(args[2] + " isn't online or is not a valid player");
			return true;
		}
		
		if (args[0].equals("track")) {
			if (hm.containsKey(player))
				hm.replace(player, target);
			else
				hm.put(player, target);
			player.sendMessage("your compass is now tracking " + target.getName());
			return true;
		}
		
		if (args[0].equals("stop")) {
			hm.remove(player);
			player.setCompassTarget(player.getWorld().getSpawnLocation());
			player.sendMessage("your compass is now pointing to spawn");
			return true;
		}
		
		sender.sendMessage("first arg needs to be track or stop");
		return true;
	}
}
