package org.alexiwilius.ranti_app.network.request;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

/**
 * Created by AlexiWilius on 9.4.2015.
 */
public class PostRequest extends Request {

    public PostRequest(String url) throws MalformedURLException {
        super(url);
    }

    public PostRequest(String url, Content content) throws MalformedURLException {
        super(url, content);
    }

    @Override
    public void sendRequest(HttpURLConnection connection) throws IOException {
        OutputStream os = null;
        try {
            Content content = getContent();
            connection.setRequestMethod("POST");
            if (content != null) {
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", content.getContentType());
                connection.setRequestProperty("Content-Length", String.valueOf(content.getContentLength()));
                os = connection.getOutputStream();
                os.write(content.getContent().getBytes());
            } else {
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", "0");
            }
        } finally {
            if (os != null) {
                os.flush();
                os.close();
            }
        }
    }
}
