package me.scyphers.fruitservers.tradingcards.event;

import me.scyphers.fruitservers.tradingcards.TradingCards;
import me.scyphers.fruitservers.tradingcards.cards.Card;
import me.scyphers.fruitservers.tradingcards.cards.CardGenerator;
import me.scyphers.fruitservers.tradingcards.cards.CardSource;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public record EventListener(TradingCards plugin) implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeathEvent(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        Player player;
        if (event.getDamager() instanceof Player damagee) {
            player = damagee;
        } else {
            if (!(event.getDamager() instanceof Projectile projectile)) return;
            if (!(projectile.getShooter() instanceof Player damagee)) return;
            player = damagee;
        }

        EntityType type = event.getEntityType();

        if (event.getFinalDamage() < entity.getHealth()) return;
        if (!plugin.getPlayerCardTrader(player.getUniqueId()).isCardsEnabled()) return;

        CardSource source = plugin.getSettings().getSource(type);
        CardGenerator generator = plugin.getGenerator();

        // Test the drop rate for this entity
        if (!plugin.getGenerator().tryDropCard(type, source)) return;

        // Generate and drop a card
        Card card = generator.generateCard(type, source);
        boolean shiny = generator.checkShiny();
        entity.getWorld().dropItem(entity.getLocation(), card.asItem(shiny));

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
