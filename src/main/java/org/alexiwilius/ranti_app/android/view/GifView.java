package org.alexiwilius.ranti_app.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by AlexiWilius on 17.4.2015.
 */

public class GifView extends View {

    Movie movie;
    long moviestart;

    public GifView(Context context) throws IOException {
        super(context);
    }

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int resource = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", -1);
        loadGIFResource(context, resource);
    }

    public void loadGIFResource(Context context, int id) {
        //turn off hardware acceleration
        //this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        InputStream is = context.getResources().openRawResource(id);
        movie = Movie.decodeStream(is);
    }

    public void loadGIFAsset(Context context, String filename) {
        InputStream is;
        try {
            is = context.getResources().getAssets().open(filename);
            movie = Movie.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (movie == null) {
            return;
        }

        long now = android.os.SystemClock.uptimeMillis();

        if (moviestart == 0) moviestart = now;

        int relTime;
        relTime = (int) ((now - moviestart) % movie.duration());
        movie.setTime(relTime);
        movie.draw(canvas, 10, 10);
        this.invalidate();
    }
}