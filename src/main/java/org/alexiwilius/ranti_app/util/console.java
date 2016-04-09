package org.alexiwilius.ranti_app.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import org.alexiwilius.ranti_app.R;

/**
 * Created by AlexiWilius on 18.11.2014.
 */
public class console {

    /**
     * show specified message on the devices screen
     *
     * @param c      specifies application context that the message will shown
     * @param params specifies messages that will concatenated and shown on screen.
     */
    public static void log(Context c, Object... params) {
        Toast.makeText(c, getMessage(params).toString(), Toast.LENGTH_LONG).show();
    }

    /**
     * @param params specifies messages that will concatenated and logged console of the application's developed computer
     */
    public static void log(Object... params) {
        System.out.println(getMessage(params));
    }

    /**
     * @param params messages the user want to see as concatenated
     * @return concatenated message
     */
    private static CharSequence getMessage(Object... params) {
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            if (message.length() != 0)
                message.append(" ");

            if (params[i] == null) continue;
            message.append(params[i].toString());
        }
        return message;
    }

    public static void showErrorMessage(Context context, int resId) {
        showErrorMessage(context, context.getString(resId));
    }

    public static void showErrorMessage(Context context, int resId, DialogInterface.OnClickListener listener) {
        showErrorMessage(context, context.getString(resId), listener);
    }

    public static void showErrorMessage(Context context, String message) {
        showErrorMessage(context, message, null);
    }

    public static void showErrorMessage(Context context, final String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(context.getString(R.string.error));
        dlgAlert.setPositiveButton(context.getString(R.string.ok), listener);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public static void err(Context context, String message) {
        showErrorMessage(context, message);
    }

    public static void notifyAndClose(final Activity activity, int resId) {
        notifyAndClose(activity, activity.getString(resId));
    }

    public static void notifyAndClose(final Activity activity, String message) {
        showErrorMessage(activity, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
    }
}
