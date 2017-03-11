package com.dibbledos.piRGB.rest.server;

import com.dibbledos.piRGB.LightController;
import com.dibbledos.piRGB.rest.entities.Color;
import com.dibbledos.piRGB.rest.entities.SequenceRequest;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;

public class ColorSequenceRequestHandler extends BaseColorRequestHandler {

    public ColorSequenceRequestHandler(LightController controller) {
        super(controller);
    }

    @Override
    public String processRequest(JsonNode input) throws IOException {
        System.out.println("Color sequence requested");
        SequenceRequest request = mapper.convertValue(input, SequenceRequest.class);
        List<Color> colors = request.getSequence();
        int showInterval = request.getInterval();

        boolean shouldFade = request.getFade();

        Thread t1 = new Thread(() -> {
            if (shouldFade) {
                controller.fadeSequence(colors, showInterval, request.getSoundSensitivity());
            } else {
                controller.showSequence(colors, showInterval, request.getSoundSensitivity());
            }
        });
        t1.start();

        String response = "Color sequence requested";
        return response;
    }
}
