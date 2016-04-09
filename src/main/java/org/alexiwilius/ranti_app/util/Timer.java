package org.alexiwilius.ranti_app.util;

import android.os.Handler;

/**
 * Created by AlexiWilius on 20.1.2015.
 */
public class Timer extends Handler {
    private static Integer STOPPED = -1;
    private static Integer WAIT = 0;
    private static Integer STARTED = 1;

    private Integer status = 0;
    private Runnable mRunnable;

    /**
     * stops updater. if there is an running task, cancel it.
     */
    public void stop() {
        removeCallbacks(mRunnable);
        setStatus(STOPPED);
    }

    /**
     * shortcut of the @link
     */
    public void start(Runnable runnable) {
        start(runnable, null);
    }

    /**
     * starts updating task
     *
     * @param interval the time delay of the starting operation
     */
    public void start(final Runnable runnable, Long interval) {
        setStatus(STOPPED);

        if (interval == null) {
            mRunnable = runnable;
            post(mRunnable);
        } else {
            setStatus(WAIT);
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    setStatus(STARTED);
                    runnable.run();
                }
            };
            postDelayed(mRunnable, interval);
        }
    }


    /**
     * @return checks whether or not the updating task stopped
     */
    public boolean isStopped() {
        return status < 0;
    }

    /**
     * @return whether the task is running or not
     */
    public boolean isRunning() {
        return status > 0;
    }

    private synchronized void setStatus(Integer status) {
        this.status = status;
    }
}
