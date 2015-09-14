package dibble.chris.lightcontrol;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/color/show", new ShowColorRequestHandler());
        server.createContext("/off", new OffColorRequestHandler());
        server.createContext("/sequence/show", new ColorSequenceRequestHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
