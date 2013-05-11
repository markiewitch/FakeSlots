package com.gmail.marszczybrew1.fakeslots.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import com.gmail.marszczybrew1.fakeslots.FakeSlots;

public class ServerListPing implements Listener {
	private FakeSlots plugin;

	public ServerListPing(FakeSlots instance) {
		plugin = instance;
	}

	@EventHandler
	public void onServerPing(ServerListPingEvent evt) {
		int maxPlayers = plugin.config.getConfig().getInt("fake-maxplayers",
				666);
		evt.setMaxPlayers(maxPlayers);

		// Debugging stuff
		debug("Server pinged from IP: " + evt.getAddress());
		debug("Slot limit: " + evt.getMaxPlayers());
		debug("Player count: " + evt.getNumPlayers());
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
}