package me.scyphers.fruitservers.tradingcards.command.commands;

import me.scyphers.fruitservers.tradingcards.TradingCards;
import me.scyphers.fruitservers.tradingcards.cards.Card;
import me.scyphers.fruitservers.tradingcards.cards.CardRarity;
import me.scyphers.fruitservers.tradingcards.command.BaseCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ListCommand extends BaseCommand {

    public ListCommand(TradingCards plugin, String permission) {
        super(plugin, permission, 0);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {

        Map<CardRarity, List<Card>> cards = getPlugin().getGenerator().getCards();

        for (CardRarity rarity : CardRarity.inRarityOrder()) {

            m.send(sender, rarity.getColour() + rarity.getDisplay());

            StringBuilder builder = new StringBuilder("&f");

            for (Card card : cards.get(rarity)) {
                builder.append(card.name()).append("&8, &f");
            }

            builder.delete(builder.length() - 6, builder.length());
            m.send(sender, builder.toString());

        }

        return true;


    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
