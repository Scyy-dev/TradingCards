package me.scyphers.fruitservers.tradingcards.command.commands;

import me.scyphers.fruitservers.tradingcards.TradingCards;
import me.scyphers.fruitservers.tradingcards.api.PlayerCardTrader;
import me.scyphers.fruitservers.tradingcards.command.PlayerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ToggleCommand extends PlayerCommand {

    public ToggleCommand(TradingCards plugin, String permission) {
        super(plugin, permission, 0);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(Player player, String[] args) {

        PlayerCardTrader cardTrader = getPlugin().getPlayerCardTrader(player.getUniqueId());

        boolean cardsEnabled = !cardTrader.isCardsEnabled();
        cardTrader.setCardsEnabled(cardsEnabled);

        String state = cardsEnabled ? "ENABLED" : "DISABLED";

        m.msg(player, "cardMessages.toggledCards", "%state%", state);

        return true;

    }
}
