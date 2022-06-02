package xyz.joscodes.randomfallingblocks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomFallingBlocks extends JavaPlugin implements Listener {

	private boolean isStarted;

	private Integer task;

	private List<Material> blocks = new ArrayList<>();

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, (Plugin) this);
		for (final Material mat : Material.values()) {
			if (mat.isBlock() && !mat.isLegacy() && !mat.isAir()) {
				blocks.add(mat);
			}
		}
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if (sender.hasPermission("blocks.*")) {
			if (((args.length == 2 || args.length == 1) && label.equalsIgnoreCase("blocks")) || label.equalsIgnoreCase("randomfallingblocks:blocks")) {
				if (args[0].equalsIgnoreCase("start") && args.length == 2) {
					if (!this.isStarted) {
						Integer rate = Integer.valueOf(0);
						try {
							rate = Integer.valueOf(args[1]);
						} catch (final NumberFormatException exc) {
							sender.sendMessage(ChatColor.RED + "Invalid number!");
							sender.sendMessage(ChatColor.BLUE + "Plugin made by SkipTurn");
						}
						if (rate.intValue() != 0) {
							this.isStarted = true;
							this.task = Integer.valueOf(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) this, new Runnable() {
								@Override
								public void run() {
									for (final Player p : Bukkit.getOnlinePlayers())
										RandomFallingBlocks.this.spawnBlock(p);
								}
							}, (20 / rate.intValue()), (20 / rate.intValue())));
							sender.sendMessage(ChatColor.GREEN + "Random Falling Blocks started with a rate of " + ChatColor.GOLD + ChatColor.BOLD + rate + ChatColor.RESET + ChatColor.GREEN + " blocks per second!");
							sender.sendMessage(ChatColor.BLUE + "Plugin made by SkipTurn");
						}
					} else if (this.isStarted) {
						sender.sendMessage(ChatColor.RED + "Random Falling Blocks is already started!");
						sender.sendMessage(ChatColor.BLUE + "Plugin made by SkipTurn");
					}
				} else if (args[0].equalsIgnoreCase("stop")) {
					if (this.isStarted) {
						this.isStarted = false;
						if (this.task != null)
							Bukkit.getScheduler().cancelTask(this.task.intValue());
						sender.sendMessage(ChatColor.GREEN + "Random Falling Blocks stopped.");
						sender.sendMessage(ChatColor.BLUE + "Plugin made by SkipTurn");
					} else if (!this.isStarted) {
						sender.sendMessage(ChatColor.RED + "Random Falling Blocks not started!");
						sender.sendMessage(ChatColor.BLUE + "Plugin made by SkipTurn");
					}
				} else if ((!args[0].equalsIgnoreCase("start") || args.length == 2) && !args[0].equalsIgnoreCase("stop")) {
					sender.sendMessage(ChatColor.RED + "Invalid Usage. Please try:");
					sender.sendMessage(ChatColor.GREEN + "/blocks start <blocks per second>");
					sender.sendMessage(ChatColor.GREEN + "/blocks stop");
					sender.sendMessage(ChatColor.BLUE + "Plugin made by SkipTurn");
				}
			} else if (args.length != 1 && args.length != 2) {
				sender.sendMessage(ChatColor.RED + "Invalid Usage. Please try:");
				sender.sendMessage(ChatColor.GREEN + "/blocks start <blocks per second>");
				sender.sendMessage(ChatColor.GREEN + "/blocks stop");
				sender.sendMessage(ChatColor.BLUE + "Plugin made by SkipTurn");
			}
			return true;
		}
		sender.sendMessage(ChatColor.RED + "You cannot use this command!");
		sender.sendMessage(ChatColor.BLUE + "Plugin made by SkipTurn");
		return true;
	}


	public void spawnBlock(final Player p) {
		final Random r = new Random();
		final Material m = this.blocks.get(r.nextInt(this.blocks.size()));
		final ArrayList<Integer> ints = new ArrayList<>();
		for (int i = -10; i < 10; i++)
			ints.add(Integer.valueOf(i));
		final Location ploc = p.getLocation();
		final Location loc = new Location(p.getWorld(), (ploc.getBlockX() + ((Integer) ints.get(r.nextInt(ints.size()))).intValue()), (ploc.getBlockY() + 20), (ploc.getBlockZ() + ((Integer) ints.get(r.nextInt(ints.size()))).intValue()));
		p.getWorld().spawnFallingBlock(loc, m.createBlockData());
	}
}
