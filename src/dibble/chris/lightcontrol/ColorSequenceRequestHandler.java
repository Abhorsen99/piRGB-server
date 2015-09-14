package dibble.chris.lightcontrol;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class ColorSequenceRequestHandler extends BaseColorRequestHandler{

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestType = httpExchange.getRequestMethod(); //Post, get, etc

        if (requestType.equalsIgnoreCase("POST")) {
            System.out.println("Color sequence requested");
            String payloadJson = IOUtils.toString(httpExchange.getRequestBody());
            JSONObject requestObject = new JSONObject(payloadJson);
            ArrayList<Color> colors = parseColorSequence(requestObject);
            int showInterval = parseIntervalLength(requestObject);

            boolean shouldFade = findIfFadingRequested(requestObject);

            Thread t1 = new Thread(() -> {
                if (shouldFade) {
                    controller.fadeSequence(colors, showInterval);
                } else {
                    controller.showSequence(colors, showInterval);
                }
            });
            t1.start();

            String response = "Color sequence requested";
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private int parseIntervalLength(JSONObject requestObject) {
        return requestObject.getInt("interval");
    }

    private ArrayList<Color> parseColorSequence(JSONObject requestObject) {
        JSONArray sequenceData = requestObject.getJSONArray("sequence");
        ArrayList<Color> colors = new ArrayList<>();

        for (int i=0; i<sequenceData.length(); i++) {
            JSONObject item = sequenceData.getJSONObject(i);
            Color color = convertJsonToColor(item);
            colors.add(color);
            System.out.println("Added " + color + " sequence of colors");
        }
        return colors;
    }
}
