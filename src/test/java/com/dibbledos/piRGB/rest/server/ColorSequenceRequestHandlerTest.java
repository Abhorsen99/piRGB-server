package com.dibbledos.piRGB.rest.server;

import com.dibbledos.piRGB.rest.entities.Color;
import com.dibbledos.piRGB.rest.entities.SequenceRequest;
import com.dibbledos.piRGB.rest.entities.SoundProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

class ColorSequenceRequestHandlerTest extends HandlerBaseTest {
    private SequenceRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        handler = new ColorSequenceRequestHandler(controller);
        request = new SequenceRequest();
    }

    @Test
    void testCorrectControllerCalledFadingSequence() throws IOException {
        request.setFade(true);
        callProcess();
        verify(controller).fadeSequence(request.getSequence(), request.getInterval(), request.getSoundSensitivity());
    }

    @Test
    void testCorrectControllerCalledNotFadingSequence() throws IOException {
        callProcess();
        verify(controller).showSequence(request.getSequence(), request.getInterval(), request.getSoundSensitivity());
    }

    @Test
    void testSoundSensitivePassedWhenFadingSequence() throws IOException {
        SoundProperties soundEnabled = new SoundProperties(true, 70);
        request.setSoundSensitivity(soundEnabled);
        request.setFade(true);
        callProcess();
        verify(controller).fadeSequence(request.getSequence(), request.getInterval(), soundEnabled);
    }

    @Test
    void testSoundSensitivePassedNotFading() throws IOException {
        SoundProperties soundEnabled = new SoundProperties(true, 70);
        request.setSoundSensitivity(soundEnabled);
        callProcess();
        verify(controller).showSequence(request.getSequence(), request.getInterval(), soundEnabled);
    }

    @Test
    void testIntervalPassedWhenFading() throws IOException {
        int expectedInterval = 13;
        request.setInterval(expectedInterval);
        request.setFade(true);
        callProcess();
        verify(controller).fadeSequence(request.getSequence(), expectedInterval, request.getSoundSensitivity());
    }

    @Test
    void testIntervalPassedNotFading() throws IOException {
        int expectedInterval = 13;
        request.setInterval(expectedInterval);
        callProcess();
        verify(controller).showSequence(request.getSequence(), expectedInterval, request.getSoundSensitivity());
    }

    @Test
    void testSequencePassedUnmodifiedWhenFading() throws IOException {
        List<Color> expectedColors = new ArrayList<>();
        expectedColors.add(new Color(1, 2, 3, 4));
        expectedColors.add(new Color(4, 3, 2, 1));
        expectedColors.add(new Color(255, 255, 255, 100));
        request.setSequence(expectedColors);
        request.setFade(true);
        callProcess();
        verify(controller).fadeSequence(expectedColors, request.getInterval(), request.getSoundSensitivity());
    }

    @Test
    void testSequencePassedUnmodifiedWhenNotFading() throws IOException {
        List<Color> expectedColors = new ArrayList<>();
        expectedColors.add(new Color(1, 2, 3, 4));
        expectedColors.add(new Color(4, 3, 2, 1));
        expectedColors.add(new Color(255, 255, 255, 100));
        request.setSequence(expectedColors);
        callProcess();
        verify(controller).showSequence(expectedColors, request.getInterval(), request.getSoundSensitivity());
    }

    private void callProcess() throws IOException {
        handler.processRequest(convertRequestToJsonNode(request));
        try {
            //TODO rewrite with callback to avoid having to do this
            Thread.sleep(25);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}