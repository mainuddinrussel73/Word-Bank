package com.example.czgame.wordbank.ui.backup_scheudle;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Locale;

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
                    word.put("MEANING", cursor.getString(2));
                    word.put("SENTENCE", cursor.getString(3));
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
        pro_back(context);
        news_back(context);
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