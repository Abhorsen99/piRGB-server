package com.dibbledos.piRGB.rest.server;

import com.dibbledos.piRGB.LightController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mock;

public class HandlerBaseTest {
    private  ObjectMapper mapper = new ObjectMapper();
    @Mock
    protected LightController controller;
    protected BaseColorRequestHandler handler;

    protected JsonNode convertRequestToJsonNode(Object request){
        return mapper.convertValue(request, JsonNode.class);
    }
}
