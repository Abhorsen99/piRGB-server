package com.dibbledos.piRGB.rest.server;

import com.dibbledos.piRGB.LightController;
import com.dibbledos.piRGB.rest.entities.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
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
        switch(requestType.toUpperCase()){
            case("POST"): {
                JsonNode requestInput = mapper.readValue(httpExchange.getRequestBody(), JsonNode.class);
                sendResponse(httpExchange, processRequest(requestInput));
                break;
            }
            case("OPTIONS"): {
                sendResponse(httpExchange, "The world is your oyster");
                break;
            }
            default : {
                httpExchange.sendResponseHeaders(400, 0);
                OutputStream os = httpExchange.getResponseBody();
                os.close();
            }
        }
    }

    private void sendResponse(HttpExchange exchange, String responseMessage) throws IOException {
        String response = mapper.writeValueAsString(new Response(responseMessage));
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    protected abstract String processRequest(JsonNode input) throws IOException;
}
