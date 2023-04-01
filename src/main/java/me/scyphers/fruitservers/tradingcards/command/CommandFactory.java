package me.scyphers.fruitservers.tradingcards.command;

import me.scyphers.fruitservers.tradingcards.TradingCards;
import me.scyphers.fruitservers.tradingcards.api.Messenger;
import me.scyphers.fruitservers.tradingcards.command.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandFactory implements TabExecutor {

    private final TradingCards plugin;

    private final Messenger m;

    private final Map<String, BaseCommand> commands;

    public CommandFactory(TradingCards plugin) {
        this.plugin = plugin;
        this.m = plugin.getMessenger();

        // Weird text alignment is to make permissions align
        this.commands = Map.of(
                "toggle",         new ToggleCommand    (plugin, "tradingcards.commands.toggle"),
                "givecard",       new GiveCardCommand  (plugin, "tradingcards.commands.givecard"),
                "givepack",       new GivePackCommand  (plugin, "tradingcards.commands.givepack"),
                "list",           new ListCommand      (plugin, "tradingcards.commands.list"),
                "giverandomcard", new RandomCardCommand(plugin, "tradingcards.commands.giverandomcard"),
                "reload",         new ReloadCommand    (plugin, "tradingcards.commands.reload")
        );
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // Splash text
        if (args.length == 0) {
            for (String line : plugin.getSplashText()) {
                m.send(sender, line);
            }
            return true;
        }

        BaseCommand baseCommand = commands.get(args[0]);

        if (baseCommand == null) {
            m.msg(sender, "errorMessages.invalidCommand");
            return true;
        }

        return baseCommand.onBaseCommand(sender, args);

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (!sender.hasPermission("tradingcards.commands")) return Collections.emptyList();

        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            for (String commandName : commands.keySet()) {
                BaseCommand baseCommand = commands.get(commandName);
                if (sender.hasPermission(baseCommand.getPermission())) list.add(commandName);
            }
            return list;
        }

        BaseCommand baseCommand = commands.get(args[0]);
        if (baseCommand == null) return Collections.emptyList();

        return baseCommand.onBaseTabComplete(sender, args);

    }
}
