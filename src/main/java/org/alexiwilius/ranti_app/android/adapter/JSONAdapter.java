package org.alexiwilius.ranti_app.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.alexiwilius.ranti_app.collection.RequestParams;
import org.alexiwilius.ranti_app.network.RequestTask;
import org.alexiwilius.ranti_app.network.request.Request;
import org.alexiwilius.ranti_app.network.request.UrlEncodedContent;
import org.alexiwilius.ranti_app.network.response.ErrorResponse;
import org.alexiwilius.ranti_app.network.response.JSONResponse;
import org.alexiwilius.ranti_app.network.response.Response;
import org.alexiwilius.ranti_app.util.Timer;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;


public class JSONAdapter extends BaseAdapter {

    /**
     * Handler to fill data
     */
    private final Handler handler;

    /**
     * holds request task that the user want to run
     */
    private Request request;

    /**
     * holds last response to request
     */
    private JSONResponse response;

    private RequestTask<Request, JSONResponse> task;

    private ArrayList<JSONObject> data = new ArrayList<JSONObject>();

    private JSONAdapterViewListener jsonAdapterViewListener;

    private Timer timer;

    public JSONAdapter(Request request, Handler handler) {
        this.request = request;
        this.handler = handler;
    }

    public JSONResponse getResponse() {
        return response;
    }

    public void refresh(Request request) {
        setRequest(request);
        refresh();
    }

    public void refresh() {
        task = new RequestTask<>(new RequestTask.ResponseReady() {
            @Override
            protected void onSuccess(Response responses) {
                clear();
                try {
                    JSONAdapter.this.response = (JSONResponse) responses;
                    handler.handle(data, JSONAdapter.this.response.getData());
                    notifyDataSetChanged();
                } catch (Exception e) {
                    notifyError(e.getMessage());
                }
                notifyUpdateFinished();
            }

            @Override
            protected void onError(ErrorResponse responses) {
                notifyError(responses.getData().toString());
            }

            @Override
            protected void onCancel() {
                notifyUpdateCancelled();
            }
        });
        task.execute(request);
        notifyUpdateStart();
    }

    public void cancel() {
        if (task != null)
            task.cancel();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        checkViewIsEmpty();
        return jsonAdapterViewListener.onItemCreate(
                data.get(position),
                position, convertView,
                data,
                parent);
    }

    private void notifyUpdateStart() {
        checkViewIsEmpty();
        jsonAdapterViewListener.onAdapterRefreshStart();
    }


    private void notifyUpdateFinished() {
        checkViewIsEmpty();
        jsonAdapterViewListener.onAdapterRefreshCompleted();
    }

    private void notifyUpdateCancelled() {
        checkViewIsEmpty();
        jsonAdapterViewListener.onAdapterRefreshCancelled();
    }

    private void notifyError(String message) {
        checkViewIsEmpty();
        jsonAdapterViewListener.onAdapterRefreshCrashed(message);
    }

    public void clear() {
        data.clear();
    }

    public URL getUrl() {
        return request.getURL();
    }

    public void setUrl(URL url) {
        request.setURL(url);
    }

    public RequestParams getParams() {
        return ((UrlEncodedContent) request.getContent()).getParams();
    }

    public void setParams(RequestParams params) {
        ((UrlEncodedContent) request.getContent()).setParams(params);
    }

    public JSONAdapterViewListener getJsonAdapterViewListener() {
        return jsonAdapterViewListener;
    }

    public void setJsonAdapterViewListener(JSONAdapterViewListener jsonAdapterViewListener) {
        this.jsonAdapterViewListener = jsonAdapterViewListener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private void checkViewIsEmpty() {
        if (jsonAdapterViewListener == null)
            throw new NullPointerException("AdapterViewListener must be set.");
    }

    public boolean isLoading() {
        return task != null && task.isLoading();
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}