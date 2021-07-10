package me.scyphers.plugins.pluginname.cards;

public enum CardRarity {

    COMMON("&7", "Common"),
    UNCOMMON("&a", "Uncommon"),
    RARE("&9", "Rare"),
    VERY_RARE("&5", "Very Rare"),
    LEGENDARY("&6", "Legendary");

    private final String colour;
    private final String display;

    CardRarity(String colour, String display) {
        this.colour = colour;
        this.display = display;
    }

    public String getColour() {
        return colour;
    }

    public String getDisplay() {
        return display;
    }
}
