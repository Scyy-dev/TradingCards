package me.scyphers.fruitservers.tradingcards.config;

import me.scyphers.fruitservers.tradingcards.TradingCards;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class SimpleConfigManager implements ConfigManager {

    private final TradingCards plugin;

    // Config Files
    private final Settings settings;
    private final MessengerFile messengerFile;
    private final CardsDataFile cardsDataFile;

    private final int saveTaskID;

    private boolean safeToSave = true;

    public SimpleConfigManager(TradingCards plugin) {
        this.plugin = plugin;
        this.settings = new Settings(this);
        this.messengerFile = new MessengerFile(this);
        this.cardsDataFile = new CardsDataFile(this);

        // Schedule a repeating task to saveData the configs
        int saveTicks = settings.getSaveTicks();
        this.saveTaskID = scheduleSaveTask(saveTicks);

    }

    @Override
    public void reloadConfigs() throws Exception {
        settings.reload();
        messengerFile.reload();
    }

    @Override
    public void saveAll() throws Exception {
        settings.save();
        messengerFile.save();
        cardsDataFile.save();
    }

    @Override
    public TradingCards getPlugin() {
        return plugin;
    }

    @Override
    public int scheduleSaveTask(int saveTicks) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (safeToSave) {
                try {
                    this.saveAll();
                } catch (Exception e) {
                    plugin.getLogger().warning("Could not saveData config files!");
                    e.printStackTrace();
                    safeToSave = false;
                }
            }

        }, saveTicks, saveTicks);
    }

    @Override
    public void cancelSaveTask() {
        Bukkit.getScheduler().cancelTask(saveTaskID);
    }

    public Settings getSettings() {
        return settings;
    }

    public MessengerFile getMessenger() {
        return messengerFile;
    }

    public CardsDataFile getCardsDataFile() {
        return cardsDataFile;
    }
}
