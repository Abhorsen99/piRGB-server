package com.dibbledos.piRGB;

import com.dibbledos.piRGB.rest.server.ColorSequenceRequestHandler;
import com.dibbledos.piRGB.rest.server.OffColorRequestHandler;
import com.dibbledos.piRGB.rest.server.ShowColorRequestHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        LightController lightSystem = LightController.getInstance();

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/color/show", new ShowColorRequestHandler(lightSystem));
        server.createContext("/off", new OffColorRequestHandler(lightSystem));
        server.createContext("/sequence/show", new ColorSequenceRequestHandler(lightSystem));
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
