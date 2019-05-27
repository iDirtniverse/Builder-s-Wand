package de.False.BuildersWand;

import de.False.BuildersWand.ConfigurationFiles.Config;
import de.False.BuildersWand.ConfigurationFiles.Locales;
import de.False.BuildersWand.events.WandEvents;
import de.False.BuildersWand.events.WandStorageEvents;
import de.False.BuildersWand.manager.InventoryManager;
import de.False.BuildersWand.manager.WandManager;
import de.False.BuildersWand.utilities.ParticleUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private Locales locales = new Locales(this);
	private Config config;
	private ParticleUtil particleUtil;
	private WandManager wandManager;
	private InventoryManager inventoryManager;

	@Override
	public void onEnable() {
		wandManager = new WandManager(this);
		inventoryManager = new InventoryManager(this);

		loadConfigFiles();
		particleUtil = new ParticleUtil(config);
		registerEvents();
		registerCommands();
	}

	private void registerCommands() {
		getCommand("builderswand").setExecutor(new Commands(this, config, wandManager));
	}

	private void registerEvents() {
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new WandEvents(this, config, particleUtil, wandManager, inventoryManager), this);
		pluginManager.registerEvents(new WandStorageEvents(this, config, wandManager, inventoryManager), this);
	}

	private void loadConfigFiles() {
		config = new Config(this);
		locales.load();
		config.load();
		wandManager.load();
		inventoryManager.load();
	}
}
