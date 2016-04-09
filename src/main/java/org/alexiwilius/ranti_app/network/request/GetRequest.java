package org.alexiwilius.ranti_app.network.request;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

/**
 * Created by AlexiWilius on 9.4.2015.
 */
public class GetRequest extends Request {

    public GetRequest(String url) throws MalformedURLException {
        super(url);
    }

    public GetRequest(String url, KeyValueContent content) throws MalformedURLException {
        super(url + (content.getContent() != null ? "?" + content.getContent() : ""), content);
    }

    @Override
    protected void sendRequest(HttpURLConnection connection) throws IOException {
        connection.setRequestMethod("GET");
    }
}
