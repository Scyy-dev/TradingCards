package me.scyphers.fruitservers.tradingcards.config;

import me.scyphers.fruitservers.tradingcards.WeightedChance;
import me.scyphers.fruitservers.tradingcards.cards.BoosterPack;
import me.scyphers.fruitservers.tradingcards.cards.CardRarity;
import me.scyphers.fruitservers.tradingcards.cards.CardSource;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Settings extends ConfigFile {

    private int saveTicks;

    private Map<EntityType, CardSource> cardSources;

    private Map<CardSource, WeightedChance<CardRarity>> cardChances;

    private boolean specificChances;
    private Map<EntityType, WeightedChance<CardRarity>> specificEntityChances;

    private Map<String, BoosterPack> boosterPacks;

    private double shinyChance;

    public Settings(ConfigManager manager) {
        super(manager, "config.yml", true);
    }

    @Override
    public void load(YamlConfiguration configuration) throws Exception {
        this.saveTicks = configuration.getInt("fileSaveTicks", 72000);
        this.cardSources = new HashMap<>();
        this.cardChances = new HashMap<>();
        this.specificEntityChances = new HashMap<>();
        this.boosterPacks = new HashMap<>();

        ConfigurationSection cardSourceSection = configuration.getConfigurationSection("cardSources");
        if (cardSourceSection == null) throw new InvalidConfigurationException("Could not create card source map");
        for (String rawSource : cardSourceSection.getKeys(false)) {
            CardSource source = CardSource.valueOf(rawSource.toUpperCase());
            List<String> rawEntities = cardSourceSection.getStringList(rawSource);
            rawEntities.stream().map(EntityType::valueOf).forEach(type -> this.cardSources.put(type, source));
        }

        // Drop Chances
        ConfigurationSection cardDropChanceSection = configuration.getConfigurationSection("chances.drop");
        if (cardDropChanceSection == null)
            throw new InvalidConfigurationException("could not find card drop chances in config.yml - chances.drop missing");

        for (String sourceKey : cardDropChanceSection.getKeys(false)) {
            CardSource source = CardSource.valueOf(sourceKey.toUpperCase(Locale.ROOT));
            double dropRate = cardDropChanceSection.getDouble(sourceKey, 0.01);
            cardChances.put(source, new WeightedChance<>(dropRate));
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

                WeightedChance<CardRarity> provider = cardChances.get(source);
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

        this.specificChances = configuration.getBoolean("useSpecificChances", false);
        if (specificChances) {
            ConfigurationSection specificChanceSection = configuration.getConfigurationSection("specificChances");
            if (specificChanceSection == null) throw new InvalidConfigurationException("Could not find specific chances");

            for (String rawEntity : specificChanceSection.getKeys(false)) {
                EntityType type = EntityType.valueOf(rawEntity.toUpperCase());

                ConfigurationSection entityCardSection = specificChanceSection.getConfigurationSection(rawEntity);
                if (entityCardSection == null) throw new InvalidConfigurationException("Could not find rarity for " + rawEntity);

                double dropChance = entityCardSection.getDouble("dropRate", 0.01);
                WeightedChance<CardRarity> chance = new WeightedChance<>(dropChance);

                ConfigurationSection entityCardRarities = entityCardSection.getConfigurationSection("rarityWeights");
                if (entityCardRarities == null) throw new InvalidConfigurationException("Could find card rarities for " + rawEntity);

                for (String rawRarity : entityCardRarities.getKeys(false)) {
                    CardRarity rarity = CardRarity.valueOf(rawRarity.toUpperCase());
                    int weight = entityCardSection.getInt(rawRarity, 1);
                    chance.addRarityWeighting(rarity, weight);
                }

                specificEntityChances.put(type, chance);

            }

        }

        // Shiny Chances
        this.shinyChance = configuration.getDouble("chances.shiny", 0.01);
    }

    // Settings are never updated through code
    @Override
    public boolean saveData(YamlConfiguration configuration) throws Exception {
        return false;
    }

    public int getSaveTicks() {
        return saveTicks;
    }

    public Map<CardSource, WeightedChance<CardRarity>> getCardChances() {
        return cardChances;
    }

    public CardSource getSource(EntityType type) {
        return cardSources.getOrDefault(type, CardSource.INVALID);
    }

    public boolean useSpecificChances() {
        return specificChances;
    }

    public Map<EntityType, WeightedChance<CardRarity>> getEntityChances() {
        return specificEntityChances;
    }

    public Map<String, BoosterPack> getBoosterPacks() {
        return boosterPacks;
    }

    public double getShinyChances() {
        return shinyChance;
    }
}
