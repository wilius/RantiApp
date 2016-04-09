package org.alexiwilius.ranti_app.android.adapter;

import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by AlexiWilius on 19.11.2014.
 */
public interface JSONAdapterViewListener {
    View onItemCreate(JSONObject data, int pos, View convertView, ArrayList<JSONObject> allData, ViewGroup parent);

    void onAdapterRefreshStart();

    void onAdapterRefreshCompleted();

    void onAdapterRefreshCancelled();

    void onAdapterRefreshCrashed(String message);
}
