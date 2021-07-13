package me.scyphers.fruitservers.tradingcards.command.commands;

import me.scyphers.fruitservers.tradingcards.TradingCards;
import me.scyphers.fruitservers.tradingcards.cards.BoosterPack;
import me.scyphers.fruitservers.tradingcards.cards.Card;
import me.scyphers.fruitservers.tradingcards.cards.CardRarity;
import me.scyphers.fruitservers.tradingcards.command.BaseCommand;
import me.scyphers.fruitservers.tradingcards.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GivePackCommand extends BaseCommand {

    public GivePackCommand(TradingCards plugin, String permission) {
        super(plugin, permission, 2);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {

        // Player
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            m.msg(sender, "errorMessages.playerNotFound", "%player%", args[1]); return true;
        }

        // Booster Pack
        BoosterPack boosterPack = getPlugin().getSettings().getBoosterPacks().get(args[2]);
        if (boosterPack == null) {
            m.msg(sender, "cardMessages.boosterPackNotFound", "%pack%", args[2]); return true;
        }

        // Create the Booster Pack
        List<ItemStack> cards = new ArrayList<>();
        for (CardRarity rarity : boosterPack.cardsGiven().keySet()) {
            int amount = boosterPack.getCardsGiven(rarity);
            for (int i = 0; i < amount; i++) {
                Card card = getPlugin().getGenerator().getRandomCard(rarity);
                cards.add(card.asItem(false));
            }
        }

        // Give the cards
        for (ItemStack card : cards) {
            PlayerUtils.give(player, card);
        }

        // Send a message
        m.msg(sender, "cardMessages.receivedBoosterPack", "%pack%", boosterPack.displayName());
        return true;


    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return switch (args.length) {
            case 2 -> null;
            case 3 -> new ArrayList<>(getPlugin().getSettings().getBoosterPacks().keySet());
            default -> Collections.emptyList();
        };
    }
}
