package com.example.mainuddin.myapplication34.ui.promotodo;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class CountService extends Service {

    private final static String TAG = "BroadcastService";

    long total = 10000;
    public static final String COUNTDOWN_BR = " com.example.mainuddin.myapplication34.ui.promotodo.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);

    CountDownTimer cdt = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Starting timer...");

        cdt = new CountDownTimer(total, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                total  = millisUntilFinished;
                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                bi.putExtra("countdown", millisUntilFinished);
                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "Timer finished");
            }
        };

        cdt.start();
    }

    private void onPlay() {
        cdt = new CountDownTimer(total, 1000) {
            public void onTick(long millisUntilFinished) {
                total  = millisUntilFinished;
                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                bi.putExtra("countdown", millisUntilFinished);
                sendBroadcast(bi);
            }
            public void onFinish() {
                Log.i(TAG, "Timer finished");
                //nTimeLabel.setText("done!");
            }
        }.start();
    }
    @Override
    public void onDestroy() {

        cdt.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onPause(){
        cdt.cancel();
    }
}
