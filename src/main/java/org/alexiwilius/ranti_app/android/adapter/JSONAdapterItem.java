package org.alexiwilius.ranti_app.android.adapter;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONAdapterItem {

    private static int counter = 0;
    private int rowId;

    private JSONObject object;

    {
        rowId = ++counter;
    }

    public JSONAdapterItem(JSONObject object) {
        if (object == null)
            throw new NullPointerException("Parameter cannot be null. ");

        this.object = object;
    }

    public String get(String key) {

        if (!object.has(key))
            return "";
        else
            try {
                return object.getString(key);
            } catch (JSONException e) {
                return "";
            }
    }

    public int getRowId() {
        return rowId;
    }
}
