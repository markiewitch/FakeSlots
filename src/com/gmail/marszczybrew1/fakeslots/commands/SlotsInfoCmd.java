package com.gmail.marszczybrew1.fakeslots.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.marszczybrew1.fakeslots.FakeSlots;

public class SlotsInfoCmd implements CommandExecutor {

	private FakeSlots plugin;

	public SlotsInfoCmd(FakeSlots plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		sender.sendMessage(String.format(getMsg("fslots-state"), plugin.config
				.getConfig().getInt("fake-maxplayers")));
		sender.sendMessage(String.format(getMsg("rslots-state"), plugin.config
				.getConfig().getInt("slots")));
		return true;
	}

	private String getMsg(String path) {
		return plugin.messages.getConfig().getString(path);
	}

}
