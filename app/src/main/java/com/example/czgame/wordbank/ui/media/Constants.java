package com.example.czgame.wordbank.ui.media;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.czgame.wordbank.R;

public class Constants {
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

    public interface ACTION {
        String MAIN_ACTION = "com.example.mainuddin.myapplication34.action.main";
        String INIT_ACTION = "com.example.mainuddin.myapplication34.action.init";
        String PREV_ACTION = "com.example.mainuddin.myapplication34.action.prev";
        String PLAY_ACTION = "com.example.mainuddin.myapplication34.action.play";
        String NEXT_ACTION = "com.example.mainuddin.myapplication34.action.next";
        String STARTFOREGROUND_ACTION = "com.example.mainuddin.myapplication34.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.example.mainuddin.myapplication34.action.stopforeground";

        String AUDIOFOCUS_GAIN = "com.example.mainuddin.myapplication34.action.AUDIOFOCUS_GAIN.";
        String AUDIOFOCUS_LOSS = "com.example.mainuddin.myapplication34.action.AUDIOFOCUS_LOSS";
        String AUDIOFOCUS_LOSS_TRANSIENT = "com.example.mainuddin.myapplication34.action.AUDIOFOCUS_LOSS_TRANSIENT";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }

}

