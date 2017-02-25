package com.dibbledos.piRGB.rest.server;

import com.dibbledos.piRGB.LightController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public abstract class BaseColorRequestHandler implements HttpHandler {
    protected LightController controller;
    protected ObjectMapper mapper = new ObjectMapper();

    public BaseColorRequestHandler(LightController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestType = httpExchange.getRequestMethod(); //Post, get, etc

        if (requestType.equalsIgnoreCase("POST")) {
            JsonNode requestInput = mapper.readValue(httpExchange.getRequestBody(), JsonNode.class);

            String response = processRequest(requestInput);
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }else{
            httpExchange.sendResponseHeaders(400, 0);
            OutputStream os = httpExchange.getResponseBody();
            os.close();
        }
    }

    protected abstract String processRequest(JsonNode input) throws IOException;
}
