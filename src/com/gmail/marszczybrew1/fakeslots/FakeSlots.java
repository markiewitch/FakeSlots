package com.gmail.marszczybrew1.fakeslots;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gmail.marszczybrew1.fakeslots.commands.FakeSlotsCmd;
import com.gmail.marszczybrew1.fakeslots.commands.RealSlotsCmd;
import com.gmail.marszczybrew1.fakeslots.commands.SlotsInfoCmd;
import com.gmail.marszczybrew1.fakeslots.listeners.PlayerLogin;
import com.gmail.marszczybrew1.fakeslots.listeners.ServerListPing;

public class FakeSlots extends JavaPlugin {
	public ConfigAccessor config;
	public ConfigAccessor messages;
	public int realSlots;
	public int fakeSlots;
	public boolean updateAvailable;
	public PluginDescriptionFile pdf;
	private boolean debugging;
	private PluginManager pm;

	@Override
	public void onEnable() {
		this.config = new ConfigAccessor(this, "config.yml");
		this.messages = new ConfigAccessor(this, "messages.yml");

		this.pm = Bukkit.getPluginManager();
		this.pdf = pm.getPlugin(getName())
				.getDescription();
		try {
			this.updateAvailable = compareVersions(updateCheck(pdf.getVersion()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		config.saveDefaultConfig();
		messages.saveDefaultConfig();
		loadListeners();
		loadCommands();
		setupMetrics();
		checkConfig();
		this.realSlots = getCfg().getInt("slots");
		this.fakeSlots = getCfg().getInt("fake-maxplayers");
		this.debugging = getCfg().getBoolean("debug");
		debug("Plugin enabled");
	}

	private String updateCheck(String currentVersion) throws Exception {
		String pluginUrlString = "http://dev.bukkit.org/server-mods/fakeslots/files.rss";
		try {
			URL url = new URL(pluginUrlString);
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder()
					.parse(url.openConnection().getInputStream());
			doc.getDocumentElement().normalize();
			NodeList nodes = doc.getElementsByTagName("item");
			Node firstNode = nodes.item(0);
			if (firstNode.getNodeType() == 1) {
				Element firstElement = (Element) firstNode;
				NodeList firstElementTagName = firstElement
						.getElementsByTagName("title");
				Element firstNameElement = (Element) firstElementTagName
						.item(0);
				NodeList firstNodes = firstNameElement.getChildNodes();
				return firstNodes.item(0).getNodeValue();
			}
		} catch (Exception localException) {
			return currentVersion;
		}
		return currentVersion;
	}

	private boolean compareVersions(String nVersion) {
		String cVersion = Bukkit.getPluginManager().getPlugin(getName())
				.getDescription().getVersion();
		cVersion = cVersion.replaceAll(".", "");
		if (cVersion.equals(nVersion)) {
			return false;
		} else {
			return true;
		}
	}

	private void checkConfig() {
		if (!getCfg().contains("slots")) {
			getCfg().set("slots", Bukkit.getMaxPlayers());
			saveCfg();
		}
		if (!getCfg().contains("fake-maxplayers")) {
			getCfg().set("fake-maxplayers", Bukkit.getMaxPlayers());
			saveCfg();
		}
		if (!getCfg().contains("reservations")) {
			getCfg().set("reservations", true);
			saveCfg();
		}
		if (!getCfg().contains("debug")) {
			getCfg().set("debug", false);
			saveCfg();
		}
		debug("Config up to date");
	}

	private void setupMetrics() {
		try {
			Metrics metrics = new Metrics(this);

			metrics.addCustomData(new Metrics.Plotter("Faked Slots Count") {

				@Override
				public int getValue() {
					return fakeSlots;
				}

			});

			metrics.start();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		debug("Metrics ready");
	}

	private void loadListeners() {
		getServer().getPluginManager().registerEvents(new ServerListPing(this),
				this);
		getServer().getPluginManager().registerEvents(new PlayerLogin(this),
				this);
		debug("Listeners registered");
	}

	private void loadCommands() {
		getCommand("fakeslots").setExecutor(new FakeSlotsCmd(this));
		getCommand("realslots").setExecutor(new RealSlotsCmd(this));
		getCommand("slotsinfo").setExecutor(new SlotsInfoCmd(this));
	}

	public FileConfiguration getCfg() {
		return config.getConfig();
	}

	public void debug(String msg) {
		if (isDebugging()) {
			System.out.println("[FakeSlots-DEBUG] " + msg);
		}
	}

	private boolean isDebugging() {
		return debugging;
	}

	private void saveCfg() {
		config.saveConfig();
	}
}
