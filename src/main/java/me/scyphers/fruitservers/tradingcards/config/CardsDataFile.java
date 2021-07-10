package me.scyphers.fruitservers.tradingcards.config;

import me.scyphers.fruitservers.tradingcards.cards.Card;
import me.scyphers.fruitservers.tradingcards.cards.CardRarity;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class CardsDataFile extends ConfigFile {

    private final Map<CardRarity, Set<Card>> cards;

    public CardsDataFile(ConfigManager manager) {
        super(manager, "cards.yml", true);
        this.cards = new HashMap<>();
    }

    @Override
    public void load(YamlConfiguration configuration) throws Exception {
        this.cards.clear();
        ConfigurationSection cardsSection = configuration.getConfigurationSection("Cards");

        if (cardsSection == null) throw new IllegalArgumentException("Cannot find cards config section");


        for (String rarityKey : cardsSection.getKeys(false)) {
            ConfigurationSection raritySection = cardsSection.getConfigurationSection(rarityKey);
            if (raritySection == null) throw new IllegalStateException("Could not find card rarity section for rarity " + rarityKey);

            CardRarity rarity = CardRarity.valueOf(rarityKey.toUpperCase(Locale.ROOT));

            Set<Card> cardList = new HashSet<>();

            for (String cardName : raritySection.getKeys(false)) {

                String series = raritySection.getString(cardName + ".Series", "INVALID_SERIES");
                String type = raritySection.getString(cardName + ".Type", "INVALID_TYPE");
                boolean canBeShiny = raritySection.getBoolean(cardName + ".Has-Shiny-Version", true);
                String description = raritySection.getString(cardName + ".Info", "INVALID_DESCRIPTION");


                Card card = new Card(cardName, type, description, series, rarity, canBeShiny);
                cardList.add(card);

            }

            cards.put(rarity, cardList);
        }
    }

    // No saving is done as this is is intended to be a read only object
    @Override
    public void save(YamlConfiguration configuration) throws Exception {

    }

    public Map<CardRarity, Set<Card>> getCards() {
        return cards;
    }
}
