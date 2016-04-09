package org.alexiwilius.ranti_app.network.response;

import org.alexiwilius.ranti_app.network.request.Request;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by AlexiWilius on 4.10.2015.
 */
public class ErrorResponse extends Response {
    public ErrorResponse(Request request, HttpURLConnection conn) throws IOException {
        super(request, conn, conn.getErrorStream());
    }

    public ErrorResponse(Request request, HttpURLConnection conn, String message) throws IOException {
        super(request, conn, message);
    }

    @Override
    public Object getData() {
        return toString();
    }
}
