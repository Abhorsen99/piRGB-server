package com.dibbledos.piRGB.rest.server;

import com.dibbledos.piRGB.rest.entities.Color;
import com.dibbledos.piRGB.rest.entities.ShowColorRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.verify;

class ShowColorRequestHandlerTest extends HandlerBaseTest{
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        handler = new ShowColorRequestHandler(controller);
    }

    @Test
    void testThatColorIsPassedUnmodifiedWhenColorRequested() throws IOException {
        Color color = new Color(231, 213, 9, 14); //values not meaningful
        ShowColorRequest request = new ShowColorRequest();
        request.setColor(color);
        handler.processRequest(convertRequestToJsonNode(request));
        verify(controller).showColor(color, false);
    }

    @Test
    void testThatColorIsPassedUnmodifiedWhenColorFadeRequested() throws IOException {
        Color color = new Color(231, 213, 9, 14); //values not meaningful
        ShowColorRequest request = new ShowColorRequest();
        request.setColor(color);
        request.setFade(true);
        handler.processRequest(convertRequestToJsonNode(request));
        verify(controller).fadeTo(color, false);
    }

    @Test
    void testThatSoundSensitiveIsPassedThroughForShowColorNoFade() throws IOException {
        ShowColorRequest request = new ShowColorRequest();
        request.setSoundSensitive(true);
        handler.processRequest(convertRequestToJsonNode(request));
        verify(controller).showColor(request.getColor(), true);
    }

    @Test
    void testThatSoundSensitiveIsPassedThroughForShowColorFade() throws IOException {
        ShowColorRequest request = new ShowColorRequest();
        request.setSoundSensitive(true);
        request.setFade(true);
        handler.processRequest(convertRequestToJsonNode(request));
        verify(controller).fadeTo(request.getColor(), true);
    }
}