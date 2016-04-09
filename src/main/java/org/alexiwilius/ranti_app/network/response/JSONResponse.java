package org.alexiwilius.ranti_app.network.response;

import org.alexiwilius.ranti_app.network.request.Request;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by AlexiWilius on 18.11.2014.
 */
public class JSONResponse extends Response {

    public JSONResponse(Request request, HttpURLConnection conn) throws IOException {
        super(request, conn, conn.getInputStream());
    }

    public JSONObject getData() {
        try {
            return new JSONObject(toString());
        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
