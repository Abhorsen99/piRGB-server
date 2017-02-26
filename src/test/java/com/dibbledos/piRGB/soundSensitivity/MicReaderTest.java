package com.dibbledos.piRGB.soundSensitivity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sound.sampled.LineUnavailableException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class MicReaderTest {
    @Mock
    private Microphone mic;

    private MicReader reader;

    @BeforeEach
    void setUp() throws LineUnavailableException {
        MockitoAnnotations.initMocks(this);
        reader = new MicReader(mic);
    }

    @Test
    void testThatMicrophoneInitializedProperlyOnConstruction() throws LineUnavailableException {
        InOrder order = inOrder(mic);
        order.verify(mic).setFormat(any());
        order.verify(mic).open();
        order.verify(mic).start();
    }

    @Test
    void testGetLevelOnSilence() {
        byte[] samples = new byte[reader.SAMPLES_PER_SECOND / reader.AVERAGES_CALCULATED_PER_SECOND];
        Arrays.fill(samples, (byte) 0);
        when(mic.read(0, samples.length)).thenReturn(samples);
        assertEquals(0, reader.getCurrentLevelPercent());
        assertEquals(0, reader.getLevelScalingNumber());
    }

    @Test
    void testGetLevelOnMaxLoudness() {
        byte[] samples = new byte[reader.SAMPLES_PER_SECOND / reader.AVERAGES_CALCULATED_PER_SECOND];
        Arrays.fill(samples, Byte.MAX_VALUE);
        when(mic.read(0, samples.length)).thenReturn(samples);
        assertEquals(100, reader.getCurrentLevelPercent(), 0.1);
        assertEquals(1, reader.getLevelScalingNumber(), 0.001);
    }
}