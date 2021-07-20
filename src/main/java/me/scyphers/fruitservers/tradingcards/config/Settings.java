package me.scyphers.fruitservers.tradingcards.config;

import me.scyphers.fruitservers.tradingcards.cards.BoosterPack;
import me.scyphers.fruitservers.tradingcards.cards.CardRarity;
import me.scyphers.fruitservers.tradingcards.cards.CardSource;
import me.scyphers.fruitservers.tradingcards.cards.ChanceProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Settings extends ConfigFile {

    private int saveTicks;

    private Map<CardSource, ChanceProvider> cardChances;

    private Map<String, BoosterPack> boosterPacks;

    private ChanceProvider shinyChance;

    public Settings(ConfigManager manager) {
        super(manager, "config.yml", true);
    }

    @Override
    public void load(YamlConfiguration configuration) throws Exception {
        this.saveTicks = configuration.getInt("fileSaveTicks", 72000);
        this.cardChances = new HashMap<>();
        this.boosterPacks = new HashMap<>();

        // Loading Card Chances

        // Drop Chances
        ConfigurationSection cardDropChanceSection = configuration.getConfigurationSection("chances.drop");
        if (cardDropChanceSection == null)
            throw new InvalidConfigurationException("could not find card drop chances in config.yml - chances.drop missing");

        for (String sourceKey : cardDropChanceSection.getKeys(false)) {
            CardSource source = CardSource.valueOf(sourceKey.toUpperCase(Locale.ROOT));
            String rawChance = cardDropChanceSection.getString(sourceKey, "1/100");
            String[] splitRawChance = rawChance.split("/");
            int chance = Integer.parseInt(splitRawChance[0]);
            int weight = Integer.parseInt(splitRawChance[1]);
            cardChances.put(source, new ChanceProvider(chance, weight));
        }

        // Rarity Chances
        ConfigurationSection raritySection = configuration.getConfigurationSection("chances.rarity");
        if (raritySection == null)
            throw new InvalidConfigurationException("could not find card rarity chances in config.yml - chances.rarity missing");

        // Iterate through rarity keys - common, rare etc
        for (String rarityKey : raritySection.getKeys(false)) {
            CardRarity rarity = CardRarity.valueOf(rarityKey.toUpperCase(Locale.ROOT));

            ConfigurationSection sourceSection = raritySection.getConfigurationSection(rarityKey);
            if (sourceSection == null)
                throw new InvalidConfigurationException("could not find card rarity chances in config.yml - chances.rarity." + rarityKey + " missing");

            // Iterate through source keys - passive, boss etc
            for (String sourceKey : sourceSection.getKeys(false)) {
                CardSource source = CardSource.valueOf(sourceKey.toUpperCase(Locale.ROOT));
                int rarityWeight = sourceSection.getInt(sourceKey);

                ChanceProvider provider = cardChances.get(source);
                provider.addRarityWeighting(rarity, rarityWeight);
            }

        }

        // Loading Booster Packs

        ConfigurationSection boosterPacksSection = configuration.getConfigurationSection("boosterpacks");
        if (boosterPacksSection == null)
            throw new InvalidConfigurationException("could not find booster packs in config.yml - boosterpacks missing");

        for (String boosterPackKey : boosterPacksSection.getKeys(false)) {

            ConfigurationSection singleBoosterPackSection = boosterPacksSection.getConfigurationSection(boosterPackKey);
            if (singleBoosterPackSection == null)
                throw new InvalidConfigurationException("could not find booster pack " + boosterPackKey + " in config.yml - boosterpack. " + boosterPackKey + " missing");

            Map<CardRarity, Integer> cardPack = new HashMap<>();
            String packDisplayName = "PACK_NAME_NOT_FOUND";

            for (String rarityKey : singleBoosterPackSection.getKeys(false)) {
                if (rarityKey.equalsIgnoreCase("name")) {
                    packDisplayName = singleBoosterPackSection.getString("name", "PACK_NAME_NOT_FOUND");
                    continue;
                }

                CardRarity rarity = CardRarity.valueOf(rarityKey.toUpperCase(Locale.ROOT));
                int amount = singleBoosterPackSection.getInt(rarityKey);

                cardPack.put(rarity, amount);

            }

            boosterPacks.put(boosterPackKey, new BoosterPack(packDisplayName, cardPack));

        }

        // Shiny Chances
        String rawChance = configuration.getString("chances.shiny", "1/100");
        String[] splitRawChance = rawChance.split("/");
        int chance = Integer.parseInt(splitRawChance[0]);
        int weight = Integer.parseInt(splitRawChance[1]);
        this.shinyChance = new ChanceProvider(chance, weight);

    }

    // Settings are never updated through code
    @Override
    public boolean saveData(YamlConfiguration configuration) throws Exception {
        return false;
    }

    public int getSaveTicks() {
        return saveTicks;
    }

    public Map<CardSource, ChanceProvider> getCardChances() {
        return cardChances;
    }

    public Map<String, BoosterPack> getBoosterPacks() {
        return boosterPacks;
    }

    public ChanceProvider getShinyChances() {
        return shinyChance;
    }
}
