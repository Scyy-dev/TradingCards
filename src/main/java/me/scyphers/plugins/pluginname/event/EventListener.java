package me.scyphers.plugins.pluginname.event;

import me.scyphers.plugins.pluginname.TradingCards;
import org.bukkit.event.Listener;

public class EventListener implements Listener {

    private final TradingCards plugin;

    public EventListener(TradingCards plugin) {
        this.plugin = plugin;
    }

}
