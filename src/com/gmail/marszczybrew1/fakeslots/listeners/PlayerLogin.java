package com.gmail.marszczybrew1.fakeslots.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.gmail.marszczybrew1.fakeslots.ConfigAccessor;
import com.gmail.marszczybrew1.fakeslots.FakeSlots;
import com.gmail.marszczybrew1.fakeslots.UpdateNotifier;

public class PlayerLogin implements Listener {
	private FakeSlots plugin;
	private ConfigAccessor config;
	private ConfigAccessor messages;
	// private Logger log = Bukkit.getLogger();
	private int slots;
	private static final String reservationPerm = "fakeslots.reserve";

	public PlayerLogin(FakeSlots instance) {
		plugin = instance;
		this.config = plugin.config;
		this.messages = plugin.messages;
	}

	@EventHandler
	public void onPlayerLoginEvent(PlayerLoginEvent evt) {
		this.slots = plugin.realSlots;
		Player p = evt.getPlayer();
		if (p.isBanned()) {
			evt.disallow(Result.KICK_BANNED, getMessage("banned-kick"));
			// Debugging stuff
			debug("Kicked banned player " + p.getName());
			debug("IP address: " + evt.getHostname());
			return;
		}
		if ((p.hasPermission(reservationPerm))
				&& (config.getConfig().getBoolean("reservations") == true)) {
			if (p.hasPermission("fakeslots.notify")) {
				Bukkit.getScheduler().runTaskLater(plugin,
						new UpdateNotifier(plugin, p), 80);
				// Debugging
				debug("Attached update notifier to player " + p.getName());
			}
			evt.allow();
			// Debugging
			debug("Allowed player " + p.getName()
					+ " to join because of reservation permission");
			return;
		}
		if (slots > Bukkit.getOnlinePlayers().length) {
			if (p.hasPermission("fakeslots.notify")) {
				Bukkit.getScheduler().runTaskLater(plugin,
						new UpdateNotifier(plugin, p), 80);
			}
			// Debugging
			debug("Allowed player " + p.getName()
					+ " to join because of free 'real' slot");
			evt.allow();
		} else {
			// Debugging
			debug("Disallowed player " + p.getName()
					+ "to join because of real slots limit reached");
			evt.disallow(Result.KICK_FULL, getMessage("full-kick"));
		}
	}

	/**
	 * Prints out a debug message
	 * 
	 * @param msg
	 *            message to be printed
	 */
	private void debug(String msg) {
		plugin.debug(msg);
	}

	private String getMessage(String path) {
		return messages.getConfig().getString(path);
	}
}
