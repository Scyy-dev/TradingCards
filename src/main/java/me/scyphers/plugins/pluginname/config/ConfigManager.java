package me.scyphers.plugins.pluginname.config;

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
     * Gets the plugin. Recommended to replace this with your actual plugin if you need access to methods in your plugin
     * @return the plugin
     */
    Plugin getPlugin();

}
