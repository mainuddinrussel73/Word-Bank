package com.example.mainuddin.myapplication34.ui.media;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.mainuddin.myapplication34.R;

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.example.mainuddin.myapplication34.action.main";
        public static String INIT_ACTION = "com.example.mainuddin.myapplication34.action.init";
        public static String PREV_ACTION = "com.example.mainuddin.myapplication34.action.prev";
        public static String PLAY_ACTION = "com.example.mainuddin.myapplication34.action.play";
        public static String NEXT_ACTION = "com.example.mainuddin.myapplication34.action.next";
        public static String STARTFOREGROUND_ACTION = "com.example.mainuddin.myapplication34.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.example.mainuddin.myapplication34.action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.image, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }

}

