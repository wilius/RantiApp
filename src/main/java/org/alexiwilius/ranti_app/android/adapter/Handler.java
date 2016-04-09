package org.alexiwilius.ranti_app.android.adapter;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by AlexiWilius on 26.3.2015.
 */
public interface Handler {
    /**
     *  @param data indicates adapter data that to be filled via response
     * @param response request response
     */
    void handle(ArrayList<JSONObject> data, JSONObject response) throws Exception;
}
