package org.alexiwilius.ranti_app.network.request;

import org.alexiwilius.ranti_app.network.response.ErrorResponse;
import org.alexiwilius.ranti_app.network.response.JSONResponse;
import org.alexiwilius.ranti_app.network.response.Response;
import org.alexiwilius.ranti_app.network.response.TextResponse;
import org.alexiwilius.ranti_app.network.response.XmlResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by AlexiWilius on 16.11.2014.
 */
public abstract class Request {

    private Content mContent;
    private URL url;
    private HttpURLConnection mConnection;

    protected abstract void sendRequest(HttpURLConnection connection) throws IOException;

    public Request(CharSequence url) throws MalformedURLException {
        setURL(url);
        connect();
    }

    public Request(CharSequence url, Content content) throws MalformedURLException {
        mContent = content;
        setURL(url);
        connect();
        content.setRequest(this);
    }

    protected Response createResponse() throws IOException {
        String contentType = mConnection.getContentType();

        switch (mConnection.getResponseCode()) {
            case 200:
                if (contentType == null)
                    throw new IOException("Data read error");

                if (contentType.contains("/json"))
                    return new JSONResponse(this, mConnection);

                if (contentType.contains("text/html"))
                    return new TextResponse(this, mConnection);

                if (contentType.contains("text/xml"))
                    return new XmlResponse(this, mConnection);
            default:
                return new ErrorResponse(this, mConnection);
        }
    }

    private void connect() {
        try {
            mConnection = (HttpURLConnection) getURL().openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            mConnection = null;
        }
    }

    public URL getURL() {
        return url;
    }

    public void setURL(CharSequence url) throws MalformedURLException {
        setURL(new URL(url.toString()));
    }

    public void setURL(URL url) {
        this.url = url;
    }

    public HttpURLConnection getConnection() {
        return mConnection;
    }

    public Content getContent() {
        return mContent;
    }

    public Response getResponse() {
        try {
            sendRequest(mConnection);
            return createResponse();
        } catch (IOException e) {
            e.printStackTrace();
            mConnection = null;
            try {
                return new ErrorResponse(this, mConnection, e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        } finally {
            if (mConnection != null)
                mConnection.disconnect();
        }
    }

    public boolean isConnected() {
        return mConnection != null;
    }
}
