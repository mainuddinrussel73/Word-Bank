package com.example.czgame.wordbank.ui.media;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.czgame.wordbank.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import es.dmoral.toasty.Toasty;
import mkaflowski.mediastylepalette.MediaNotificationProcessor;

import static com.example.czgame.wordbank.ui.media.Media_list_activity.mLrcView;
import static com.example.czgame.wordbank.ui.media.Media_list_activity.mVisualizer;
import static com.example.czgame.wordbank.ui.media.Media_list_activity.mp;
import static com.example.czgame.wordbank.ui.media.Media_list_activity.playBtn;
import static com.example.czgame.wordbank.ui.media.Media_list_activity.posit;

public class NotificationService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener {

    public static RemoteViews notificationView;
    public static RemoteViews notificationView1;
    public static RemoteViews notificationView3;
    public static PendingIntent pendingIntentYes;
    public static PendingIntent pendingIntentNo;
    public static PendingIntent pendingIntentNx;
    public static PendingIntent pendingIntenttogg;
    public static NotificationManager manager;
    public static NotificationCompat.Builder notificationBuilder;
    public static NotificationCompat.Builder notification;
    public static Intent yesReceive = new Intent();
    public static Intent noReceive = new Intent();
    public static Intent nxReceive = new Intent();
    // Binder given to clients
    private final IBinder iBinder = new LocalBinder();
    private final String LOG_TAG = "NotificationService";
    public static final String COUNTDOWN_BR = "com.example.czgame.wordgame.music_br";
    AudioManager am = null;
    String title;
    String artist;
    String album;
    public static Intent bi = new Intent(COUNTDOWN_BR);
    public static Timer mTimer = null;    //timer handling
    int p;
    Notification status;
    private AudioManager audioManager;

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }
    public  int getDominantColor(Bitmap bitmap) {
        MediaNotificationProcessor processor = new MediaNotificationProcessor(NotificationService.this, bitmap); // can use drawable

        int backgroundColor = processor.getBackgroundColor();
        int primaryTextColor = processor.getPrimaryTextColor();
        int secondaryTextColor = processor.getSecondaryTextColor();

        return  backgroundColor;
    }


    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        //Focus gained
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        //Could not gain focus
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent || null == intent.getAction()) {
            String source = null == intent ? "intent" : "action";
            Log.e(LOG_TAG, source + " was null, flags=" + flags + " bits=" + Integer.toBinaryString(flags));
            return START_STICKY;
        }
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            try {
                p = intent.getIntExtra("p", 0);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


                    // implementation reference

                    if (requestAudioFocus() == false) {
                        //Could not gain focus
                        stopSelf();
                    }
                    bi.setClass(NotificationService.this, MyNotificationReceiver.class);
                    startMyOwnForeground();


                }

                System.out.println(p);

              //  AsynchTaskTimer();

            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
            Toasty.success(this, "Song Playing", Toast.LENGTH_SHORT).show();


            title = intent.getStringExtra("pos");
            artist = intent.getStringExtra("art");
            album = intent.getStringExtra("alb");

        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            p = intent.getIntExtra("p", 0);

            // implementation reference

            startMyOwnForeground();
            //AsynchTaskTimer();

            Toasty.success(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
            Log.i("ok", "Clicked Previous");
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
          //  AsynchTaskTimer();
            Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
            Log.i("ok", "Clicked Play");
        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {

            p = intent.getIntExtra("p", 0);

            // implementation reference


            startMyOwnForeground();
           // AsynchTaskTimer();
            Toasty.success(this, "Clicked Next", Toast.LENGTH_SHORT).show();
            Log.i("ok", "Clicked Next");
        }else if (intent.getAction().equals(Constants.ACTION.AUDIOFOCUS_LOSS)) {

            //AsynchTaskTimer();
            Log.i("ok", "Clicked Gain");
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {


            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Stopfunc();
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
            Log.i("ok", "Received Stop Foreground Intent");
            Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Stopfunc() {

        yesReceive = new Intent();
        yesReceive.setClass(NotificationService.this, MyNotificationReceiver.class);
        yesReceive.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        yesReceive.setAction(MyNotificationReceiver.STOP_ACTION);
        pendingIntentYes = PendingIntent.getBroadcast(this, MyNotificationReceiver.REQUEST_CODE_NOTIFICATION, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationView.setOnClickPendingIntent(R.id.status_bar_collapse, pendingIntentYes);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";

        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);


        Bitmap bm;
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(Media_list_activity.ListElementsArrayList.get((p)).getImagepath()));
        ContentResolver res = this.getContentResolver();
        InputStream in;
        bm = null;
        try {
            in = res.openInputStream(uri);

            bm = BitmapFactory.decodeStream(in);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            InputStream is = this.getResources().openRawResource(R.raw.image);
            bm = BitmapFactory.decodeStream(is);
        }


        notificationView1 = new RemoteViews(getPackageName(), R.layout.status_bar);
        notificationView1.setTextViewText(R.id.status_bar_track_name, Media_list_activity.ListElementsArrayList.get((p)).getTitle());
        notificationView1.setTextViewText(R.id.status_bar_artist_name, Media_list_activity.ListElementsArrayList.get((p)).getArtist());
        notificationView1.setImageViewBitmap(R.id.status_bar_album_art, bm);

        notificationView = new RemoteViews(getPackageName(), R.layout.status_bar_expanded);
        notificationView.setTextViewText(R.id.status_bar_track_name, Media_list_activity.ListElementsArrayList.get((p)).getTitle());
        notificationView.setTextViewText(R.id.status_bar_artist_name, Media_list_activity.ListElementsArrayList.get((p)).getArtist());
        notificationView.setTextViewText(R.id.status_bar_album_name, Media_list_activity.ListElementsArrayList.get((p)).getArtist());
        notificationView.setImageViewBitmap(R.id.status_bar_album_art, bm);


        if (!Media_list_activity.mp.isPlaying()) {



            notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);
            notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);


        } else if (Media_list_activity.mp.isPlaying()) {

            Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_pause_black_24dp);
            myIcon2.setTint(getComplimentColor(bm));
            notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
            notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);


        }
        int audioSessionId = mp.getAudioSessionId();
        if (audioSessionId != -1){
            mVisualizer.setAudioSessionId(audioSessionId);
        }


        yesReceive = new Intent();
        yesReceive.setClass(NotificationService.this, MyNotificationReceiver.class);
        yesReceive.setAction(MyNotificationReceiver.RESUME_ACTION);
        pendingIntentYes = PendingIntent.getBroadcast(this, MyNotificationReceiver.REQUEST_CODE_NOTIFICATION, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);


        noReceive = new Intent();
        noReceive.setClass(NotificationService.this, MyNotificationReceiver.class);
        noReceive.setAction(MyNotificationReceiver.CANCEL_ACTION);
        pendingIntentNo = PendingIntent.getBroadcast(this, MyNotificationReceiver.REQUEST_CODE_NOTIFICATION, noReceive, PendingIntent.FLAG_UPDATE_CURRENT);




        nxReceive = new Intent();
        nxReceive.setClass(NotificationService.this, MyNotificationReceiver.class);
        nxReceive.setAction(MyNotificationReceiver.NEXT_ACTION);
        pendingIntentNx = PendingIntent.getBroadcast(this, MyNotificationReceiver.REQUEST_CODE_NOTIFICATION, nxReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationView.setOnClickPendingIntent(R.id.status_bar_next, pendingIntentNx);
        notificationView1.setOnClickPendingIntent(R.id.status_bar_next, pendingIntentNx);

        notificationView.setOnClickPendingIntent(R.id.status_bar_prev, pendingIntentNo);
        notificationView1.setOnClickPendingIntent(R.id.status_bar_prev, pendingIntentNo);

        notificationView.setInt(R.id.notificationbg, "setBackgroundColor", getDominantColor(bm));
        notificationView.setInt(R.id.textarea, "setBackgroundColor", getDominantColor(bm));
        notificationView.setInt(R.id.status_bar_track_name, "setBackgroundColor", getDominantColor(bm));
        notificationView.setInt(R.id.status_bar_artist_name, "setBackgroundColor", getDominantColor(bm));
        notificationView.setInt(R.id.status_bar_album_name, "setBackgroundColor", getDominantColor(bm));


        notificationView.setInt(R.id.status_bar_prev, "setColorFilter", getComplimentColor(bm));
        notificationView.setInt(R.id.status_bar_prev, "setBackgroundColor", getDominantColor(bm));

        notificationView.setInt(R.id.status_bar_next, "setColorFilter", getComplimentColor(bm));
        notificationView.setInt(R.id.status_bar_next, "setBackgroundColor", getDominantColor(bm));

        notificationView.setInt(R.id.status_bar_play, "setColorFilter", getComplimentColor(bm));
        notificationView.setInt(R.id.status_bar_play, "setBackgroundColor", getDominantColor(bm));


        notificationView.setTextColor(R.id.status_bar_track_name, getComplimentColor(bm));
        notificationView.setTextColor(R.id.status_bar_artist_name, getComplimentColor(bm));
        notificationView.setTextColor(R.id.status_bar_album_name, getComplimentColor((bm)));


        notificationView1.setOnClickPendingIntent(R.id.status_bar_next, pendingIntentNx);

        notificationView1.setInt(R.id.notificationbg, "setBackgroundColor", getDominantColor(bm));
        notificationView1.setInt(R.id.area, "setBackgroundColor", getDominantColor(bm));
        notificationView1.setInt(R.id.status_bar_track_name, "setBackgroundColor", getDominantColor(bm));
        notificationView1.setInt(R.id.status_bar_artist_name, "setBackgroundColor", getDominantColor(bm));

        notificationView1.setInt(R.id.status_bar_prev, "setColorFilter", getComplimentColor(bm));
        notificationView1.setInt(R.id.status_bar_prev, "setBackgroundColor", getDominantColor(bm));

        notificationView1.setInt(R.id.status_bar_next, "setColorFilter", getComplimentColor(bm));
        notificationView1.setInt(R.id.status_bar_next, "setBackgroundColor", getDominantColor(bm));

        notificationView1.setInt(R.id.status_bar_play, "setColorFilter", getComplimentColor(bm));
        notificationView1.setInt(R.id.status_bar_play, "setBackgroundColor", getDominantColor(bm));

        notificationView1.setTextColor(R.id.status_bar_track_name, getComplimentColor(bm));
        notificationView1.setTextColor(R.id.status_bar_artist_name, getComplimentColor(bm));


        Intent toggle = new Intent();
        toggle.setClass(NotificationService.this, MyNotificationReceiver.class);
        toggle.setAction(MyNotificationReceiver.STOP_ACTION);
        pendingIntenttogg = PendingIntent.getBroadcast(this, MyNotificationReceiver.REQUEST_CODE_NOTIFICATION, toggle, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationView1.setOnClickPendingIntent(R.id.status_bar_collapse, pendingIntenttogg);
        notificationView.setOnClickPendingIntent(R.id.status_bar_collapse, pendingIntenttogg);


        notificationView.setOnClickPendingIntent(R.id.status_bar_play, pendingIntentYes);
        notificationView1.setOnClickPendingIntent(R.id.status_bar_play, pendingIntentYes);
        notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.ic_pawprint);

        // notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC;
//        notificationBuilder.setLargeIcon(R.mipmap.ic_launcher);
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        final MediaSession mediaSession = new MediaSession(this, "debug tag");

      //  notification.setColor(getComplimentColor(bm));
        notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setColor(getDominantColor(bm))
                .setSmallIcon(R.drawable.ic_iconfinder_083_music_183211)
                .setOngoing(true)
                .setCustomContentView(notificationView1)
                .setCustomBigContentView(notificationView);
        notification.setCustomContentView(notificationView);
        notification.setCustomContentView(notificationView1);
        notification.setStyle(new androidx.media.app.NotificationCompat.MediaStyle());
        startForeground(2, notification.build());


    }


    public int getComplimentColor(Bitmap bitmap) {
        // get existing colors
        MediaNotificationProcessor processor = new MediaNotificationProcessor(this, bitmap); // can use drawable

        int backgroundColor = processor.getBackgroundColor();
        int primaryTextColor = processor.getPrimaryTextColor();
        int secondaryTextColor = processor.getSecondaryTextColor();

        return primaryTextColor;
    }


    public  boolean isAppRunning() {
        ActivityManager m = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
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
    private boolean isAppOnForeground(Context context, String appPackageName) {
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
            } else {
                //App is closed
            }
        }
        return false;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mTimer.cancel();

    }

    @Override
    public void onAudioFocusChange(int focusChange) {


                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        // resume playback
                        if (mp == null) ;
                        else if (!mp.isPlaying()) mp.start();
                        mp.setVolume(1.0f, 1.0f);
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        // Lost focus for an unbounded amount of time: stop playback and release media player
                        if (mp.isPlaying()) {
                            posit = mp.getCurrentPosition();
                            mp.stop();

                            //Service is active
                            //Send media with BroadcastReceiver


                                // Stopping
                                mLrcView.resume();
                                playBtn.change(false);

                                NotificationService.notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                                NotificationService.notification.setCustomContentView(NotificationService.notificationView);

                                NotificationService.notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
                                NotificationService.notification.setCustomContentView(NotificationService.notificationView1);
                                // NotificationService.notificationView.setTextViewText(R.id.status_bar_track_name, "pllll");
                                NotificationService.manager.notify(2, NotificationService.notificationBuilder.build());



                        }

                        mp.release();
                        mp = null;

                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        // Lost focus for a short time, but we have to stop
                        // playback. We don't release the media player because playback
                        // is likely to resume
                        if (mp.isPlaying()) mp.pause();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        // Lost focus for a short time, but it's ok to keep playing
                        // at an attenuated level
                        if (mp.isPlaying()) mp.setVolume(0.1f, 0.1f);
                        break;
                }


    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

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

    public class LocalBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }
    }
}