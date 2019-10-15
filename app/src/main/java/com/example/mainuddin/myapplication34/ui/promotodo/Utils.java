package com.example.mainuddin.myapplication34.ui.promotodo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

public class Utils {

    public static Drawable getDrawable(Context context, int res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(res, context.getTheme());
        } else {
            return context.getResources().getDrawable(res);
        }
    }
}