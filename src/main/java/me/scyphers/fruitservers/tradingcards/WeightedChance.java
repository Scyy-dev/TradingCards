package me.scyphers.fruitservers.tradingcards;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WeightedChance<T> {

    private final Map<T, Integer> chanceMap;

    private final int chance, dropChanceWeight;

    private int totalRarityWeight;

    public WeightedChance(int chance, int dropChanceWeight) {
        this.chanceMap = new HashMap<>();
        this.chance = chance;
        this.dropChanceWeight = dropChanceWeight;
    }

    public void addRarityWeighting(T rarity, int weight) {
        chanceMap.put(rarity, weight);
        this.calculateTotalWeight();
    }

    private void calculateTotalWeight() {
        this.totalRarityWeight = 0;
        chanceMap.values().forEach(integer -> this.totalRarityWeight += integer);
    }

    public boolean hasChance(T object) {
        return chanceMap.containsKey(object);
    }

    public T getRandomEnum(Random random) {
        int roll = random.nextInt(totalRarityWeight) + 1;
        return this.getRarity(roll);
    }

    public T getRarity(int weight) {
        if (weight > totalRarityWeight) throw new IllegalStateException("Cannot get rarity for a weight higher than " + (totalRarityWeight - 1));
        for (T rarity : chanceMap.keySet()) {
            int rarityWeight = chanceMap.get(rarity);
            if (rarityWeight == 0) continue;
            weight -= rarityWeight;
            if (weight <= 0) return rarity;
        }
        throw new IllegalStateException("Illegal weight limit was allowed");
    }

    public boolean checkDropChance(Random random) {
        int roll = random.nextInt(dropChanceWeight);
        return roll < chance;
    }

}
