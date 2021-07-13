package me.scyphers.fruitservers.tradingcards.cards;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class CardGeneratorTest {

    private static CardGenerator emptyGenerator;

    @BeforeAll
    static void setup() {
        emptyGenerator = new CardGenerator(new HashMap<>(), new HashMap<>());
    }

    @Test
    public void getInvalidCardTest() {
        Card commonCard = emptyGenerator.getCard(CardRarity.COMMON, "intentionally bad card name");
        assertEquals(commonCard, Card.invalidCard());
        assertTrue(commonCard.isInvalidCard());
        Card notCommonCard = emptyGenerator.getCard(CardRarity.LEGENDARY, "intentionally bad card name");
        assertEquals(notCommonCard, Card.invalidCard());
        assertTrue(notCommonCard.isInvalidCard());
    }

}
