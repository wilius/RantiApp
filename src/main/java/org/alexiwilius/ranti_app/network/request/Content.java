package org.alexiwilius.ranti_app.network.request;

public abstract class Content {

    protected Request mRequest;

    public abstract String getContent();

    public abstract Integer getContentLength();

    public abstract String getContentType();

    protected void setRequest(Request request) {
        if (!request.isConnected())
            throw new UnsupportedOperationException("Cannot set a Request that is not connected to server to a Content instance.");

        mRequest = request;
    }
}
