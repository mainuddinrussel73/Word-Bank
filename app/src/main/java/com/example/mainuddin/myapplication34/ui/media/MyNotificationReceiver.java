package com.example.mainuddin.myapplication34.ui.media;


import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.example.mainuddin.myapplication34.ui.media.NotificationService.notification;
import static com.example.mainuddin.myapplication34.ui.media.NotificationService.notificationView;

public class MyNotificationReceiver extends BroadcastReceiver {
    public static int REQUEST_CODE_NOTIFICATION = 1212;
    public static int REQUEST_CODE = 10;
    public static final String RESUME_ACTION = "RESUME_ACTION";
    public static final String STOP_ACTION = "STOP_ACTION";
    public static final String CANCEL_ACTION = "CANCEL_ACTION";
    public static final String NEXT_ACTION = "NEXT_ACTION";


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.e("action", action);



        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case RESUME_ACTION :



                    if(!Media_list_activity.mp.isPlaying()){
                        Media_list_activity.mp.start();
                        Media_list_activity.playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);

                        NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                        NotificationService.notification.setCustomContentView(notificationView);
                       // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                        NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());

                    }else {
                        Media_list_activity.mp.pause();
                        Media_list_activity.playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);

                        NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                        NotificationService.notification.setCustomContentView(notificationView);
                       // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                        NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());
                    }
                    Toast.makeText(context, "resume", Toast.LENGTH_SHORT).show();
// you resume action
                    break;
                case STOP_ACTION :
                    if(Media_list_activity.mp.isPlaying()){
                        Media_list_activity.mp.pause();
                        Media_list_activity.playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);

                        notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                        notification.setCustomContentView(notificationView);
                        notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                    }else{

                        Media_list_activity.mp.start();
                        Media_list_activity.playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);

                            notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                            notification.setCustomContentView(notificationView);
                            notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");


                    }
                    Toast.makeText(context, "stop", Toast.LENGTH_SHORT).show();
// you stop action
                    break;
                case CANCEL_ACTION:
                    Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
// you cancel action
                    break;
                case NEXT_ACTION:
                    if(Media_list_activity.mp.isPlaying()){
                        //Media_list_activity.nxt = true;
                        //MediaActivity.playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);

                    }
                    Toast.makeText(context, "next button", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }



}