package dibble.chris.lightcontrol;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;


public class OffColorRequestHandler extends BaseColorRequestHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestType = httpExchange.getRequestMethod(); //Post, get, etc
        String payloadJson = IOUtils.toString(httpExchange.getRequestBody());
        JSONObject requestObject = new JSONObject(payloadJson);
        boolean shouldFade = findIfFadingRequested(requestObject);

        if (requestType.equalsIgnoreCase("POST")) {
            if(shouldFade){
                System.out.println("Fading lights off");
                controller.fadeOff();
            }else {
                System.out.println("Turning lights off");
                controller.off();
            }
            String response = "Lights Off";
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}