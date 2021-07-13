package me.scyphers.fruitservers.tradingcards;

import me.scyphers.fruitservers.tradingcards.api.Messenger;
import me.scyphers.fruitservers.tradingcards.api.PlayerCardTrader;
import me.scyphers.fruitservers.tradingcards.cards.CardGenerator;
import me.scyphers.fruitservers.tradingcards.command.CommandFactory;
import me.scyphers.fruitservers.tradingcards.config.PlayerDataManager;
import me.scyphers.fruitservers.tradingcards.config.Settings;
import me.scyphers.fruitservers.tradingcards.config.SimpleConfigManager;
import me.scyphers.fruitservers.tradingcards.event.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TradingCards extends JavaPlugin {

    private SimpleConfigManager configManager;

    private PlayerDataManager playerDataManager;

    private CommandFactory commandFactory;

    private CardGenerator generator;

    @Override
    public void onEnable() {

        // Register the Config Managers
        this.configManager = new SimpleConfigManager(this);
        this.playerDataManager = new PlayerDataManager(this);

        this.generator = this.setUpGenerator();

        // Register the CommandFactory
        this.commandFactory = new CommandFactory(this);
        this.getCommand("cards").setExecutor(commandFactory);
        this.getCommand("cards").setTabCompleter(commandFactory);

        Bukkit.getPluginManager().registerEvents(new EventListener(this), this);

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private CardGenerator setUpGenerator() {
        return new CardGenerator(
                configManager.getCardsDataFile().getCards(),
                configManager.getSettings().getCardChances()
        );
    }

    public void reload(CommandSender sender) {
        try {
            sender.sendMessage("Reloading...");
            configManager.reloadConfigs();
            this.generator = setUpGenerator();
            sender.sendMessage("Successfully reloaded!");
        } catch (Exception e) {
            sender.sendMessage("Error reloading! Check console for logs!");
            e.printStackTrace();
        }
    }

    public CardGenerator getGenerator() {
        return generator;
    }

    public SimpleConfigManager getConfigManager() {
        return configManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public PlayerCardTrader getPlayerCardTrader(UUID uuid) {
        return playerDataManager.getPlayerDataFile(uuid);
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
