package me.scyphers.fruitservers.tradingcards.config;

import org.bukkit.configuration.file.YamlConfiguration;

public class Settings extends ConfigFile {

    private long saveTicks;

    public Settings(ConfigManager manager) {
        super(manager, "config.yml", true);
    }

    @Override
    public void load(YamlConfiguration configuration) throws Exception {
        this.saveTicks = configuration.getLong("fileSaveTicks", 72000);
    }

    // Settings are never updated through code
    @Override
    public void save(YamlConfiguration configuration) throws Exception {

    }

    public long getSaveTicks() {
        return saveTicks;
    }

}
