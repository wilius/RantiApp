package org.alexiwilius.ranti_app.network.request;

import org.alexiwilius.ranti_app.collection.RequestParams;

/**
 * Created by AlexiWilius on 6.10.2015.
 */
public class UrlEncodedContent extends KeyValueContent {
    public UrlEncodedContent(RequestParams params) {
        setParams(params);
    }

    @Override
    public String getContentType() {
        return "application/x-www-form-urlencoded";
    }


}
