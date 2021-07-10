package me.scyphers.plugins.pluginname.event;

import me.scyphers.plugins.pluginname.Plugin;
import org.bukkit.event.Listener;

public class EventListener implements Listener {

    private final Plugin plugin;

    public EventListener(Plugin plugin) {
        this.plugin = plugin;
    }

}
