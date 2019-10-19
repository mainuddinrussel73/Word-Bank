package com.example.mainuddin.myapplication34.ui.promotodo;

import android.app.AlarmManager;
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
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.ui.media.Constants;
import com.example.mainuddin.myapplication34.ui.media.MediaActivity;
import com.example.mainuddin.myapplication34.ui.media.Media_list_activity;
import com.example.mainuddin.myapplication34.ui.media.MyNotificationReceiver;
import com.example.mainuddin.myapplication34.ui.media.NotificationService;

import java.io.FileNotFoundException;
import java.io.InputStream;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import es.dmoral.toasty.Toasty;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class Promotodo_service extends Service {



    public static RemoteViews notificationView;

    public static NotificationManager manager;
    public  static NotificationCompat.Builder notificationBuilder;
    public static Notification notification;

    public  static  long total = 1800000;

    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "com.example.mainuddin.myapplication34.countdown_br";
    public static Intent bi = new Intent(COUNTDOWN_BR);

    public static CountDownTimer cdt ;
    public static boolean ispause = true;


    @Override
    public void onDestroy() {

        cdt.cancel();

      //  startService(new Intent(this, Promotodo_service.class));

        super.onDestroy();
    }

    public static void pause(){

        ispause = true;
        if(cdt!=null){

            cdt.cancel();
        }
    }

    public  static void resume(Context context){

        ispause = false;
        //cdt.cancel();
        cdt = new CountDownTimer(total, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {


                total = millisUntilFinished;
                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);

                bi.setAction(Promotodo_receiver.GET_TIME);
                bi.putExtra("countdown", millisUntilFinished);

                context.sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "Timer finished");
                bi.setAction(Promotodo_receiver.SET_TIME);
                bi.putExtra("countdown", new Long(0));
                context.sendBroadcast(bi);
                total = 1800000;
            }
        };

        cdt.start();
    }





    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        stattimer();
        return START_STICKY;
    }


    public  void stattimer(){
        ispause = false;

        Log.i(TAG, "Starting timer...");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        }

        cdt = new CountDownTimer(total, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                total = millisUntilFinished;
                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);

                bi.setAction(Promotodo_receiver.GET_TIME);
                bi.putExtra("countdown", millisUntilFinished);

                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "Timer finished");
                bi.setAction(Promotodo_receiver.SET_TIME);
                bi.putExtra("countdown", new Long(0));
                sendBroadcast(bi);
                total = 1800000;
            }
        };

        cdt.start();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.example.myapp";
        String channelName = "My Promotodo Service";

        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);


        notificationView = new RemoteViews(getPackageName(), R.layout.promo_notification);
        notificationView.setTextViewText(R.id.timerview,"HH" );
        notificationView.setTextViewText(R.id.timerview1, "MM");
        notificationView.setTextViewText(R.id.timerview2, "SS");

        notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);


        //notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        notificationBuilder
                .setAutoCancel(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setSmallIcon(R.drawable.ic_timer_white_24dp)
                .setCustomBigContentView(notificationView);


        notification = notificationBuilder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

        startForeground(6, notification);


    }




}
