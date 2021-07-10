package me.scyphers.fruitservers.tradingcards.config;

import me.scyphers.fruitservers.tradingcards.TradingCards;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager implements ConfigManager {

    private final TradingCards plugin;

    private final Map<UUID, PlayerDataFile> dataFiles;

    public PlayerDataManager(TradingCards plugin) {
        this.plugin = plugin;
        this.dataFiles = new HashMap<>();
    }

    // This is data storage, as such there is only reading and writing, no reloading.
    @Override
    public void reloadConfigs() throws Exception {

    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    public PlayerDataFile getPlayerDataFile(UUID uuid) {
        if (dataFiles.containsKey(uuid)) {
            return dataFiles.get(uuid);
        } else {
            PlayerDataFile dataFile = new PlayerDataFile(this, uuid);
            dataFiles.put(uuid, dataFile);
            return dataFile;
        }
    }

}
