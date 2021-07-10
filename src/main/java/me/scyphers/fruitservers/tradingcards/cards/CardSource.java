package me.scyphers.fruitservers.tradingcards.cards;

import org.bukkit.entity.EntityType;

public enum CardSource {
    INVALID,
    PASSIVE,
    HOSTILE,
    BOSS;

    public static CardSource fromEntity(EntityType type) {
        return switch (type) {
            case ELDER_GUARDIAN, WITHER_SKELETON, STRAY, HUSK, ZOMBIE_VILLAGER, EVOKER, VEX, VINDICATOR, ILLUSIONER, CREEPER, SKELETON, SPIDER, ZOMBIE, SLIME, GHAST, ZOMBIFIED_PIGLIN, ENDERMAN, CAVE_SPIDER, SILVERFISH, BLAZE, MAGMA_CUBE, WITCH, ENDERMITE, GUARDIAN, SHULKER, PHANTOM, DROWNED, PILLAGER, RAVAGER, HOGLIN, PIGLIN, ZOGLIN, PIGLIN_BRUTE -> HOSTILE;
            case SKELETON_HORSE, ZOMBIE_HORSE, DONKEY, MULE, BAT, PIG, SHEEP, COW, CHICKEN, SQUID, WOLF, MUSHROOM_COW, SNOWMAN, OCELOT, IRON_GOLEM, HORSE, RABBIT, POLAR_BEAR, LLAMA, PARROT, VILLAGER, TURTLE, COD, SALMON, PUFFERFISH, TROPICAL_FISH, DOLPHIN, CAT, PANDA, TRADER_LLAMA, WANDERING_TRADER, FOX, BEE, STRIDER, AXOLOTL, GLOW_SQUID, GOAT -> PASSIVE;
            case ENDER_DRAGON, WITHER -> BOSS;
            default -> INVALID;
        };
    }

}
