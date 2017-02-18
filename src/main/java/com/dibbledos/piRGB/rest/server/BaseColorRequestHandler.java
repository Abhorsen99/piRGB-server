package com.dibbledos.piRGB.rest.server;

import com.dibbledos.piRGB.LightController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpHandler;

public abstract class BaseColorRequestHandler implements HttpHandler {
    LightController controller = LightController.getInstance();
    ObjectMapper mapper = new ObjectMapper();
}
