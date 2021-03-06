package com.example.czgame.wordbank.ui.backup_scheudle;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.news.DBNewsHelper;
import com.example.czgame.wordbank.ui.promotodo.DBproHandle;
import com.example.czgame.wordbank.ui.words.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import androidx.core.app.NotificationCompat;
import es.dmoral.toasty.Toasty;

public class receive_back  extends BroadcastReceiver {


    //the method will be fired when the alarm is triggerred
    @Override
    public void onReceive(Context context, Intent intent) {

        //you can check the log that it is fired
        //Here we are actually not doing anything
        //but you can do any task here that you want to be done a
        // t a specific time everyday
        Log.d("MyAlarmBelal", "Alarm just fired");

        DatabaseHelper mDBHelper  = new DatabaseHelper(context);

        JSONArray jsonArray = new JSONArray();
        final Cursor cursor = mDBHelper.getAllData();
        final Cursor cursor1 = mDBHelper.getAllData2();

        // looping through all rows and adding to list
        if (cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

                JSONObject word = new JSONObject();
                try {
                    word.put("ID", Integer.parseInt(cursor.getString(0)));
                    word.put("WORD", cursor.getString(1));
                    word.put("MEANINGB", cursor.getString(2));
                    word.put("MEANINGE", cursor.getString(3));
                    word.put("SENTENCE", cursor.getString(4));
                    word.put("SYNONYM", cursor.getString(5));
                    word.put("ANTONYM", cursor.getString(5));
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
            File file = new File(dir, "backup.json");
            try {
                FileOutputStream f = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(f);
                pw.write(jsonArray.toString());
                pw.flush();
                pw.close();
                f.close();
               // Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
               // Toasty.error(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                //Toasty.error(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
            }

        } else {

            //showMessage("Error", "Nothing found");
        }

        jsonArray = new JSONArray();
        if (cursor1.getCount() != 0) {
            // show message
            while (cursor1.moveToNext()) {

                JSONObject word = new JSONObject();
                try {
                    word.put("ID", Integer.parseInt(cursor1.getString(0)));
                    word.put("WORD", cursor1.getString(1));
                    word.put("SENTENCE", cursor1.getString(2));
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
            File file = new File(dir, "backup_sentences.json");
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
                //Toasty.error(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                //Toasty.error(getApplicationContext(), "Opps.", Toast.LENGTH_SHORT).show();
            }

        } else {


            //showMessage("Error", "Nothing found");
        }
        pro_back(context);
        news_back(context);

        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_backup_black) // notification icon
                .setContentTitle("Done!") // title for notification
                .setContentText("Daily back completed") // message for notification
                .setAutoCancel(true); // clear notification after click
        @SuppressLint("WrongConstant")
        PendingIntent pi = PendingIntent.getActivity(context,0,intent,Intent.FLAG_ACTIVITY_NEW_TASK);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(7, mBuilder.build());



    }

    public void showMessage(Context context,String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void pro_back(Context context){
        DBproHandle mDBHelper = new DBproHandle(context);
        JSONArray jsonArray = new JSONArray();
        final Cursor cursor = mDBHelper.getAllData();

        // looping through all rows and adding to list
        if (cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

                JSONObject word = new JSONObject();
                try {
                    word.put("ID", Integer.parseInt(cursor.getString(0)));
                    word.put("TITLE", cursor.getString(1));
                    word.put("NUM", cursor.getInt(2));
                    word.put("COMPLETED", cursor.getInt(3));
                    word.put("ISREPEAT", cursor.getInt(4));
                    word.put("DUE_DATE", cursor.getString(5));
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
            File file = new File(dir, "backup_promotodos.json");
            try {
                FileOutputStream f = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(f);
                pw.write(jsonArray.toString());
                pw.flush();
                pw.close();
                f.close();
                Toasty.success(context, "Done.", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toasty.error(context, "Opps.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toasty.error(context, "Opps.", Toast.LENGTH_SHORT).show();
            }

        } else {

            showMessage(context,"Error", "Nothing found");
        }
    }
    public void news_back(Context context){
        DBNewsHelper mDBHelper = new DBNewsHelper(context);
        JSONArray jsonArray = new JSONArray();
        final Cursor cursor = mDBHelper.getAllData();

        // looping through all rows and adding to list
        if (cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

                JSONObject word = new JSONObject();
                try {
                    word.put("ID", Integer.parseInt(cursor.getString(0)));
                    word.put("TITLE", cursor.getString(1));
                    word.put("BODY", cursor.getString(2));
                    word.put("URL", cursor.getString(3));
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
            File file = new File(dir, "backup_news.json");
            try {
                FileOutputStream f = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(f);
                pw.write(jsonArray.toString());
                pw.flush();
                pw.close();
                f.close();
                Toasty.success(context, "Done.", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toasty.error(context, "Opps.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toasty.error(context, "Opps.", Toast.LENGTH_SHORT).show();
            }

        } else {

            showMessage(context,"Error", "Nothing found");
        }


    }


}