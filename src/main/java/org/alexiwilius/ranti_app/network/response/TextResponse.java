package org.alexiwilius.ranti_app.network.response;

import org.alexiwilius.ranti_app.network.request.Request;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by AlexiWilius on 25.9.2015.
 */
public class TextResponse extends Response {

    public TextResponse(Request request, HttpURLConnection conn) throws IOException {
        super(request, conn, conn.getInputStream());
    }

    @Override
    public Object getData() {
        return toString();
    }
}
