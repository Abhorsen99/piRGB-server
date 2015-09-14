package dibble.chris.lightcontrol;

import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseColorRequestHandler implements HttpHandler {
    LightController controller = LightController.getInstance();

    Color convertJsonToColor(JSONObject color){
        return new Color(color.getInt("red"), color.getInt("green"), color.getInt("blue"), color.getInt("magnitude"));
    }

    boolean findIfFadingRequested(JSONObject request) {
        boolean fade = false;
        try {
            fade = request.getBoolean("fade");
        } catch (JSONException e) {}
        return fade;
    }
}
