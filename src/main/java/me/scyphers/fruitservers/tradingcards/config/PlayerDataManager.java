package me.scyphers.fruitservers.tradingcards.config;

import me.scyphers.fruitservers.tradingcards.TradingCards;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager implements ConfigManager {

    private final TradingCards plugin;

    private final Map<UUID, PlayerDataFile> dataFiles;

    private final File dataDirectory;

    private boolean safeToSave = true;

    public PlayerDataManager(TradingCards plugin) {
        this.plugin = plugin;
        this.dataDirectory = new File(plugin.getDataFolder(), "data");
        this.dataFiles = new HashMap<>();

        long saveTicks = plugin.getSettings().getSaveTicks();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (safeToSave) {
                try {
                    for (PlayerDataFile file : dataFiles.values()) {
                        file.save();
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("Could not save player data config files!");
                    e.printStackTrace();
                    safeToSave = false;
                }
            }

        }, saveTicks, saveTicks);
    }

    // This is data storage, as such there is only reading and writing, no reloading.
    @Override
    public void reloadConfigs() throws Exception {

    }

    public File getDataDirectory() {
        return dataDirectory;
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

    public void closePlayerDataFile(UUID uuid) {
        if (!dataFiles.containsKey(uuid)) return;
        PlayerDataFile dataFile = dataFiles.get(uuid);
        try {
            dataFile.save();
            dataFiles.remove(uuid);
        } catch (Exception e) {
            plugin.getLogger().warning("Could not save player data file " + uuid);
            e.printStackTrace();
        }
    }

}
