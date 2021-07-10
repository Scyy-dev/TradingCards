package me.scyphers.plugins.pluginname.gui;

import me.scyphers.plugins.pluginname.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class InventoryGUI implements InventoryHolder, GUI<InventoryClickEvent> {

    /**
     * The GUI that was open before this one
     */
    protected final GUI<?> lastGUI;

    /**
     * The plugin class
     */
    protected final Plugin plugin;

    /**
     * The player viewing this inventory
     */
    protected final Player player;

    /**
     * The array of items in the inventory
     */
    protected ItemStack[] inventoryItems;

    /**
     * The inventory to be displayed to the player
     */
    protected final Inventory inventory;

    /**
     * Name of the GUI, displayed at the top of the inventory
     */
    protected final String name;

    /**
     * If the GUI should close on the next tick instead of reopening a new GUI. Change this in {@link }
     */
    protected boolean shouldClose;

    /**
     * Size of the GUI, also found from {@code inventoryItems.length}
     */
    protected final int size;

    /**
     * @param lastGUI The GUI that was open before this one, or <code>null</code> if opened for the first time
     * @param plugin  The main plugin instance
     * @param player  The player that this GUI is being presented to
     * @param name    Name of the GUI to be displayed
     */
    public InventoryGUI(@Nullable GUI<?> lastGUI, @NotNull Plugin plugin, @NotNull Player player, @NotNull String name, int size) {
        this.lastGUI = lastGUI;
        this.plugin = plugin;
        this.player = player;
        this.name = name;
        this.size = size;
        this.inventory = Bukkit.createInventory(null, size, name);
        this.inventoryItems = inventory.getContents();
    }

    @Override
    public abstract @NotNull GUI<?> handleInteraction(InventoryClickEvent event);

    @Override
    public void open(Player player) {
        inventory.setContents(inventoryItems);
        player.openInventory(inventory);
    }

    /**
     * Utility method for saving time when registering listeners for the GUI.<br>
     * All subclasses of {@link InventoryGUI} will use this listener for triggering their interaction handlers
     * @return the listener for this GUI and all GUI subclasses for it
     */
    public static Listener getListener() {
        return new InventoryListener();
    }

    @Override
    public @Nullable GUI<?> getLastGUI() {
        return lastGUI;
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public ItemStack[] getInventoryItems() {
        return inventoryItems;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean shouldClose() {
        return shouldClose;
    }

    /**
     * A check if the player can perform actions in their inventory
     * @return if the player can perform actions in their inventory
     */
    public abstract boolean allowPlayerInventoryEdits();

    private static class InventoryListener implements Listener {
        @EventHandler(priority = EventPriority.HIGHEST)
        public void onInventoryClickEvent(InventoryClickEvent event) {
            // Verify if the inventory interacted with was an InventoryGUI
            // If the inventory interacted with is not a valid GUI then we do not handle this event
            if (!(event.getView().getTopInventory().getHolder() instanceof InventoryGUI)) return;
            InventoryGUI oldGUI = (InventoryGUI) event.getView().getTopInventory().getHolder();

            // Check if the inventory allows player inventory edits, and if so, cancel the interaction
            if (!oldGUI.allowPlayerInventoryEdits() && event.getClickedInventory() instanceof PlayerInventory) {
                event.setCancelled(true);
                return;
            }

            // If the new GUI should close instead of trying to handle new interactions
            if (oldGUI.shouldClose()) {
                Bukkit.getScheduler().runTask(oldGUI.plugin, () -> event.getWhoClicked().closeInventory());
                return;
            }

            // Handle the interact event and open the new inventory
            GUI<?> newGUI = oldGUI.handleInteraction(event);
            Bukkit.getScheduler().runTask(oldGUI.plugin, () -> newGUI.open(oldGUI.getPlayer()));
        }
    }

}
