package me.scyphers.fruitservers.tradingcards.cards;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChanceProvider {

    private final Map<CardRarity, Integer> chanceMap;

    private final int chance, dropChanceWeight;

    private int totalRarityWeight;

    public ChanceProvider(int chance, int dropChanceWeight) {
        this.chanceMap = new HashMap<>();
        this.chance = chance;
        this.dropChanceWeight = dropChanceWeight;
    }

    public void addRarityWeighting(CardRarity rarity, int weight) {
        chanceMap.put(rarity, weight);
        this.calculateTotalWeight();
    }

    private void calculateTotalWeight() {
        this.totalRarityWeight = 0;
        chanceMap.values().forEach(integer -> this.totalRarityWeight += integer);
    }

    public CardRarity getRandomRarity(Random random) {
        int roll = random.nextInt(totalRarityWeight);
        return this.getRarity(roll);
    }

    public CardRarity getRarity(int weight) {
        if (weight > totalRarityWeight) throw new IllegalStateException("Cannot get rarity for a weight higher than " + (totalRarityWeight - 1));
        for (CardRarity rarity : chanceMap.keySet()) {
            int rarityWeight = chanceMap.get(rarity);
            if (rarityWeight == 0) continue;
            weight -= rarityWeight;
            if (weight <= 0) return rarity;
        }
        throw new IllegalStateException("Illegal weight limit was allowed");
    }

    public boolean checkDropChance(Random random) {
        int roll = random.nextInt(dropChanceWeight) + 1;
        return roll < chance;
    }
}
