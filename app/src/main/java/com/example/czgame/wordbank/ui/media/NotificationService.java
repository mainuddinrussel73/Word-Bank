package com.example.czgame.wordbank.ui.media;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.czgame.wordbank.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.palette.graphics.Palette;
import es.dmoral.toasty.Toasty;

public class NotificationService extends Service {

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
    private final String LOG_TAG = "NotificationService";
    AudioManager am = null;
    String title;
    String artist;
    String album;
    int p;
    Notification status;

    public static int getDominantColor(Bitmap bitmap) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    p = intent.getIntExtra("p", 0);

                    // implementation reference

                    startMyOwnForeground();

                }

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

            Toasty.success(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
            Log.i("ok", "Clicked Previous");
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
            Log.i("ok", "Clicked Play");
        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {

            p = intent.getIntExtra("p", 0);

            // implementation reference

            startMyOwnForeground();
            Toasty.success(this, "Clicked Next", Toast.LENGTH_SHORT).show();
            Log.i("ok", "Clicked Next");
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
    public void Stopfunc() {

        yesReceive = new Intent();
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

            notificationView.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);
            notificationView1.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_black_24dp);


        }


        yesReceive = new Intent();
        yesReceive.setAction(MyNotificationReceiver.RESUME_ACTION);
        pendingIntentYes = PendingIntent.getBroadcast(this, MyNotificationReceiver.REQUEST_CODE_NOTIFICATION, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);


        noReceive = new Intent();
        noReceive.setAction(MyNotificationReceiver.CANCEL_ACTION);
        pendingIntentNo = PendingIntent.getBroadcast(this, MyNotificationReceiver.REQUEST_CODE_NOTIFICATION, noReceive, PendingIntent.FLAG_UPDATE_CURRENT);




        nxReceive = new Intent();
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
        notificationView.setInt(R.id.status_bar_prev, "setBackgroundColor", getDominantColor(bm));
        notificationView.setInt(R.id.status_bar_next, "setBackgroundColor", getDominantColor(bm));
        notificationView.setInt(R.id.status_bar_play, "setBackgroundColor", getDominantColor(bm));


        notificationView.setTextColor(R.id.status_bar_track_name, getComplimentColor(getDominantColor(bm)));
        notificationView.setTextColor(R.id.status_bar_artist_name, getComplimentColor(getDominantColor(bm)));
        notificationView.setTextColor(R.id.status_bar_album_name, getComplimentColor(getDominantColor(bm)));


        notificationView1.setOnClickPendingIntent(R.id.status_bar_next, pendingIntentNx);

        notificationView1.setInt(R.id.notificationbg, "setBackgroundColor", getDominantColor(bm));
        notificationView1.setInt(R.id.area, "setBackgroundColor", getDominantColor(bm));
        notificationView1.setInt(R.id.status_bar_track_name, "setBackgroundColor", getDominantColor(bm));
        notificationView1.setInt(R.id.status_bar_artist_name, "setBackgroundColor", getDominantColor(bm));
        notificationView1.setInt(R.id.status_bar_next, "setBackgroundColor", getDominantColor(bm));
        notificationView1.setInt(R.id.status_bar_prev, "setBackgroundColor", getDominantColor(bm));
        notificationView1.setInt(R.id.status_bar_play, "setBackgroundColor", getDominantColor(bm));
        notificationView1.setTextColor(R.id.status_bar_track_name, getComplimentColor(getDominantColor(bm)));
        notificationView1.setTextColor(R.id.status_bar_artist_name, getComplimentColor(getDominantColor(bm)));


        Intent toggle = new Intent();
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

}