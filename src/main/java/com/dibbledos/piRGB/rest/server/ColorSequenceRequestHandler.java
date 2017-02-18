package com.dibbledos.piRGB.rest.server;

import com.dibbledos.piRGB.rest.entities.Color;
import com.dibbledos.piRGB.rest.entities.SequenceRequest;
import com.dibbledos.piRGB.rest.server.BaseColorRequestHandler;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ColorSequenceRequestHandler extends BaseColorRequestHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestType = httpExchange.getRequestMethod(); //Post, get, etc

        if (requestType.equalsIgnoreCase("POST")) {
            System.out.println("Color sequence requested");
            SequenceRequest request = mapper.readValue(httpExchange.getRequestBody(), SequenceRequest.class);
            List<Color> colors = request.getSequence();
            int showInterval = request.getInterval();

            boolean shouldFade = request.getFade();

            Thread t1 = new Thread(() -> {
                if (shouldFade) {
                    controller.fadeSequence(colors, showInterval);
                } else {
                    controller.showSequence(colors, showInterval);
                }
            });
            t1.start();

            String response = "Color sequence requested";
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
