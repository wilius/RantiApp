package org.alexiwilius.ranti_app.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import org.alexiwilius.ranti_app.android.media.GifDecoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class GifImageView extends ImageView implements Runnable {

    private static final String TAG = "GifDecoderView";
    private GifDecoder gifDecoder;
    private Bitmap tmpBitmap;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean animating;
    private boolean shouldClear;
    private Thread animationThread;

    private final Runnable updateResults = new Runnable() {
        @Override
        public void run() {
            if (tmpBitmap != null && !tmpBitmap.isRecycled())
                setImageBitmap(tmpBitmap);
        }
    };

    private final Runnable cleanupRunnable = new Runnable() {
        @Override
        public void run() {
            if (tmpBitmap != null && !tmpBitmap.isRecycled())
                tmpBitmap.recycle();
            tmpBitmap = null;
            gifDecoder = null;
            animationThread = null;
            shouldClear = false;
        }
    };

    public GifImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        int resource = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", -1);
        if (resource <= 0) return;

        InputStream inputStream = context.getResources()
                .openRawResource(resource);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int bytesToRead = 1024;
        byte[] buffer = new byte[bytesToRead];
        try {
            int read;
            while (bytesToRead > 0 && -1 != (read = inputStream.read(buffer, 0, bytesToRead))) {
                outputStream.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setBytes(outputStream.toByteArray());
    }

    public GifImageView(final Context context) {
        super(context);
    }

    public void setBytes(final byte[] bytes) {
        gifDecoder = new GifDecoder();
        try {
            gifDecoder.read(bytes);
        } catch (final OutOfMemoryError e) {
            gifDecoder = null;
            Log.e(TAG, e.getMessage(), e);
            return;
        }

        if (canStart()) {
            animationThread = new Thread(this);
            animationThread.start();
        }
    }

    public void startAnimation() {
        animating = true;

        if (canStart()) {
            animationThread = new Thread(this);
            animationThread.start();
        }
    }

    public boolean isAnimating() {
        return animating;
    }

    public void stopAnimation() {
        animating = false;

        if (animationThread != null) {
            animationThread.interrupt();
            animationThread = null;
        }
    }

    public void clear() {
        animating = false;
        shouldClear = true;
        stopAnimation();
    }

    private boolean canStart() {
        return animating && gifDecoder != null && animationThread == null;
    }

    @Override
    public void run() {
        if (shouldClear) {
            handler.post(cleanupRunnable);
            return;
        }

        final int n = gifDecoder.getFrameCount();
        do {
            for (int i = 0; i < n; i++) {
                if (!animating)
                    break;
                try {
                    tmpBitmap = gifDecoder.getNextFrame();
                    if (!animating)
                        break;
                    handler.post(updateResults);
                } catch (final ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                    Log.w(TAG, e);
                }
                if (!animating)
                    break;
                gifDecoder.advance();
                try {
                    Thread.sleep(gifDecoder.getNextDelay());
                } catch (final Exception e) {
                    // suppress any exception
                    // it can be InterruptedException or IllegalArgumentException
                }
            }
        } while (animating);
    }
}
