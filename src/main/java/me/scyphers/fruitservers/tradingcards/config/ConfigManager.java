package me.scyphers.fruitservers.tradingcards.config;

import org.bukkit.plugin.Plugin;

/**
 * Manager for a collection of config files. Recommended to provide methods for getting each of the ConfigFiles it manages
 */
public interface ConfigManager {

    /**
     * Reloads all configs this manager is responsible for
     */
    void reloadConfigs() throws Exception;

    /**
     * Saves all data (if any) to file
     * @throws Exception if there is an exception thrown while saving data
     */
    void saveAll() throws Exception;

    /**
     * Gets the plugin. Recommended to replace this with your actual plugin if you need access to methods in your plugin
     * @return the plugin
     */
    Plugin getPlugin();

    /**
     * Schedules the bukkit task to save all config data to file
     * @param saveTicks how often to save the file, in ticks
     * @return the bukkit task ID
     */
    int scheduleSaveTask(int saveTicks);

    /**
     * Cancels the save task, to help with server lag or if errors are arising
     */
    void cancelSaveTask();

}
