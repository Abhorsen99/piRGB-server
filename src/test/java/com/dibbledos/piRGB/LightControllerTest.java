package com.dibbledos.piRGB;

import com.dibbledos.piRGB.lightSystems.LightSystem;
import com.dibbledos.piRGB.rest.entities.Color;
import com.dibbledos.piRGB.rest.entities.SoundProperties;
import com.dibbledos.piRGB.soundSensitivity.MicReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import sun.plugin.dom.exception.InvalidStateException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LightControllerTest {
    private SoundProperties soundDisabled;
    private SoundProperties soundEnabled;

    @Mock
    private LightSystem lightSystem;
    @Mock
    private MicReader reader;

    @InjectMocks
    private TestLightController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        soundDisabled = new SoundProperties();
        soundEnabled = new SoundProperties(true, 70);
    }

    @Test
    void testThatShowColorSetsColorForEachPin() {
        controller.showColor(new Color(0, 0, 0, 0), soundDisabled);
        verify(lightSystem).setPinPercentage(ColorPin.BLUE, 0);
        verify(lightSystem).setPinPercentage(ColorPin.GREEN, 0);
        verify(lightSystem).setPinPercentage(ColorPin.RED, 0);
    }

    @Test
    void testThatShowColorSetsCurrentColorToRequested() {
        Color expected = new Color(1, 2, 3, 4);
        controller.showColor(expected, soundDisabled);
        assertEquals(expected, controller.currentColor);
    }

    @Test
    void testThatSequenceResetWhenShowColorCalled() {
        Color expected = new Color(1, 2, 3, 4);
        controller.showColor(expected, soundDisabled);
        assertTrue(controller.wasResetCalled);
    }

    @Test
    void testThatMicProcessingCalledWhenShowColorCalled() {
        Color expected = new Color(1, 2, 3, 4);
        controller.showColor(expected, soundDisabled);
        assertTrue(controller.wasMicProcessingCalled);
    }

    @Test
    void testThatSoundSensitiveFlagSetWhenShowColorCalled() {
        Color expected = new Color(1, 2, 3, 4);
        controller.showColor(expected, soundEnabled);
        assertTrue(controller.soundSensitivity.isEnabled());
        controller.showColor(expected, soundDisabled);
        assertFalse(controller.soundSensitivity.isEnabled());
    }

    @Test
    void testThatShowColorRespectsColorMagnitude0() {
        controller.showColor(new Color(255, 255, 255, 0), soundDisabled);
        verify(lightSystem).setPinPercentage(ColorPin.BLUE, 0);
        verify(lightSystem).setPinPercentage(ColorPin.GREEN, 0);
        verify(lightSystem).setPinPercentage(ColorPin.RED, 0);
    }

    @Test
    void testThatShowColorRespectsColorMagnitude100() {
        controller.showColor(new Color(255, 255, 255, 100), soundDisabled);
        verify(lightSystem).setPinPercentage(ColorPin.BLUE, 100);
        verify(lightSystem).setPinPercentage(ColorPin.GREEN, 100);
        verify(lightSystem).setPinPercentage(ColorPin.RED, 100);
    }

    @Test
    void testThatShowColorRespectsColorMagnitude50() {
        controller.showColor(new Color(255, 255, 255, 50), soundDisabled);
        verify(lightSystem).setPinPercentage(ColorPin.BLUE, 50);
        verify(lightSystem).setPinPercentage(ColorPin.GREEN, 50);
        verify(lightSystem).setPinPercentage(ColorPin.RED, 50);
    }

    @Test
    void testThatMagnitudeAdjustmentIsAppliedToEachColorPin() {
        Color requested = new Color(100, 200, 150, 50);
        controller.showColor(requested, soundDisabled);
        verify(lightSystem).setPinPercentage(ColorPin.BLUE,  controller.adjustForMagnitude(requested.getBluePercent(), requested.getMagnitude()));
        verify(lightSystem).setPinPercentage(ColorPin.GREEN, controller.adjustForMagnitude(requested.getGreenPercent(), requested.getMagnitude()));
        verify(lightSystem).setPinPercentage(ColorPin.RED, controller.adjustForMagnitude(requested.getRedPercent(), requested.getMagnitude()));
    }

    @Test
    void testThatAdjustMagnitudeAdjustsCorrectly() {
        assertEquals(12, controller.adjustForMagnitude(24, 50));
        assertEquals(100, controller.adjustForMagnitude(100, 100));
        assertEquals(30, controller.adjustForMagnitude(59, 50));
    }

    @Test
    void testThatResetSetsSequenceAndSoundFlagsToFalse() {
        controller.shouldContinueSequence = true;
        controller.soundSensitivity.setEnabled(true);
        controller.resetLights();
        assertFalse(controller.soundSensitivity.isEnabled());
        assertFalse(controller.shouldContinueSequence);
    }

    @Test
    void testSoundSensitiveLightsNeverGoBelow10() throws InterruptedException {
        controller.currentColor = new Color(255, 255, 255, 100);

        doThrow(new InvalidStateException("Level too low")).when(lightSystem).setPinPercentage(any(), Matchers.intThat(new Below10()));
        Random rng = new Random();
        for(int i = 0; i < 10000; i++) {
            double micInput = rng.nextDouble();
            System.out.println("Level " + micInput);
            controller.updateForSoundLevel(micInput);
        }
    }

    @Test
    void testSoundSensitiveLightsGives10MagnitudeForNoSound() throws InterruptedException {
        controller.currentColor = new Color(255, 255, 255, 100);
        double micInput = 0;
        controller.updateForSoundLevel(micInput);
        verify(lightSystem, times(3)).setPinPercentage(any(), eq(10));
    }

    @Test
    void testSettingSoundThresholdTo100() {
        controller.currentColor = new Color(255, 255, 255, 100);
        controller.soundSensitivity = new SoundProperties(true, 100);
        double micInput = 1;
        controller.updateForSoundLevel(micInput);
        verify(lightSystem, times(3)).setPinPercentage(any(), eq(10));
    }

    @Test
    void testSettingSoundThresholdTo50() {
        controller.currentColor = new Color(255, 255, 255, 100);
        controller.soundSensitivity = new SoundProperties(true, 50);
        double micInput = .50;
        controller.updateForSoundLevel(micInput);
        verify(lightSystem, times(3)).setPinPercentage(any(), eq(10));

        micInput = .51;
        controller.updateForSoundLevel(micInput);
        verify(lightSystem, times(3)).setPinPercentage(any(), eq(12));
    }

    class Below10 implements ArgumentMatcher<Integer>{
        @Override
        public boolean matches(Integer argument) {
            if(argument < 10){
                System.out.println("Was executed with: " + argument);
                return true;
            }
            return false;
        }
    }

    public static class TestLightController extends LightController{
        public boolean wasResetCalled;
        public boolean wasMicProcessingCalled;

        public TestLightController(LightSystem lightSystem, MicReader micReader) {
            super(lightSystem, micReader);
        }

        @Override
        protected void resetLights() {
            wasResetCalled = true;
            super.resetLights();
        }

        @Override
        protected void startMicProcessing() {
            wasMicProcessingCalled = true;
            super.startMicProcessing();
        }
    }
}