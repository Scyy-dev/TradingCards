package me.scyphers.fruitservers.tradingcards.cards;

import me.scyphers.fruitservers.tradingcards.WeightedChance;
import org.bukkit.entity.EntityType;

import java.util.*;
import java.util.stream.Collectors;

public class CardGenerator {

    private final Random random;

    // Card List
    private final Map<CardRarity, List<Card>> cards;

    // Rarity chance
    private final Map<CardSource, WeightedChance<CardRarity>> cardChances;
    private final Map<EntityType, WeightedChance<CardRarity>> entityChances;

    private final double shinyChance;

    public CardGenerator(Map<CardRarity, List<Card>> cards, Map<CardSource, WeightedChance<CardRarity>> cardChances,
                         Map<EntityType, WeightedChance<CardRarity>> entityChances, double shinyChance) {
        this.cards = cards;
        this.random = new Random();
        this.cardChances = cardChances;
        this.entityChances = entityChances;
        this.shinyChance = shinyChance;
    }

    public boolean tryDropCard(EntityType type, CardSource source) {
        if (entityChances.containsKey(type)) return entityChances.get(type).checkDropChance(random);
        if (source == CardSource.INVALID) return false;
        return cardChances.get(source).checkDropChance(random);
    }

    public boolean checkShiny() {
        double roll = random.nextDouble();
        return roll < shinyChance;
    }

    public Card generateCard(EntityType type, CardSource source) {
        if (entityChances.containsKey(type)) {
            WeightedChance<CardRarity> chanceMap = entityChances.get(type);
            CardRarity rarity = chanceMap.getRandomEnum(random);
            return getRandomCard(rarity);
        } else {
            if (source == CardSource.INVALID) throw new IllegalStateException("Cannot generate card for invalid type");
            WeightedChance<CardRarity> chanceMap = cardChances.get(source);
            CardRarity rarity = chanceMap.getRandomEnum(random);
            return getRandomCard(rarity);
        }
    }

    public Card getRandomCard(CardRarity rarity) {
        List<Card> cardList = cards.get(rarity);
        return cardList.get(random.nextInt(cardList.size()));
    }

    public Card getCard(CardRarity rarity, String cardName) {
        if (!cards.containsKey(rarity)) return Card.invalidCard();
        return cards.get(rarity).stream().filter(card -> card.name().equalsIgnoreCase(cardName)).findFirst().orElse(Card.invalidCard());
    }

    public List<String> getCardNames(CardRarity rarity) {
        if (!cards.containsKey(rarity)) return Collections.emptyList();
        return cards.get(rarity).stream().map(card -> card.name().toLowerCase(Locale.ROOT)).collect(Collectors.toList());
    }

    public Map<CardRarity, List<Card>> getCards() {
        return cards;
    }


}
