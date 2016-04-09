package org.alexiwilius.ranti_app.network;

import android.os.AsyncTask;

import org.alexiwilius.ranti_app.network.request.Request;
import org.alexiwilius.ranti_app.network.response.ErrorResponse;
import org.alexiwilius.ranti_app.network.response.Response;

public class RequestTask<P extends Request, R extends Response> {

    private ResponseReady callback;

    AsyncTask<P, Integer, R[]> mTask;

    private boolean loading = false;

    public RequestTask(ResponseReady callback) {
        this.callback = callback;
    }

    /**
     * cancels the current request
     */
    public void cancel() {
        loading = false;
        if (mTask == null || mTask.isCancelled()) return;

        switch (mTask.getStatus()) {
            case PENDING:
            case RUNNING:
                mTask.cancel(false);
        }
    }

    /**
     * @param params request that will executed in background
     */
    public void execute(P params) {
        loading = true;
        createTask();
        mTask.execute(params);
    }

    public boolean isLoading() {
        return loading;
    }

    private void createTask() {
        mTask = new AsyncTask<P, Integer, R[]>() {

            @SafeVarargs
            @Override
            protected final R[] doInBackground(P... params) {
                return (R[]) new Response[]{params[0].getResponse()};
            }

            @Override
            protected void onPostExecute(R[] result) {
                loading = false;
                if (callback == null) return;

                if (result[0] instanceof ErrorResponse)
                    callback.onError((ErrorResponse) result[0]);
                else
                    callback.onSuccess(result[0]);

                callback.onComplete(result[0]);
            }

            @Override
            protected void onCancelled(R[] rs) {
                if (callback == null) return;
                callback.onCancel();
            }
        };
    }

    public static abstract class ResponseReady {

        protected void onSuccess(Response responses) {
        }

        protected void onError(ErrorResponse responses) {
        }

        protected void onComplete(Response responses) {
        }

        protected void onCancel() {

        }
    }
}
