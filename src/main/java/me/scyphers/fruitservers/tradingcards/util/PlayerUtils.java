package me.scyphers.fruitservers.tradingcards.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerUtils {

    public static void give(Player player, ItemStack itemStack) {
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(itemStack);
        } else {
            player.getLocation().getWorld().dropItem(player.getLocation(), itemStack);
        }
    }

}
