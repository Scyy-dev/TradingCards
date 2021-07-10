package me.scyphers.plugins.pluginname.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.UUID;

public class PlayerDataFile extends ConfigFile {

    public PlayerDataFile(ConfigManager manager, UUID playerUUID) {
        super(manager, playerUUID.toString() + ".yml", false);
    }

    @Override
    public void load(YamlConfiguration configuration) throws Exception {

    }

    @Override
    public void save(YamlConfiguration configuration) throws Exception {

    }

}
