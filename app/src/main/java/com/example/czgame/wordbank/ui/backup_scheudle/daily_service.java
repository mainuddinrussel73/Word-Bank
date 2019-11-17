package com.example.czgame.wordbank.ui.backup_scheudle;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.example.czgame.wordbank.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
        String []days = {"SUN","MON", "TUR", "WED", "THU", "FRI","SAT"};

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
        boolean b = dbDaily.insertAll(dayLongName.toUpperCase().substring(0,3),String.valueOf(currentWEEK),months[currentMonth-1],String.valueOf(currentYear),prefs1.getString("t","0"));


        if(b) {
            SharedPreferences.Editor editor1 = prefs1.edit();
            editor1.putInt("t", 0);
            editor1.commit();
        }

        JSONArray jsonArray = new JSONArray();
        final Cursor cursor = dbDaily.getAll();

        // looping through all rows and adding to list
        if (cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

                JSONObject word = new JSONObject();
                try {
                    word.put("ID", Integer.parseInt(cursor.getString(0)));
                    word.put("DAY", cursor.getString(1));
                    word.put("WEEK", cursor.getString(2));
                    word.put("MONTH", cursor.getString(3));
                    word.put("YEAR", cursor.getString(4));
                    word.put("TIME", cursor.getString(5));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                jsonArray.put(word);
            }

            File root = android.os.Environment.getExternalStorageDirectory();
            // http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
            File dir = new File(root.getAbsolutePath() + "/wordstore");
            dir.mkdirs();
            File file = new File(dir, "backup_daily.json");
            try {
                FileOutputStream f = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(f);
                pw.write(jsonArray.toString());
                pw.flush();
                pw.close();
                f.close();
                //Toasty.success(context, "Done.", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //Toasty.error(context, "Opps.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                //Toasty.error(context, "Opps.", Toast.LENGTH_SHORT).show();
            }

        } else {

           // showMessage("Error", "Nothing found");
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
