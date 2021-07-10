package me.scyphers.plugins.pluginname.config;

import me.scyphers.plugins.pluginname.TradingCards;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class SimpleConfigManager implements ConfigManager {

    private final TradingCards plugin;

    // Config Files
    private final Settings settings;
    private final MessengerFile messengerFile;
    private final CardsDataFile cardsDataFile;

    private boolean safeToSave = true;

    public SimpleConfigManager(TradingCards plugin) {
        this.plugin = plugin;
        this.settings = new Settings(this);
        this.messengerFile = new MessengerFile(this);
        this.cardsDataFile = new CardsDataFile(this);

        // Schedule a repeating task to save the configs
        long saveTicks = settings.getSaveTicks();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (safeToSave) {
                try {
                    YamlConfiguration settingsConfig = YamlConfiguration.loadConfiguration(settings.getConfigFile());
                    settings.save(settingsConfig);
                    YamlConfiguration messengerConfig = YamlConfiguration.loadConfiguration(messengerFile.getConfigFile());
                    messengerFile.save(messengerConfig);
                    YamlConfiguration cardsConfig = YamlConfiguration.loadConfiguration(cardsDataFile.getConfigFile());
                    cardsDataFile.save(cardsConfig);
                } catch (Exception e) {
                    plugin.getLogger().warning("Could not save config files!");
                    e.printStackTrace();
                    safeToSave = false;
                }
            }

            }, saveTicks, saveTicks);

    }

    @Override
    public void reloadConfigs() throws Exception {
        settings.reload();
        messengerFile.reload();
    }

    @Override
    public TradingCards getPlugin() {
        return plugin;
    }

    public Settings getSettings() {
        return settings;
    }

    public MessengerFile getMessenger() {
        return messengerFile;
    }
}
