package me.scyphers.fruitservers.tradingcards.cards;

import java.util.Map;

public record BoosterPack(String displayName, Map<CardRarity, Integer> cardsGiven) {

    public int getCardsGiven(CardRarity rarity) {
        return cardsGiven.getOrDefault(rarity, 0);
    }

}
