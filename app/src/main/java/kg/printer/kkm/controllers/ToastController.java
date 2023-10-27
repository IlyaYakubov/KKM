package kg.printer.kkm.controllers;

import android.content.Context;
import android.widget.Toast;

public class ToastController {

    private ToastController() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void show(Context context, int msgResId) {
        Toast.makeText(context, msgResId, Toast.LENGTH_SHORT).show();
    }

}