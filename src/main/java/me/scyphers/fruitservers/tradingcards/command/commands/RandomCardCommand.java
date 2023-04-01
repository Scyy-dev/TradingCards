package me.scyphers.fruitservers.tradingcards.command.commands;

import me.scyphers.fruitservers.tradingcards.TradingCards;
import me.scyphers.fruitservers.tradingcards.cards.Card;
import me.scyphers.fruitservers.tradingcards.cards.CardRarity;
import me.scyphers.fruitservers.tradingcards.command.BaseCommand;
import me.scyphers.fruitservers.tradingcards.command.PlayerCommand;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RandomCardCommand extends BaseCommand {

    private final TradingCards plugin;

    public RandomCardCommand(TradingCards plugin, String permission) {
        super(plugin, permission, 1);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {

        CardRarity rarity;
        try {
            rarity = CardRarity.valueOf(args[1]);
        } catch (Exception e) {
            plugin.getMessenger().msg(sender, "cardMessages.invalidCardRarity");
            return true;
        }

        Player player = plugin.getServer().getPlayer(args[2]);
        if (player == null) {
            plugin.getMessenger().msg(sender, "errorMessages.playerNotFound", "%player%", args[1]);
            return true;
        }

        Card card = plugin.getGenerator().getRandomCard(rarity);
        ItemStack item = card.asItem(plugin.getGenerator().checkShiny());

        if (player.getInventory().addItem(item).size() != 0) {
            World world = player.getWorld();
            world.dropItemNaturally(player.getLocation(), item);
        }

        plugin.getMessenger().msg(sender, "cardMessages.receivedRandomCard", "%rarity%", rarity.getColour() + rarity.getDisplay());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return switch (args.length) {
            case 2 -> CardRarity.asCommandArguments();
            case 3 -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            default -> Collections.emptyList();
        };
    }
}
