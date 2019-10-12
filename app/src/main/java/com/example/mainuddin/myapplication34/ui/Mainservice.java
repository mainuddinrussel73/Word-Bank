package com.example.mainuddin.myapplication34.ui;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainuddin.myapplication34.MainActivity;
import com.example.mainuddin.myapplication34.R;
import com.example.mainuddin.myapplication34.ui.Quiz.quiz_page;
import com.example.mainuddin.myapplication34.ui.data.DatabaseHelper;
import com.example.mainuddin.myapplication34.ui.data.word;
import com.example.mainuddin.myapplication34.ui.data.word_details;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.mainuddin.myapplication34.MainActivity.contactList;

public class Mainservice extends IntentService {

    TextView content_title;
    TextView content_title1;
    TextView notification_message;
    word word ;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private String NOTIFICATION_TITLE = "Notification Sample App";
    private String CONTENT_TEXT = "Expand me to see a detailed message!";
    private static final String CHANNEL_ID = "channel_id01";
    public static final int NOTIFICATION_ID = 1;
    //SharedPreferences prefs ;
    public Mainservice() {
        super("mainservice");
    }


    public Mainservice(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.IntentService#onHandleIntent(android.content.Intent)
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        showNotification();





    }

    private void showNotification() {

        createNotificationChannel();


        double randomDouble = Math.random();


        randomDouble = randomDouble * MainActivity.sizee.getInt( "size", 0 ) + 0;
        int randomInt = (int) randomDouble;
        word = randomword(randomInt);
        System.out.println(MainActivity.sizee.getInt( "size", 0 ));



        //inflating the views (custom_normal.xml and custom_expanded.xml)
        RemoteViews remoteCollapsedViews = new RemoteViews(getPackageName(), R.layout.view_collapsed_notification);
        RemoteViews remoteExpandedViews = new RemoteViews(getPackageName(), R.layout.view_expanded_notification);


        remoteCollapsedViews.setTextViewText(R.id.content_title,word.getWORD());

        remoteExpandedViews.setTextViewText(R.id.content_title,"Today's word is : "+word.getWORD());
        remoteExpandedViews.setTextViewText(R.id.notification_message,word.getMEANING());



        //start this(MainActivity) on by Tapping notification
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent mainPIntent = PendingIntent.getActivity(this, 0,
                mainIntent, PendingIntent.FLAG_ONE_SHOT);

        //creating notification


        remoteExpandedViews.setOnClickPendingIntent(R.id.left_button, mainPIntent);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        //icon
        builder.setSmallIcon(R.drawable.ic_pawprint);
        //set priority
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //dismiss on tap
        builder.setAutoCancel(true);
        //start intent on notification tap (MainActivity)
        builder.setContentIntent(mainPIntent);
        //custom style
        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());


        builder.setCustomContentView(remoteCollapsedViews);
        builder.setCustomBigContentView(remoteExpandedViews);

        //notification manager
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);


        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
        System.out.println("created");



    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "My Notification";
            String description = "My notification description";
            //importance of your notification
            int importance = NotificationManager.IMPORTANCE_DEFAULT;


            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    public word randomword(int iii){



        mDBHelper = new DatabaseHelper(this);


        //mDBHelper.getAllData();
        mDb = mDBHelper.getReadableDatabase();



        // Select All Query
        // String selectQuery = "SELECT  * FROM " + "Word_table";

        String query = "SELECT * FROM Word_table WHERE ID =" + iii;

        Cursor cursor = mDb.rawQuery(query,null);

        word word = new word();
        while (cursor.moveToNext()) {

            word.setID( Integer.parseInt(cursor.getString(0)));
            word.setWORD( cursor.getString(1));
            word.setMEANING(cursor.getString(2));

        }

        return word;
    }


}