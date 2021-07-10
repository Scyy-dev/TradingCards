package me.scyphers.fruitservers.tradingcards.cards;

import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public record Card(String name, String type, String description, String series,
                   CardRarity rarity, boolean canBeShiny) {

    public ItemStack asItem() {
        return CardBuilder.construct(name, type, description, series, rarity, canBeShiny);
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
