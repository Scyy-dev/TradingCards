package me.scyphers.fruitservers.tradingcards.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public abstract class ConfigFile {

    private final ConfigManager manager;

    private final File configFile;

    public ConfigFile(ConfigManager manager, String configFilePath, boolean loadFromResource) {

        // Save the manager reference
        this.manager = manager;

        Plugin plugin = manager.getPlugin();

        // Save the message file path

        // Save the messages file
        this.configFile = new File(plugin.getDataFolder(), configFilePath);

        // Check if the file exists
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            if (loadFromResource) {
                plugin.saveResource(configFilePath, false);
            } else {
                try {
                    configFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        YamlConfiguration config = new YamlConfiguration();

        // Load the file
        try {
            config.load(configFile);
            this.load(config);
            // Catch invalid YAML
        } catch (InvalidConfigurationException e) {
            plugin.getLogger().warning("Invalid configuration found! Please check your configs!");
            e.printStackTrace();
            // IOExceptions and anything else unexpected
        } catch (Exception e) {
            plugin.getLogger().warning("Something went wrong loading the files!");
            e.printStackTrace();
        }

    }

    /**
     * For loading all of the data into relevant data storage
     *
     * @param configuration the file configuration to load data from
     * @throws Exception thrown when there is an error loading data
     */
    public abstract void load(YamlConfiguration configuration) throws Exception;

    /**
     * For saving all data into an empty configuration which is then saved to file by {@link ConfigFile#save()}. If the config file is intended to be a ready only type file (e.g. config.yml) then this method should always return false
     *
     * @return if the configuration should be saved to file or ignored
     * @throws Exception If an error occurs saving the data
     */
    public abstract boolean saveData(YamlConfiguration configuration) throws Exception;

    /**
     * Saves the data to the file. Will not write data to file if false is returned by {@link ConfigFile#saveData(YamlConfiguration)}
     *
     * @throws Exception if an exception occurs while trying to save data to configuration or file
     */
    public void save() throws Exception {
        YamlConfiguration configuration = new YamlConfiguration();
        boolean saveToFile = this.saveData(configuration);
        if (saveToFile) configuration.save(configFile);
    }

    /**
     * For reloading the file after changes have been made. This method only needs to be overridden if calling {@link ConfigFile#load(YamlConfiguration)} multiple times is unsafe
     *
     * @throws Exception if an error occurs while reloading
     */
    public void reload() throws Exception {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
        this.load(configuration);
    }

    public File getConfigFile() {
        return configFile;
    }

    public ConfigManager getManager() {
        return manager;
    }

}
