package com.example.mainuddin.myapplication34.ui.media;



import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.MainActivity;
import com.example.mainuddin.myapplication34.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationService extends Service {

    public static  RemoteViews notificationView;
    public static  RemoteViews notificationView1;
    public static  PendingIntent pendingIntentYes;
    public static  PendingIntent pendingIntentNo;
    public static   NotificationManager manager;
    public  static NotificationCompat.Builder notificationBuilder;
    public static NotificationCompat.Builder notification;
    public  static  Intent yesReceive = new Intent();
    public  static  Intent noReceive = new Intent();

    String title;
    String artist;
    String album;
    int p;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent || null == intent.getAction ()) {
            String source = null == intent ? "intent" : "action";
            Log.e (LOG_TAG, source + " was null, flags=" + flags + " bits=" + Integer.toBinaryString (flags));
            return START_STICKY;
        }
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            try{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    p = intent.getIntExtra("p",0);
                    startMyOwnForeground();
                }

            }
            catch (Exception e){
                System.out.println(e.getMessage());

            }
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();


            title = intent.getStringExtra("pos");
            artist = intent.getStringExtra("art");
            album = intent.getStringExtra("alb");

        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
            Log.i("ok", "Clicked Previous");
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
            Log.i("ok", "Clicked Play");
        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {

            Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();
            Log.i("ok", "Clicked Next");
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i("ok", "Received Stop Foreground Intent");
            Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    Notification status;
    private final String LOG_TAG = "NotificationService";

    private void showActionButtonsNotification() {


        Notification.Builder notif;
        NotificationManager nm;
        notif = new Notification.Builder(getApplicationContext());
        notif.setSmallIcon(R.mipmap.ic_launcher);
        notif.setContentTitle("Hi there!");
        notif.setContentText("This is even more text.");
        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notif.setSound(path);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent yesReceive = new Intent();
        yesReceive.setAction(MyNotificationReceiver.RESUME_ACTION);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, MyNotificationReceiver.REQUEST_CODE_NOTIFICATION, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        notif.addAction(R.drawable.play, "resume", pendingIntentYes);


        Intent yesReceive2 = new Intent();
        yesReceive2.setAction(MyNotificationReceiver.STOP_ACTION);
        PendingIntent pendingIntentYes2 = PendingIntent.getBroadcast(this, MyNotificationReceiver.REQUEST_CODE_NOTIFICATION, yesReceive2, PendingIntent.FLAG_UPDATE_CURRENT);
        notif.addAction(R.drawable.stop, "stop", pendingIntentYes2);




        Intent maybeReceive2 = new Intent();
        maybeReceive2.setAction(MyNotificationReceiver.CANCEL_ACTION);
        PendingIntent pendingIntentMaybe2 = PendingIntent.getBroadcast(this, MyNotificationReceiver.REQUEST_CODE_NOTIFICATION, maybeReceive2, PendingIntent.FLAG_UPDATE_CURRENT);
        notif.addAction(R.drawable.ic_cancel_black_24dp, "cancel", pendingIntentMaybe2);


        assert nm != null;
        nm.notify(MyNotificationReceiver.REQUEST_CODE, notif.getNotification());


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startMyOwnForeground(){
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

            bm= BitmapFactory.decodeStream(in);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            InputStream is = this.getResources().openRawResource(R.raw.image);
            bm = BitmapFactory.decodeStream(is);
        }


        notificationView1 = new RemoteViews(getPackageName(), R.layout.status_bar );

        notificationView1.setTextViewText(R.id.status_bar_track_name,Media_list_activity.ListElementsArrayList.get((p)).getTitle());

        notificationView = new RemoteViews(getPackageName(), R.layout.status_bar_expanded);

        notificationView.setTextViewText(R.id.status_bar_track_name,Media_list_activity.ListElementsArrayList.get((p)).getTitle());
        notificationView.setTextViewText(R.id.status_bar_artist_name,Media_list_activity.ListElementsArrayList.get((p)).getArtist());

        notificationView.setImageViewBitmap(R.id.status_bar_album_art,bm);


        if(!MediaActivity.mp.isPlaying()) {

            notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_play_arrow_black_24dp);


        }else if(MediaActivity.mp.isPlaying()){

            notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);


        }




        yesReceive = new Intent();
        yesReceive.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        yesReceive.setAction(MyNotificationReceiver.RESUME_ACTION);
        pendingIntentYes = PendingIntent.getBroadcast(this, MyNotificationReceiver.REQUEST_CODE_NOTIFICATION, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationView.setOnClickPendingIntent(R.id.status_bar_play, pendingIntentYes);
        notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setCustomContentView(notificationView1)
                .setCustomBigContentView(notificationView);
        notification.setCustomContentView(notificationView);
        startForeground(2, notification.build());




    }
    private void showNotification() {
// Using RemoteViews to bind custom layouts into Notification
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        RemoteViews bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);

// showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        bigViews.setImageViewBitmap(R.id.status_bar_album_art,
                Constants.getDefaultAlbumArt(this));

        Intent notificationIntent = new Intent(this, MediaActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, NotificationService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, NotificationService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setImageViewResource(R.id.status_bar_play,
                R.drawable.ic_pause_black_24dp);
        bigViews.setImageViewResource(R.id.status_bar_play,
                R.drawable.ic_pause_black_24dp);

        views.setTextViewText(R.id.status_bar_track_name, "Song Title");
        bigViews.setTextViewText(R.id.status_bar_track_name, "Song Title");

        views.setTextViewText(R.id.status_bar_artist_name, "Artist Name");
        bigViews.setTextViewText(R.id.status_bar_artist_name, "Artist Name");

        bigViews.setTextViewText(R.id.status_bar_album_name, "Album Name");

        status = new Notification.Builder(this).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.ic_launcher_background;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }


}