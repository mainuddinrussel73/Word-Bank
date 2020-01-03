package com.example.czgame.wordbank.ui.words;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.czgame.wordbank.R;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Mainservice extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel_id01";
    TextView content_title;
    TextView content_title1;
    TextView notification_message;
    word word;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private String NOTIFICATION_TITLE = "Notification Sample App";
    private String CONTENT_TEXT = "Expand me to see a detailed message!";

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


        randomDouble = randomDouble * MainActivity.sizee.getInt("size", 0) + 0;
        int randomInt = (int) randomDouble;
        word = randomword(randomInt);
        System.out.println(MainActivity.sizee.getInt("size", 0));


        //inflating the views (custom_normal.xml and custom_expanded.xml)
        RemoteViews remoteCollapsedViews = new RemoteViews(getPackageName(), R.layout.view_collapsed_notification);
        RemoteViews remoteExpandedViews = new RemoteViews(getPackageName(), R.layout.view_expanded_notification);


        remoteCollapsedViews.setTextViewText(R.id.content_title, word.getWORD());

        remoteExpandedViews.setTextViewText(R.id.content_title, "Today's word is : " + word.getWORD());
        remoteExpandedViews.setTextViewText(R.id.notification_message, word.getMEANINGB());


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

    public word randomword(int iii) {


        mDBHelper = new DatabaseHelper(this);


        //mDBHelper.getAllData();
        mDb = mDBHelper.getReadableDatabase();


        // Select All Query
        // String selectQuery = "SELECT  * FROM " + "Word_table";

        String query = "SELECT * FROM Word_table WHERE ID =" + iii;

        Cursor cursor = mDb.rawQuery(query, null);

        word word = new word();
        while (cursor.moveToNext()) {

            word.setID(Integer.parseInt(cursor.getString(0)));
            word.setWORD(cursor.getString(1));
            word.setMEANINGB(cursor.getString(2));

        }

        return word;
    }


}