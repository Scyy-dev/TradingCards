package me.scyphers.fruitservers.tradingcards.cards;

import java.util.*;
import java.util.stream.Collectors;

public class CardGenerator {

    private final Random random;

    // Card List
    private final Map<CardRarity, List<Card>> cards;

    // Rarity chance
    private final Map<CardSource, ChanceProvider> cardChances;

    private final ChanceProvider shinyChance;

    public CardGenerator(Map<CardRarity, List<Card>> cards, Map<CardSource, ChanceProvider> cardChances, ChanceProvider shinyDropChance) {
        this.cards = cards;
        this.random = new Random();
        this.cardChances = cardChances;
        this.shinyChance = shinyDropChance;
    }

    public boolean checkCardDrop(CardSource source) {
        if (source == CardSource.INVALID) return false;
        return cardChances.get(source).checkDropChance(random);
    }

    public boolean checkShiny() {
        return shinyChance.checkDropChance(random);
    }

    public Card generateCard(CardSource source) {
        if (source == CardSource.INVALID) throw new IllegalStateException("Cannot generate card for invalid source");
        ChanceProvider chanceMap = cardChances.get(source);
        CardRarity rarity = chanceMap.getRandomRarity(random);
        return getRandomCard(rarity);
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
