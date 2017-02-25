package com.dibbledos.piRGB.rest.server;

import com.dibbledos.piRGB.LightController;
import com.dibbledos.piRGB.rest.entities.OffRequest;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;


public class OffColorRequestHandler extends BaseColorRequestHandler {

    public OffColorRequestHandler(LightController controller) {
        super(controller);
    }

    @Override
    public String processRequest(JsonNode input) throws IOException {
        OffRequest request = mapper.convertValue(input, OffRequest.class);
        boolean shouldFade = request.getFade();

        if(shouldFade){
            System.out.println("Fading lights off");
            controller.fadeOff();
        }else {
            System.out.println("Turning lights off");
            controller.off();
        }
        return "Lights Off";
    }
}