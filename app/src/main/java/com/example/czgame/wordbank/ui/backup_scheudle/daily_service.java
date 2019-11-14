package com.example.czgame.wordbank.ui.backup_scheudle;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.czgame.wordbank.R;

import java.util.Calendar;
import java.util.Locale;

import androidx.core.app.NotificationCompat;

public class daily_service extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        SharedPreferences prefs1 = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        DBDaily dbDaily = new DBDaily(context);
        //dbDaily.deleteAll();
        String []months = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
        String []days = {"MON", "TUR", "WED", "THU", "FRI","SAT","SUN"};

        Calendar calendar = Calendar.getInstance();




        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH)+1;
        int currentWEEK = calendar.get(Calendar.WEEK_OF_YEAR);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        String dayLongName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        // currentDay-=1;

        //  dbDaily.insertAll1("WED",String.valueOf(currentWEEK),months[currentMonth-1],String.valueOf(currentYear),11);
        // dbDaily.insertAll1("THU",String.valueOf(currentWEEK),months[currentMonth-1],String.valueOf(currentYear),3);



        System.out.println(dayLongName.toUpperCase().substring(0,3)+","+months[currentMonth-1]);
        boolean b = dbDaily.insertAll(dayLongName.toUpperCase().substring(0,3),String.valueOf(currentWEEK),months[currentMonth-1],String.valueOf(currentYear),prefs1.getInt("t", 0));

        if(b) {
            SharedPreferences.Editor editor1 = prefs1.edit();
            editor1.putInt("t", 0);
            editor1.commit();
        }

        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_timer_black_24dp) // notification icon
                .setContentTitle("Done!") // title for notification
                .setContentText("Daily update completed") // message for notification
                .setAutoCancel(true); // clear notification after click
        @SuppressLint("WrongConstant")
        PendingIntent pi = PendingIntent.getActivity(context,0,intent,Intent.FLAG_ACTIVITY_NEW_TASK);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(9, mBuilder.build());

        //Toasty.success(context,"Daily Update Done", Toast.LENGTH_LONG).show();

    }
}
