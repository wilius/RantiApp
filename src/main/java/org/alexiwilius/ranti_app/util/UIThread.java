package org.alexiwilius.ranti_app.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.alexiwilius.ranti_app.R;

/**
 * Created by AlexiWilius on 9.1.2015.
 */
public class UIThread {

    static Activity activity;

    public static void setActivity(Activity activity) {
        UIThread.activity = activity;
    }

    public static void run(Runnable runnable) {
        activity.runOnUiThread(runnable);
    }

    public static Activity getActivity() {
        return activity;
    }

    public static View findView(int resourceId) {
        return activity.findViewById(resourceId);
    }

    public static boolean isPlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (status != ConnectionResult.SUCCESS) {
            String message;
            if (status == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED)
                message = getActivity().getString(R.string.play_services_require_update);
            else
                message = getActivity().getString(R.string.play_services_not_exists);

            AlertDialog.Builder dialog = new AlertDialog.Builder(UIThread.getActivity());

            dialog.setMessage(message);
            dialog.setTitle(R.string.error);
            dialog.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gms")));
                    } catch (ActivityNotFoundException e) {
                        console.notifyAndClose(getActivity(), R.string.missing_play_store);
                    } catch (Exception e) {
                        console.notifyAndClose(getActivity(), e.getMessage());
                    }
                }
            });
            dialog.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    UIThread.getActivity().finish();
                }
            });
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    UIThread.getActivity().finish();
                }
            });
            dialog.create().show();
            return false;
        }
        return true;
    }

    public static boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return PackageManager.PERMISSION_GRANTED == activity.checkSelfPermission(permission);
        return activity.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public  void requestPermissions(int reqCode, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            activity.requestPermissions(permissions, reqCode);
    }
}
