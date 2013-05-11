package com.gmail.marszczybrew1.fakeslots;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class UpdateNotifier implements Runnable {

	private FakeSlots plugin;
	private FileConfiguration messages;
	private PluginDescriptionFile pdf;
	private Player p;

	public UpdateNotifier(FakeSlots plugin, Player player) {
		setPlugin(plugin);
		setMessages(plugin.messages.getConfig());
		this.pdf = getPlugin().pdf;
		this.p = player;
		run();
	}

	@Override
	public void run() {
		if (getPlugin().updateAvailable == false)
			return;
		String msg = getMessages().getString("updateAvailable");
		p.sendMessage(ChatColor.AQUA + String.format(msg, pdf.getWebsite()) + "files/");
	}

	public FakeSlots getPlugin() {
		return plugin;
	}

	public void setPlugin(FakeSlots plugin) {
		this.plugin = plugin;
	}

	public FileConfiguration getMessages() {
		return messages;
	}

	public void setMessages(FileConfiguration messages) {
		this.messages = messages;
	}

}
