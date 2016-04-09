package org.alexiwilius.ranti_app.network.response;

import org.alexiwilius.ranti_app.network.request.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLConnection;

/**
 * Created by AlexiWilius on 18.11.2014.
 */
public abstract class Response {

    private static final char UTF8_BOM = '\uFEFF';

    private final URLConnection mConnection;
    private final StringBuilder mResponseText;
    private final Request mRequest;

    public abstract Object getData() throws RuntimeException;

    public Response(Request request, HttpURLConnection conn, InputStream stream) throws IOException {
        mRequest = request;
        mConnection = conn;
        mResponseText = removeUTF8BOM(read(stream));
    }

    public Response(Request request, HttpURLConnection conn, String response) throws IOException {
        mRequest = request;
        mConnection = conn;
        mResponseText = new StringBuilder(response);
    }

    private static StringBuilder removeUTF8BOM(StringBuilder s) {
        if (s.length() != 0 && s.charAt(0) == UTF8_BOM) {
            s.replace(0, 1, "");
        }
        return s;
    }

    private StringBuilder read(InputStream stream) {
        StringBuilder response = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(stream));

        CharSequence line;
        try {
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                rd.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    public String toString() {
        return mResponseText.toString();
    }

    public URLConnection getConnection() {
        return mConnection;
    }

    public Request getRequest() {
        return mRequest;
    }
}
