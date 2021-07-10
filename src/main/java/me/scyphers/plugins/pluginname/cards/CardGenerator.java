package me.scyphers.plugins.pluginname.cards;

import java.util.*;

public class CardGenerator {

    private final Map<CardRarity, List<Card>> cards;

    private final Map<CardSource, >

    private final Map<CardRarity, Integer> rarityWeights;



    private int totalWeight;

    private final Random random;

    public CardGenerator(Map<CardRarity, List<Card>> cards) {
        this.cards = cards;
        this.random = new Random();
        this.rarityWeights = new HashMap<>();
        this.totalWeight = 0;
    }

    public Card generateCard() {
        int roll = random.nextInt(totalWeight);
        CardRarity rarity = getRarity(roll);
        return getRandomCard(rarity);
    }

    public CardRarity getRarity(int weight) {
        if (weight > totalWeight) throw new IllegalStateException("Cannot get rarity for a weight higher than " + (totalWeight - 1));
        for (CardRarity rarity : rarityWeights.keySet()) {
            weight -= rarityWeights.get(rarity);
            if (weight <= 0) return rarity;
        }
        throw new IllegalStateException("Illegal weight limit was allowed");
    }

    public Card getRandomCard(CardRarity rarity) {
        List<Card> cardList = cards.get(rarity);
        return cardList.get(random.nextInt(cardList.size()));
    }

    public void addRarityWeighting(CardRarity rarity, int weight) {
        this.rarityWeights.put(rarity, weight);
        this.calculateTotalWeight();
    }

    private void calculateTotalWeight() {
        rarityWeights.values().forEach(integer -> this.totalWeight += integer);
    }


}
