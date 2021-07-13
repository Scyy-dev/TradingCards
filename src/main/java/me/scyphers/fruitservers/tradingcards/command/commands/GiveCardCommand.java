package me.scyphers.fruitservers.tradingcards.command.commands;

import me.scyphers.fruitservers.tradingcards.TradingCards;
import me.scyphers.fruitservers.tradingcards.cards.Card;
import me.scyphers.fruitservers.tradingcards.cards.CardRarity;
import me.scyphers.fruitservers.tradingcards.command.BaseCommand;
import me.scyphers.fruitservers.tradingcards.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class GiveCardCommand extends BaseCommand {

    public GiveCardCommand(TradingCards plugin, String permission) {
        super(plugin, permission, 5);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {

        // Player
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            m.msg(sender, "errorMessages.playerNotFound", "%player%", args[1]); return true;
        }

        // Rarity
        CardRarity rarity;
        try {
            rarity = CardRarity.valueOf(args[2].toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            m.msg(sender, "cardMessages.invalidCardRarity"); return true;
        }

        // Card Name
        Card card = getPlugin().getGenerator().getCard(rarity, args[3]);
        if (card.isInvalidCard()) {
            m.msg(sender, "cardMessages.invalidCardName", "%cardname%", args[3]); return true;
        }

        // Item Amount
        int amount;
        try {
            amount = Integer.parseInt(args[4]);
            if (amount < 1 || amount > 64) {
                m.msg(sender, "errorMessages.invalidAmountRange"); return true;
            }
        } catch (Exception e) {
            m.msg(sender, "errorMessages.mustBeNumber"); return true;
        }

        // Shiny
        boolean shiny = args.length >= 6 && args[5].equalsIgnoreCase("shiny");

        // Construct the card
        ItemStack cardItem = card.asItem(shiny);
        cardItem.setAmount(amount);

        PlayerUtils.give(player, cardItem);

        // Send a message
        m.msg(sender, "cardMessages.receivedCard", "%player%", player.getName(), "%cardname%", args[3]);
        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        switch (args.length) {
            case 2:
                return null;
            case 3:
                return Arrays.stream(CardRarity.values()).map(Enum::toString).collect(Collectors.toList());
            case 4:
                CardRarity rarity;
                try {
                    rarity = CardRarity.valueOf(args[2]);
                    return getPlugin().getGenerator().getCardNames(rarity);
                } catch (Exception e) {
                    return Collections.emptyList();
                }
            case 5:
                return itemAmountList();
            case 6:
                return Arrays.asList("shiny", "not-shiny");
            default:
                return Collections.emptyList();
        }
    }
}
