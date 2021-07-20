package me.scyphers.fruitservers.tradingcards.config;

import me.scyphers.fruitservers.tradingcards.cards.Card;
import me.scyphers.fruitservers.tradingcards.cards.CardRarity;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class CardsDataFile extends ConfigFile {

    private Map<CardRarity, List<Card>> cards;

    public CardsDataFile(ConfigManager manager) {
        super(manager, "cards.yml", true);
    }

    @Override
    public void load(YamlConfiguration configuration) throws Exception {
        this.cards = new HashMap<>();
        ConfigurationSection cardsSection = configuration.getConfigurationSection("Cards");

        if (cardsSection == null) throw new IllegalArgumentException("Cannot find cards config section");

        for (String rarityKey : cardsSection.getKeys(false)) {
            ConfigurationSection raritySection = cardsSection.getConfigurationSection(rarityKey);
            if (raritySection == null) throw new IllegalStateException("Could not find card rarity section for rarity " + rarityKey);

            CardRarity rarity = CardRarity.valueOf(rarityKey.toUpperCase(Locale.ROOT));

            List<Card> cardList = new ArrayList<>();

            for (String cardNameKey : raritySection.getKeys(false)) {

                ConfigurationSection individualCardSection = raritySection.getConfigurationSection(cardNameKey);
                if (individualCardSection == null) throw new IllegalStateException("Could not find card " + cardNameKey + " for rarity " + rarityKey);

                // workaround for having . in card names
                String cardName = cardNameKey.replaceAll("\\(\\*\\)", ".");

                String series = individualCardSection.getString("Series", "INVALID_SERIES");
                String type = individualCardSection.getString("Type", "INVALID_TYPE");
                boolean canBeShiny = individualCardSection.getBoolean("Has-Shiny-Version", true);
                String description = individualCardSection.getString("Info", "INVALID_DESCRIPTION");

                Card card = new Card(cardName, type, description, series, rarity, canBeShiny);
                cardList.add(card);

            }

            cards.put(rarity, cardList);
        }
    }

    @Override
    public boolean saveData(YamlConfiguration configuration) throws Exception {
        // file is a read only
        return false;
    }

    public Map<CardRarity, List<Card>> getCards() {
        return cards;
    }
}
