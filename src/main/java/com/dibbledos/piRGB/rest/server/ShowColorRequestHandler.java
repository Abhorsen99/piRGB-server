package com.dibbledos.piRGB.rest.server;

import com.dibbledos.piRGB.rest.entities.Color;
import com.dibbledos.piRGB.rest.entities.ShowColorRequest;
import com.dibbledos.piRGB.rest.server.BaseColorRequestHandler;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;

public class ShowColorRequestHandler extends BaseColorRequestHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
        String requestType = t.getRequestMethod(); //Post, get, etc

        if (requestType.equalsIgnoreCase("POST")) {
            System.out.println("Show color request received");
            ShowColorRequest request = mapper.readValue(t.getRequestBody(), ShowColorRequest.class);
            Color requestedColor = request.getColor();
            boolean shouldFade = request.getFade();

            System.out.println(
                    String.format("Requested color: %s. Are we fading? %b",
                            requestedColor,
                            shouldFade
                    )
            );
            if(shouldFade){
                controller.fadeTo(requestedColor, request.getSoundSensitive());
            }else{
                controller.showColor(requestedColor, request.getSoundSensitive());
            }

            String response = "Color: " + requestedColor.toString();
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
