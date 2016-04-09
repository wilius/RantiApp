package org.alexiwilius.ranti_app.network.request;

import org.alexiwilius.ranti_app.collection.RequestParams;

/**
 * Created by AlexiWilius on 7.10.2015.
 */
public abstract class KeyValueContent extends Content {

    protected RequestParams mParams;
    protected String mTextContent;

    @Override
    public String getContent() {
        return mTextContent;
    }

    @Override
    public Integer getContentLength() {
        return mTextContent.length();
    }

    public RequestParams getParams() {
        return mParams;
    }


    public final void setParams(RequestParams params) {
        if (mRequest != null && mRequest instanceof GetRequest && mRequest.isConnected())
            throw new UnsupportedOperationException("Cannot change parameters after GET request connected.");

        mParams = params;
        mTextContent = mParams.toString();
    }
}
