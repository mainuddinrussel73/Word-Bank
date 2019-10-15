package com.example.mainuddin.myapplication34.ui.media;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.R;

import es.dmoral.toasty.Toasty;

import static com.example.mainuddin.myapplication34.ui.media.NotificationService.notificationView;
import static com.example.mainuddin.myapplication34.ui.media.NotificationService.notificationView1;

public class MyNotificationReceiver extends BroadcastReceiver {
    public static int REQUEST_CODE_NOTIFICATION = 1212;
    public static int REQUEST_CODE = 10;
    public static final String RESUME_ACTION = "RESUME_ACTION";
    public static final String STOP_ACTION = "STOP_ACTION";
    public static final String CANCEL_ACTION = "CANCEL_ACTION";
    public static final String NEXT_ACTION = "NEXT_ACTION";


    Media_list_activity mediaListActivity;


    public void  setMainActivityHandler(Media_list_activity main){
        mediaListActivity = main;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.e("action", action);



        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case RESUME_ACTION :



                    if(!mediaListActivity.mp.isPlaying()){
                        mediaListActivity.mp.start();
                        mediaListActivity.playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);

                        NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                        NotificationService.notification.setCustomContentView(notificationView);

                        notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                        NotificationService.notification.setCustomContentView(notificationView1);
                       // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                        NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());

                    }else {
                        mediaListActivity.mp.pause();
                        mediaListActivity.playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);

                        NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                        NotificationService.notification.setCustomContentView(notificationView);

                        NotificationService.notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                        NotificationService.notification.setCustomContentView(notificationView1);

                       // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                        NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());
                    }
                    Toasty.success(context, "Resume", Toast.LENGTH_SHORT).show();
// you resume action
                    break;
                case STOP_ACTION :

                    Toast.makeText(context, "stop", Toast.LENGTH_SHORT).show();
                    System.out.println("stop");
// you stop action
                    break;
                case CANCEL_ACTION:
                    //if(mediaListActivity.mp.isPlaying()){


                           try{ mediaListActivity.prevsong();}
                           catch (Exception e){
                               System.out.println(e.getMessage());
                           }

                        //Media_list_activity.nxt = true;
                        //MediaActivity.playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);

                    //}


                    Toasty.success(context, "Previous.", Toast.LENGTH_SHORT).show();


// you cancel action
                    break;
                case NEXT_ACTION:
                    //if(mediaListActivity.mp.isPlaying()){


                        try{ mediaListActivity.nxtsong();}
                        catch (Exception e){
                            System.out.println(e.getMessage());
                        }

                        //Media_list_activity.nxt = true;
                        //MediaActivity.playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);

                    //}
                    Toasty.success(context, "Next.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }



}