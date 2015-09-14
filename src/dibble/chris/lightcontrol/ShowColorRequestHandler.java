package dibble.chris.lightcontrol;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public class ShowColorRequestHandler extends BaseColorRequestHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
        String requestType = t.getRequestMethod(); //Post, get, etc

        if (requestType.equalsIgnoreCase("POST")) {
            System.out.println("Show color request received");
            String payloadJson = IOUtils.toString(t.getRequestBody());
            JSONObject requestObject = new JSONObject(payloadJson);
            JSONObject color = requestObject.getJSONObject("color");
            Color requestedColor = convertJsonToColor(color);
            boolean shouldFade = findIfFadingRequested(requestObject);

                    System.out.println(
                            String.format("Requested color: %s. Are we fading? %b",
                                    requestedColor,
                                    shouldFade
                            )
                    );
            if(shouldFade){
                controller.fadeTo(requestedColor);
            }else{
                controller.showColor(requestedColor);
            }

            String response = "Color: " + requestedColor.toString();
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
