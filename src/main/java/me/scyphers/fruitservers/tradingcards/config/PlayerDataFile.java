package me.scyphers.fruitservers.tradingcards.config;

import me.scyphers.fruitservers.tradingcards.api.PlayerCardTrader;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class PlayerDataFile extends ConfigFile implements PlayerCardTrader {

    private boolean cardsEnabled;

    public PlayerDataFile(ConfigManager manager, UUID playerUUID) {
        super(manager, "data" + File.separator + playerUUID.toString() + ".yml", false);
    }

    @Override
    public void load(YamlConfiguration configuration) throws Exception {
        this.cardsEnabled = configuration.getBoolean("cardsEnabled", true);
    }

    @Override
    public boolean saveData(YamlConfiguration configuration) throws Exception {
         configuration.set("cardsEnabled", cardsEnabled);
         configuration.save(this.getConfigFile());
         return true;
    }

    @Override
    public boolean isCardsEnabled() {
        return cardsEnabled;
    }

    @Override
    public void setCardsEnabled(boolean enabled) {
        this.cardsEnabled = enabled;
    }




}
