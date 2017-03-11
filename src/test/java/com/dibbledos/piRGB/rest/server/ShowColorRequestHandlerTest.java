package com.dibbledos.piRGB.rest.server;

import com.dibbledos.piRGB.rest.entities.Color;
import com.dibbledos.piRGB.rest.entities.ShowColorRequest;
import com.dibbledos.piRGB.rest.entities.SoundProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.verify;

class ShowColorRequestHandlerTest extends HandlerBaseTest{
    private SoundProperties soundDisabled;
    private SoundProperties soundEnabled;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        handler = new ShowColorRequestHandler(controller);
        soundDisabled = new SoundProperties();
        soundEnabled = new SoundProperties(true, 70);
    }

    @Test
    void testThatColorIsPassedUnmodifiedWhenColorRequested() throws IOException {
        Color color = new Color(231, 213, 9, 14); //values not meaningful
        ShowColorRequest request = new ShowColorRequest();
        request.setColor(color);
        handler.processRequest(convertRequestToJsonNode(request));
        verify(controller).showColor(color, soundDisabled);
    }

    @Test
    void testThatColorIsPassedUnmodifiedWhenColorFadeRequested() throws IOException {
        Color color = new Color(231, 213, 9, 14); //values not meaningful
        ShowColorRequest request = new ShowColorRequest();
        request.setColor(color);
        request.setFade(true);
        handler.processRequest(convertRequestToJsonNode(request));
        verify(controller).fadeTo(color, soundDisabled);
    }

    @Test
    void testThatSoundSensitiveIsPassedThroughForShowColorNoFade() throws IOException {
        ShowColorRequest request = new ShowColorRequest();
        request.setSoundSensitivity(soundEnabled);
        handler.processRequest(convertRequestToJsonNode(request));
        verify(controller).showColor(request.getColor(), soundEnabled);
    }

    @Test
    void testThatSoundSensitiveIsPassedThroughForShowColorFade() throws IOException {
        ShowColorRequest request = new ShowColorRequest();
        request.setSoundSensitivity(soundEnabled);
        request.setFade(true);
        handler.processRequest(convertRequestToJsonNode(request));
        verify(controller).fadeTo(request.getColor(), soundEnabled);
    }
}