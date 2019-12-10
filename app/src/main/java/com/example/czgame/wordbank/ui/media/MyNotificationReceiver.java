package com.example.czgame.wordbank.ui.media;


import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.czgame.wordbank.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import androidx.palette.graphics.Palette;
import be.rijckaert.tim.animatedvector.FloatingMusicActionButton;
import es.dmoral.toasty.Toasty;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.example.czgame.wordbank.ui.media.Media_list_activity.mp;
import static com.example.czgame.wordbank.ui.media.Media_list_activity.position;
import static com.example.czgame.wordbank.ui.media.Media_list_activity.serviceIntent;

public class MyNotificationReceiver extends BroadcastReceiver {
    public static final String RESUME_ACTION = "RESUME_ACTION";
    public static final String STOP_ACTION = "STOP_ACTION";
    public static final String CANCEL_ACTION = "CANCEL_ACTION";
    public static final String NEXT_ACTION = "NEXT_ACTION";
    public static final String AUDIOFOCUS_GAIN = "AUDIOFOCUS_GAIN";
    public static final String AUDIOFOCUS_LOSS = "AUDIOFOCUS_LOSS";
    public static final String AUDIOFOCUS_LOSS_TRANSIENT = "AUDIOFOCUS_LOSS_TRANSIENT";
    public static int REQUEST_CODE_NOTIFICATION = 1212;
    public static int REQUEST_CODE = 10;
    Media_list_activity mediaListActivity  ;

    public void setMainActivityHandler(Media_list_activity main) {
        mediaListActivity = main;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.e("action", action);
        Log.e("action", action);




        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case RESUME_ACTION:


                    if (!mp.isPlaying()) {
                        mp.start();

                        serviceIntent.setClass(context, NotificationService.class);
                        serviceIntent.setAction(Constants.ACTION.AUDIOFOCUS_LOSS);
                        context.startService(serviceIntent);



                        //Media_list_activity.playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                        Media_list_activity.playBtn.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_PAUSE);
                        Media_list_activity.ply.setBackgroundResource(R.drawable.ic_pause_black_24dp);

                        NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                        NotificationService.notification.setCustomContentView(NotificationService.notificationView);

                        NotificationService.notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                        NotificationService.notification.setCustomContentView(NotificationService.notificationView1);
                        // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                        NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());
                        Toasty.success(context, "Resume", Toast.LENGTH_SHORT).show();

                    } else {
                        mp.pause();
                       // Media_list_activity.playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                        Media_list_activity.playBtn.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                        Media_list_activity.ply.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);

                        NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                        NotificationService.notification.setCustomContentView(NotificationService.notificationView);

                        NotificationService.notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                        NotificationService.notification.setCustomContentView(NotificationService.notificationView1);

                        // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                        NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());
                        Toasty.success(context, "Pause", Toast.LENGTH_SHORT).show();
                    }

// you resume action
                    break;
                case STOP_ACTION:


                    if (isAppOnForeground(context, "com.example.czgame.wordbank")) {
                        // App is in Foreground
                        if (!mp.isPlaying()) {
                            // mediaListActivity.mp.stop();

                        } else {
                            mp.pause();

                        }

                    } else {
                        // App is in Background
                        if (!mp.isPlaying()) {
                            mp.stop();

                        } else {
                            mp.pause();

                        }

                        Intent service = new Intent(context, NotificationService.class);
                        service.setClass(context, NotificationService.class);
                        context.stopService(service);
                    }


                    Toast.makeText(context, "stop", Toast.LENGTH_SHORT).show();
                    System.out.println("stop");
// you stop action
                    break;
                case CANCEL_ACTION:
                    //if(mediaListActivity.mp.isPlaying()){


                    ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                    Log.d("topActivity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());

                    try {
                        if(!taskInfo.get(0).topActivity.getClassName().equals("com.example.czgame.wordbank.ui.media.Media_list_activity")){
                            prevsong(context);
                        }else {
                            System.out.println(position);
                            context.sendBroadcast(new Intent(Constants.ACTION.PREV_ACTION));

                        }

                    } catch (Exception e) {

                    }
                    //Media_list_activity.nxt = true;
                    //MediaActivity.playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);

                    //}


                    Toasty.success(context, "Previous.", Toast.LENGTH_SHORT).show();


// you cancel action
                    break;
                case NEXT_ACTION:
                    //if(mediaListActivity.mp.isPlaying()){


                    ActivityManager am1 = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> taskInfo1 = am1.getRunningTasks(1);
                    Log.d("topActivity", "CURRENT Activity ::" + taskInfo1.get(0).topActivity.getClassName());

                    try {
                        if(!taskInfo1.get(0).topActivity.getClassName().equals("com.example.czgame.wordbank.ui.media.Media_list_activity")){
                            nxtsong(context);
                        }else {
                            System.out.println(position);
                            context.sendBroadcast(new Intent(Constants.ACTION.NEXT_ACTION));

                        }

                    } catch (Exception e) {

                    }




                    //Media_list_activity.nxt = true;
                    //MediaActivity.playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);

                    //}
                    Toasty.success(context, "Next.", Toast.LENGTH_SHORT).show();
                    break;
                case AUDIOFOCUS_GAIN:

                    if (!mp.isPlaying()) {
                        // Stopping
                        mp.start();

                    } else{
                        // Playing


                    }
                    Media_list_activity.playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp);

                    NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                    NotificationService.notification.setCustomContentView(NotificationService.notificationView);

                    NotificationService.notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                    NotificationService.notification.setCustomContentView(NotificationService.notificationView1);
                    // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                    NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());
                    Log.d("receiver is ","GAIN");
                    break;
                case AUDIOFOCUS_LOSS:
                    //play();
                    if (!mp.isPlaying()) {
                        // Stopping();


                    } else{
                        // Playing

                        mp.pause();



                    }



                    Media_list_activity.playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);

