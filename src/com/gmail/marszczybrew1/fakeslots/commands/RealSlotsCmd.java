package com.gmail.marszczybrew1.fakeslots.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.marszczybrew1.fakeslots.ConfigAccessor;
import com.gmail.marszczybrew1.fakeslots.FakeSlots;

public class RealSlotsCmd implements CommandExecutor {

	private ConfigAccessor messages;
	private ConfigAccessor config;
	private FakeSlots plugin;
	private CommandSender player;

	public RealSlotsCmd(FakeSlots instance) {
		plugin = instance;
		this.config = plugin.config;
		this.messages = plugin.messages;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		this.player = sender;
		
		if (args.length != 1) {
			sendMsg(player, ChatColor.RED, getMessage("only-numbers"));
			return false;
		} else if (args[0].equalsIgnoreCase("reset")) {
			setSlotsReal(Bukkit.getMaxPlayers());
			sendMsg(player,
					ChatColor.GREEN,
					String.format(
							getMessage("slots-reset"),
							Integer.toString(Bukkit.getMaxPlayers())));
			return true;
		} else if (isInteger(args[0]) == false) {
			sendMsg(player, ChatColor.RED, getMessage("only-numbers"));
			return false;
		} else {
			setSlotsReal(Integer.parseInt(args[0]));
			sendMsg(player, ChatColor.GREEN, String.format(getMessage("realslots-set"), args[0]));
			return true;
		}
	}

	private static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private String getMessage(String path) {
		return messages.getConfig().getString(path);
	}

	private void setSlotsReal(int slots) {
		if (config.getConfig().isInt("slots") == false) {
			config.getConfig().set("slots", slots);
			config.saveConfig();
		} else {
			config.getConfig().set("slots", slots);
			config.saveConfig();
		}
	}

	private void sendMsg(CommandSender p, ChatColor color, String msg) {
		p.sendMessage(color + msg);
		return;
	}
}
