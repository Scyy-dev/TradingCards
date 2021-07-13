package me.scyphers.fruitservers.tradingcards.event;

import me.scyphers.fruitservers.tradingcards.TradingCards;
import me.scyphers.fruitservers.tradingcards.cards.Card;
import me.scyphers.fruitservers.tradingcards.cards.CardGenerator;
import me.scyphers.fruitservers.tradingcards.cards.CardSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public record EventListener(TradingCards plugin) implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeathEvent(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        // Check cause
        CardSource source = CardSource.fromEntity(entity.getType());
        if (source == CardSource.INVALID) return;

        // Player killed the mob
        if (event.getFinalDamage() < entity.getHealth()) return;

        // TODO check if player has cards toggled
        if (!plugin.getPlayerCardTrader(player.getUniqueId()).isCardsEnabled()) return;

        CardGenerator generator = plugin.getGenerator();

        // Generate a card
        if (plugin.getGenerator().checkCardDrop(source)) {
            Card card = generator.generateCard(source);

            // TODO - implement shiny chance
            entity.getWorld().dropItem(entity.getLocation(), card.asItem(false));

        }

    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Load the player data file
        plugin.getPlayerDataManager().getPlayerDataFile(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Close the player data file
        plugin.getPlayerDataManager().closePlayerDataFile(player.getUniqueId());
    }

}
