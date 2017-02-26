package com.dibbledos.piRGB;

import com.dibbledos.piRGB.lightSystems.LightSystem;
import com.dibbledos.piRGB.lightSystems.LightSystemProvider;
import com.dibbledos.piRGB.rest.server.ColorSequenceRequestHandler;
import com.dibbledos.piRGB.rest.server.OffColorRequestHandler;
import com.dibbledos.piRGB.rest.server.ShowColorRequestHandler;
import com.dibbledos.piRGB.soundSensitivity.MicReader;
import com.dibbledos.piRGB.soundSensitivity.Microphone;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        LightSystem lightSystem = new LightSystemProvider().getLightSystem();
        MicReader micReader = new MicReader(new Microphone());
        LightController lightController = new LightController(lightSystem, micReader);

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/color/show", new ShowColorRequestHandler(lightController));
        server.createContext("/off", new OffColorRequestHandler(lightController));
        server.createContext("/sequence/show", new ColorSequenceRequestHandler(lightController));
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
