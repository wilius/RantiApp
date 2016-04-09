package org.alexiwilius.ranti_app.network.request;

import android.support.annotation.NonNull;

import org.json.JSONObject;

/**
 * Created by AlexiWilius-WS on 13.2.2016.
 */
public class JSONContent extends Content {
    private final JSONObject mContent;

    public JSONContent(@NonNull JSONObject content) {
        mContent = content;
    }

    @Override
    public String getContent() {
        return mContent.toString();
    }

    @Override
    public Integer getContentLength() {
        return getContent().length();
    }

    @Override
    public String getContentType() {
        return "application/json";
    }
}
