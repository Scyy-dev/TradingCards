package me.scyphers.plugins.pluginname.command;

import me.scyphers.plugins.pluginname.api.Messenger;
import me.scyphers.plugins.pluginname.TradingCards;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdminCommand implements TabExecutor {

    private final TradingCards plugin;

    private final Messenger pm;

    public AdminCommand(TradingCards plugin) {
        this.plugin = plugin;
        this.pm = plugin.getMessenger();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 0) {
            for (String message : plugin.getSplashText()) {
                sender.sendMessage(message);
                return true;
            }
        }

        // IDE flags this with a warning for not enough case statements - ignored due to more commands expected when plugin is created
        switch (args[0]) {
            case "reload":
                if (!sender.hasPermission("tradingcards.commands.reload")) {
                    pm.msg(sender, "errorMessages.noPermission"); return true;
                }
                plugin.reload(sender);
                return true;
            default:
                pm.msg(sender, "errorMessages.invalidCommand");
                return true;
        }

    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        List<String> list = new ArrayList<>();

        // IDE flags this with a warning for not enough case statements - ignored due to more commands expected when plugin is created
        switch (args.length) {
            case 1:
                if (sender.hasPermission("plugin.admin.reload")) list.add("reload");
                return list;
            default:
                return list;
        }
    }
}
