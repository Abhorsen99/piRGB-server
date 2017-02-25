package com.dibbledos.piRGB.rest.server;

import com.dibbledos.piRGB.LightController;
import com.dibbledos.piRGB.rest.entities.Color;
import com.dibbledos.piRGB.rest.entities.ShowColorRequest;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ShowColorRequestHandler extends BaseColorRequestHandler {

    public ShowColorRequestHandler(LightController controller) {
        super(controller);
    }

    @Override
    public String processRequest(JsonNode input) throws IOException {
        System.out.println("Show color request received");
        ShowColorRequest request = mapper.convertValue(input, ShowColorRequest.class);
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
        return response;
    }
}
