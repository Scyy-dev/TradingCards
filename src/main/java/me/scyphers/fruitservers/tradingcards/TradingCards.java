package me.scyphers.fruitservers.tradingcards;

import me.scyphers.fruitservers.tradingcards.api.Messenger;
import me.scyphers.fruitservers.tradingcards.cards.CardGenerator;
import me.scyphers.fruitservers.tradingcards.config.Settings;
import me.scyphers.fruitservers.tradingcards.config.SimpleConfigManager;
import me.scyphers.fruitservers.tradingcards.event.EventListener;
import me.scyphers.fruitservers.tradingcards.command.AdminCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class TradingCards extends JavaPlugin {

    private SimpleConfigManager configManager;

    private CardGenerator generator;

    @Override
    public void onEnable() {

        // Register the Config Manager
        this.configManager = new SimpleConfigManager(this);

        this.generator = this.setUpGenerator();

        // Register the Admin Command
        AdminCommand adminCommand = new AdminCommand(this);
        this.getCommand("admin").setExecutor(adminCommand);
        this.getCommand("admin").setTabCompleter(adminCommand);

        Bukkit.getPluginManager().registerEvents(new EventListener(this), this);

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public CardGenerator setUpGenerator() {
        return new CardGenerator(null);
    }

    public void reload(CommandSender sender) {
        try {
            sender.sendMessage("Reloading...");
            configManager.reloadConfigs();
            sender.sendMessage("Successfully reloaded!");
        } catch (Exception e) {
            sender.sendMessage("Error reloading! Check console for logs!");
            e.printStackTrace();
        }
    }

    public SimpleConfigManager getConfigManager() {
        return configManager;
    }

    public Settings getSettings() {
        return configManager.getSettings();
    }

    public Messenger getMessenger() {
        return configManager.getMessenger();
    }

    public List<String> getSplashText() {
        StringBuilder authors = new StringBuilder();
        for (String author : this.getDescription().getAuthors()) {
            authors.append(author).append(", ");
        }
        authors.delete(authors.length() - 1, authors.length());
        return Arrays.asList(
                "TradingCards v" + this.getDescription().getVersion(),
                "Built by" + authors
        );
    }


}
