package me.scyphers.fruitservers.tradingcards.cards;

import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public record Card(String name, String type, String description, String series,
                   CardRarity rarity, boolean canBeShiny) {

    public ItemStack asItem(boolean shiny) {
        return CardBuilder.construct(name, type, description, 25, series, rarity, shiny);
    }

    public static Card invalidCard() {
        return new Card("INVALID_CARD",
                "INVALID_TYPE",
                "INVALID_DESCRIPTION",
                "INVALID_SERIES",
                CardRarity.COMMON,
                false);
    }

    public boolean isInvalidCard() {
        return name.equals("INVALID_CARD") &&
                type.equals("INVALID_TYPE") &&
                description.equals("INVALID_DESCRIPTION") &&
                series.equals("INVALID_SERIES") &&
                rarity.equals(CardRarity.COMMON) &&
                !canBeShiny;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Card.class != o.getClass()) return false;
        Card card = (Card) o;
        return canBeShiny == card.canBeShiny && name.equals(card.name) && type.equals(card.type) && description.equals(card.description) && series.equals(card.series) && rarity == card.rarity;
    }

    public int hashCode() {
        return Objects.hash(name, type, description, series, rarity, canBeShiny);
    }
}
