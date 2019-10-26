package com.example.mainuddin.myapplication34.ui.media;


import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;
import es.dmoral.toasty.Toasty;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.example.mainuddin.myapplication34.ui.media.Media_list_activity.mp;
import static com.example.mainuddin.myapplication34.ui.media.NotificationService.getDominantColor;
import static com.example.mainuddin.myapplication34.ui.media.NotificationService.notificationView;
import static com.example.mainuddin.myapplication34.ui.media.NotificationService.notificationView1;
import static com.example.mainuddin.myapplication34.ui.promotodo.promodetail.textView1;

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



                    if(!mp.isPlaying()){
                        mp.start();
                        mediaListActivity.playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);

                        NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                        NotificationService.notification.setCustomContentView(notificationView);

                        notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                        NotificationService.notification.setCustomContentView(notificationView1);
                       // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                        NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());

                    }else {
                        mp.pause();
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


                    if (isAppOnForeground(context,"com.example.mainuddin.myapplication34")) {
                        // App is in Foreground
                        if(!mp.isPlaying()){
                           // mediaListActivity.mp.stop();

                        }else {
                            mp.pause();

                        }

                    } else {
                        // App is in Background
                        if(!mp.isPlaying()){
                            mp.stop();

                        }else {
                            mp.pause();

                        }

                        Intent service = new Intent(context, NotificationService.class);
                        context.stopService(service);
                    }



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

    private boolean isAppOnForeground(Context context,String appPackageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            //App is closed
            return false;
        }
        final String packageName = appPackageName;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                //                Log.e("app",appPackageName);
                return true;
            }else{
                //App is closed
            }
        }
        return false;
    }

    private String[] getAudioPath(Context context,String songTitle) {

        final Cursor mInternalCursor = context.getContentResolver().query(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA},
                MediaStore.Audio.Media.TITLE + "=?",
                new String[]{songTitle},
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        final Cursor mExternalCursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA},
                MediaStore.Audio.Media.TITLE + "=?",
                new String[]{songTitle},
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        Cursor[] cursors = {mInternalCursor, mExternalCursor};
        final MergeCursor mMergeCursor = new MergeCursor(cursors);

        int count = mMergeCursor.getCount();

        String[] songs = new String[count];
        String[] mAudioPath = new String[count];
        int i = 0;
        if (mMergeCursor.moveToFirst()) {
            do {
                songs[i] = mMergeCursor.getString(mMergeCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                mAudioPath[i] = mMergeCursor.getString(mMergeCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                i++;
            } while (mMergeCursor.moveToNext());
        }

        mMergeCursor.close();
        return mAudioPath;
    }





}