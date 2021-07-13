package me.scyphers.fruitservers.tradingcards.cards;

import me.scyphers.fruitservers.tradingcards.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardBuilder {

    private static final String NAME_PREFIX = "&e&l[&6&lCARD&e&l] ";
    private static final String TYPE_PREFIX = "&bType: &f";
    private static final String DESCRIPTION_LINE = "&eInfo:";
    private static final String DESCRIPTION_PREFIX = " &7- &f";
    private static final String SERIES_PREFIX = "&aSeries: &f";

    public static ItemStack construct(String cardName, String type, String description, int lineLength, String series, CardRarity rarity, boolean shiny) {

        String formattedCardName = cardName.replaceAll("_", " ");

        ItemBuilder cardBuilder = new ItemBuilder(Material.PAPER).amount(1)
                .name(shiny ? NAME_PREFIX + rarity.getColour() + "Shiny " + formattedCardName : NAME_PREFIX + rarity.getColour() + formattedCardName)
                .lore(TYPE_PREFIX + type)
                .lore(DESCRIPTION_LINE)
                .lore(formatDescription(description, lineLength))
                .lore(SERIES_PREFIX + series)
                .lore(shiny ? rarity.getColour() + "&lShiny " + rarity.getDisplay() : rarity.getColour() + "&l" + rarity.getDisplay())
                .flag(ItemFlag.HIDE_ENCHANTS);

        // Shiny
        if (shiny) {
            cardBuilder.enchant(Enchantment.ARROW_INFINITE, 10).flag(ItemFlag.HIDE_ENCHANTS);
        }

        return cardBuilder.build();

    }

    private static List<String> formatDescription(String description, int lineLength) {
        String[] words = description.split(" ");
        List<String> list = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder(DESCRIPTION_PREFIX);

        for (String word : words) {
            currentLine.append(word);
            if (currentLine.length() > lineLength) {
                list.add(currentLine.toString());
                currentLine = new StringBuilder(DESCRIPTION_PREFIX);
            } else {
                currentLine.append(" ");
            }
        }

        if (!currentLine.toString().equals(DESCRIPTION_PREFIX)) {
            list.add(currentLine.toString());
        }

        return list;

    }

}
