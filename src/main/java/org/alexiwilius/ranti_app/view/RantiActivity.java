package org.alexiwilius.ranti_app.view;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.alexiwilius.ranti_app.util.UIThread;

/**
 * Created by AlexiWilius on 4.11.2015.
 */
public class RantiActivity extends AppCompatActivity {

    {
        UIThread.setActivity(this);
    }

    /**
     * decide to return SupportFragmentManager or contemporary fragment manager
     *
     * @return FragmentManager
     */
    protected FragmentManager getFM() {
        return getSupportFragmentManager();
    }

    @Override
    protected void onResume() {
        UIThread.setActivity(this);
        super.onResume();
    }
}
