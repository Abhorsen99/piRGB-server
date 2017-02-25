package com.dibbledos.piRGB.rest.server;

import com.dibbledos.piRGB.rest.entities.OffRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

class OffColorRequestHandlerTest extends HandlerBaseTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        handler = new OffColorRequestHandler(controller);
    }

    @Test
    void testHandlesOffWithoutFadeCorrectly() throws IOException {
        OffRequest request = new OffRequest();
        request.setFade(false);
        handler.processRequest(convertRequestToJsonNode(request));
        Mockito.verify(controller).off();
    }

    @Test
    void testHandlesOffWithFadeCorrectly() throws IOException {
        OffRequest request = new OffRequest();
        request.setFade(true);
        handler.processRequest(convertRequestToJsonNode(request));
        Mockito.verify(controller).fadeOff();
    }
}