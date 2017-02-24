package com.dibbledos.piRGB.rest.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {
    Color color;

    @BeforeEach
    void setUp() {
        color = new Color();
    }

    @Test
    void testColorDefaults() {
        assertEquals(0, color.get255Blue());
        assertEquals(0, color.get255Green());
        assertEquals(0, color.get255Red());
        assertEquals(0, color.getMagnitude());
        assertEquals(0, color.getBluePercent());
        assertEquals(0, color.getGreenPercent());
        assertEquals(0, color.getRedPercent());
    }

    @Test
    void test255IsOneHundredPercent() {
        color.set255Blue(255);
        color.set255Green(255);
        color.set255Red(255);

        assertEquals(100, color.getBluePercent());
        assertEquals(100, color.getGreenPercent());
        assertEquals(100, color.getRedPercent());
    }

    @Test
    void testTranslationOf255AtTwentyPercent() {
        color.set255Blue(51);
        color.set255Green(51);
        color.set255Red(51);

        assertEquals(20, color.getBluePercent());
        assertEquals(20, color.getGreenPercent());
        assertEquals(20, color.getRedPercent());
    }

    @Test
    void testCanGoFromPercentageTo255Value() {
        color.setPercentBlue(20);
        color.setPercentRed(20);
        color.setPercentGreen(20);

        assertEquals(51, color.get255Blue());
        assertEquals(51, color.get255Red());
        assertEquals(51, color.get255Green());
    }
}