                    NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                    NotificationService.notification.setCustomContentView(NotificationService.notificationView);

                    NotificationService.notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                    NotificationService.notification.setCustomContentView(NotificationService.notificationView1);

                    // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                    NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());
                    Log.d("receiver is ","LOSS");
                    break;
                case AUDIOFOCUS_LOSS_TRANSIENT:
                    mp.pause();
                    Media_list_activity.playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);

                    NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                    NotificationService.notification.setCustomContentView(NotificationService.notificationView);

                    NotificationService.notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
                    NotificationService.notification.setCustomContentView(NotificationService.notificationView1);

                    // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                    NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());
                    break;
            }
        }
    }

    public void nxtsong(Context context) {

            // startService(view);

            //System.out.println(pro);
            final int[] mutedColor = new int[1];

            mp.stop();
            mp = new MediaPlayer();

            if (position + 1 >= Media_list_activity.ListElementsArrayList.size()) {
                position = -1;
            }
            position++;
            {

                // playBtn = (Button) findViewById(R.id.playBtn);
                // elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
                //  remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

                // Media Player


                String title = Media_list_activity.ListElementsArrayList.get(position).getTitle();
                String artist = Media_list_activity.ListElementsArrayList.get(position).getArtist();
                String album = Media_list_activity.ListElementsArrayList.get(position).getAlbum();


                String titleq = Media_list_activity.ListElementsArrayList.get(position).getImagepath();
                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

                Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
                ContentResolver res = context.getContentResolver();
                InputStream in;
                Bitmap bm = null;
                try {
                    in = res.openInputStream(uri);

                    bm = BitmapFactory.decodeStream(in);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    InputStream is = context.getResources().openRawResource(R.raw.image);
                    bm = BitmapFactory.decodeStream(is);
                }


                Bitmap finalBm = bm;
                Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        // Use generated instance
                        //work with the palette here
                        int defaultValue = 0x000000;
                        int vibrant = getDominantColor(finalBm);
                        int vibrantLight = palette.getLightVibrantColor(defaultValue);
                        int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                        int muted = palette.getMutedColor(defaultValue);
                        int mutedLight = palette.getLightMutedColor(defaultValue);
                        int mutedDark = getComplimentColor(vibrant);
                        mutedColor[0] = mutedDark;


                        if (vibrant == 0) {
                            mutedDark = palette.getDarkVibrantColor(vibrant);
                        }


                    }
                });


                String[] abspath = getAudioPath(context, title);

                try {
                    if (!mp.isPlaying()) {
                        mp.setDataSource(abspath[0]);
                        mp.prepare();

                    } else {


                    }
                } catch (Exception e) {

                    System.out.println(e.getMessage());
                }


                //   if(! other.equals("yes") ) {
                mp.setLooping(false);
                mp.seekTo(0);

                mp.start();

                mp.setVolume(2.5f, 2.5f);



            Intent intenta = new Intent(context, NotificationService.class);

            intenta.putExtra("p", position);
            intenta.setClass(context, NotificationService.class);
            intenta.setAction(Constants.ACTION.NEXT_ACTION);
            context.startService(intenta);

        }


    }


    public void prevsong(Context context) {

        // startService(view);

        //System.out.println(pro);
        final int[] mutedColor = new int[1];

        mp.stop();
        mp = new MediaPlayer();

        if (position - 1 < 0) {
            position = Media_list_activity.ListElementsArrayList.size();
        }
        position--;
        {

            // playBtn = (Button) findViewById(R.id.playBtn);
            // elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
            //  remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

            // Media Player


            String title = Media_list_activity.ListElementsArrayList.get(position).getTitle();
            String artist = Media_list_activity.ListElementsArrayList.get(position).getArtist();
            String album = Media_list_activity.ListElementsArrayList.get(position).getAlbum();


            String titleq = Media_list_activity.ListElementsArrayList.get(position).getImagepath();
            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(titleq));
            ContentResolver res = context.getContentResolver();
            InputStream in;
            Bitmap bm = null;
            try {
                in = res.openInputStream(uri);

                bm = BitmapFactory.decodeStream(in);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                InputStream is = context.getResources().openRawResource(R.raw.image);
                bm = BitmapFactory.decodeStream(is);
            }


            Bitmap finalBm = bm;
            Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    // Use generated instance
                    //work with the palette here
                    int defaultValue = 0x000000;
                    int vibrant = getDominantColor(finalBm);
                    int vibrantLight = palette.getLightVibrantColor(defaultValue);
                    int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                    int muted = palette.getMutedColor(defaultValue);
                    int mutedLight = palette.getLightMutedColor(defaultValue);
                    int mutedDark = getComplimentColor(vibrant);
                    mutedColor[0] = mutedDark;


                    if (vibrant == 0) {
                        mutedDark = palette.getDarkVibrantColor(vibrant);
                    }


                }
            });


            String[] abspath = getAudioPath(context, title);

            try {
                if (!mp.isPlaying()) {
                    mp.setDataSource(abspath[0]);
                    mp.prepare();

                } else {


                }
            } catch (Exception e) {

                System.out.println(e.getMessage());
            }


            //   if(! other.equals("yes") ) {
            mp.setLooping(false);
            mp.seekTo(0);

            mp.start();

            mp.setVolume(2.5f, 2.5f);



            Intent intenta = new Intent(context, NotificationService.class);

            intenta.putExtra("p", position);
            intenta.setClass(context, NotificationService.class);
            intenta.setAction(Constants.ACTION.PREV_ACTION);
            context.startService(intenta);

        }


    }

    private boolean isAppOnForeground(Context context, String appPackageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
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
            } else {
                //App is closed
            }
        }
        return false;
    }

    private String[] getAudioPath(Context context, String songTitle) {

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

    public int getComplimentColor(int color) {
        // get existing colors
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int blue = Color.blue(color);
        int green = Color.green(color);

        // find compliments
        red = (~red) & 0xff;
        blue = (~blue) & 0xff;
        green = (~green) & 0xff;

        return Color.argb(alpha, red, green, blue);
    }

    public int getDominantColor(Bitmap bitmap) {
        List<Palette.Swatch> swatchesTemp = Palette.from(bitmap).generate().getSwatches();
        List<Palette.Swatch> swatches = new ArrayList<Palette.Swatch>(swatchesTemp);
        Collections.sort(swatches, new Comparator<Palette.Swatch>() {
            @Override
            public int compare(Palette.Swatch swatch1, Palette.Swatch swatch2) {
                return swatch2.getPopulation() - swatch1.getPopulation();
            }

        });
        return swatches.size() > 0 ? swatches.get(0).getRgb() : Color.WHITE;
    }
    public  boolean isAppRunning(Context context) {
        ActivityManager m = (ActivityManager) context.getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList =  m.getRunningTasks(10);
        Iterator<ActivityManager.RunningTaskInfo> itr = runningTaskInfoList.iterator();
        int n=0;
        while(itr.hasNext()){
            n++;
            itr.next();
        }
        // App is killed
        return n != 1;// App is in background or foreground
    }


}