package com.example.czgame.wordbank.ui.alertme;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.czgame.wordbank.R;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.RequiresApi;

public class AlarmNotification extends Activity
{
    private final String TAG = "AlarmMe";
    private final long[] mVibratePattern = { 0, 500, 500 };
    private Ringtone mRingtone;
    private Vibrator mVibrator;
    private boolean mVibrate;
    private Uri mAlarmSound;
    private long mPlayTime;
    private Timer mTimer = null;
    private Alarm mAlarm;
    private DateTime mDateTime;
    private TextView mTextView;
    private PlayTimerTask mTimerTask;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.alarm_notification);

        mDateTime = new DateTime(this);
        mTextView = findViewById(R.id.alarm_title_text);

        readPreferences();

        mRingtone = RingtoneManager.getRingtone(getApplicationContext(), mAlarmSound);
        if (mVibrate)
            mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        start(getIntent());
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i(TAG, "AlarmNotification.onDestroy()");

        stop();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Log.i(TAG, "AlarmNotification.onNewIntent()");

        addNotification(mAlarm);

        stop();
        start(intent);
    }

    private void start(Intent intent)
    {
        mAlarm = new Alarm(this);
        mAlarm.fromIntent(intent);

        Log.i(TAG, "AlarmNotification.start('" + mAlarm.getTitle() + "')");

        mTextView.setText(mAlarm.getTitle());

        mTimerTask = new PlayTimerTask();
        mTimer = new Timer();
        mTimer.schedule(mTimerTask, mPlayTime);
        mRingtone.play();
        if (mVibrate)
            mVibrator.vibrate(mVibratePattern, 0);
    }

    private void stop()
    {
        Log.i(TAG, "AlarmNotification.stop()");

        mTimer.cancel();
        mRingtone.stop();
        if (mVibrate)
            mVibrator.cancel();
    }

    public void onDismissClick(View view)
    {
        finish();
    }

    private void readPreferences()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        mAlarmSound = Uri.parse(prefs.getString("alarm_sound_pref", "DEFAULT_RINGTONE_URI"));
        mVibrate = prefs.getBoolean("vibrate_pref", true);
        mPlayTime = (long)Integer.parseInt(prefs.getString("alarm_play_time_pref", "30")) * 1000;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addNotification(Alarm alarm)
    {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        PendingIntent activity;
        Intent intent;

        Log.i(TAG, "AlarmNotification.addNotification(" + alarm.getId() + ", '" + alarm.getTitle() + "', '" + mDateTime.formatDetails(alarm) + "')");

        intent = new Intent(this.getApplicationContext(), AlarmMe.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        activity = PendingIntent.getActivity(this, (int)alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationChannel channel = new NotificationChannel("alarmme_016", "AlarmMe Notifications",
                NotificationManager.IMPORTANCE_HIGH);

        notification = new Notification.Builder(this)
                .setContentIntent(activity)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setContentTitle("Missed alarm: " + alarm.getTitle())
                .setContentText(mDateTime.formatDetails(alarm))
                .setChannelId("alarmme_016")
                .build();

        notificationManager.createNotificationChannel(channel);

        notificationManager.notify((int)alarm.getId(), notification);
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    private class PlayTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            Log.i(TAG, "AlarmNotification.PalyTimerTask.run()");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                addNotification(mAlarm);
            }
            finish();
        }
    }
}