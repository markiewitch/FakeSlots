package com.gmail.marszczybrew1.fakeslots.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.marszczybrew1.fakeslots.ConfigAccessor;
import com.gmail.marszczybrew1.fakeslots.FakeSlots;

public class FakeSlotsCmd implements CommandExecutor {

	private ConfigAccessor messages;
	private ConfigAccessor config;
	private FakeSlots plugin;
	private CommandSender player;

	// private int fakeSlots;
	// private int realSlots;

	public FakeSlotsCmd(FakeSlots instance) {
		plugin = instance;
		this.config = plugin.config;
		this.messages = plugin.messages;
		// this.fakeSlots = plugin.fakeSlots;
		// this.realSlots = plugin.realSlots;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		this.player = sender;
		if (args.length > 0) {
			if (isInteger(args[0]) == true) { // Jesli podano liczbe, zmienia
												// fake-sloty na podana wartosc
				setFSlotsConfig(Integer.parseInt(args[0]));
				sendMsg(player, ChatColor.GREEN,
						String.format(getMessage("fakeslots-set"), args[0]));
				return true;
			} else if (args[0].equalsIgnoreCase("reset")) { // Zresetuj liczbe
															// slotow do tej
															// nadpisanej lub
															// domyslnej bukkita
				setFSlotsConfig(config.getConfig().getInt("slots",
						Bukkit.getMaxPlayers()));
				sendMsg(player,
						ChatColor.GREEN,
						String.format(
								getMessage("slots-reset"),
								Integer.toString(config.getConfig().getInt(
										"slots", Bukkit.getMaxPlayers()))));
				return true;
			} else if (args[0].equalsIgnoreCase("reload")) { // Przeladowuje
																// caly plugin
				plugin.getPluginLoader().disablePlugin(plugin);
				plugin.getPluginLoader().enablePlugin(plugin);
				sendMsg(player, ChatColor.GREEN, getMessage("reload"));
				return true;
			} else { // Jesli podano cos innego niz liczba wyslij ostrzezenie
				sendMsg(player, ChatColor.RED, getMessage("only-numbers"));
				return false;
			}
		} else { // Jesli wykonano komende bez argumentow...
			Bukkit.dispatchCommand(sender, "version " + plugin.getName());
			sender.sendMessage(plugin.messages.getConfig().getString(
					"need-help"));
			return true;
		}
	}

	/**
	 * Sprawdza czy podany String jest liczba.
	 * 
	 * @param s
	 *            String do sprawdzenia
	 * @return true jesli jest to liczba
	 */
	private static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Zwraca wiadomosc o podanej sciezce z pliku konfiguracyjnego
	 * 
	 * @param path
	 *            sciezka do wiadomosci
	 * @return wiadomosc pobrana z pliku konfiguracyjnego
	 */
	private String getMessage(String path) {
		return messages.getConfig().getString(path);
	}

	/**
	 * Ustawia liczbe slotow na podana
	 * 
	 * @param slots
	 *            liczba slotow
	 */
	private void setFSlotsConfig(int slots) {
		plugin.fakeSlots = slots;
		config.getConfig().set("fake-maxplayers", slots);
		config.saveConfig();
		return;
	}

	/**
	 * Wysyla wiadomosc do podanego gracza.
	 * 
	 * @param p
	 *            gracz
	 * @param color
	 *            kolor tekstu
	 * @param msg
	 *            wiadomosc do wyslania
	 */
	private void sendMsg(CommandSender p, ChatColor color, String msg) {
		p.sendMessage(color + msg);
		return;
	}
}