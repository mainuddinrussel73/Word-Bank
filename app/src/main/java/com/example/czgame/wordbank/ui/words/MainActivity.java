package com.example.czgame.wordbank.ui.words;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.Home.HomeActivity;
import com.example.czgame.wordbank.ui.Quiz.Quiz_confirm;
import com.example.czgame.wordbank.ui.alertme.AlarmMe;
import com.example.czgame.wordbank.ui.backup_scheudle.daily_service;
import com.example.czgame.wordbank.ui.backup_scheudle.receive_back;
import com.example.czgame.wordbank.ui.diary.DiaryMain;
import com.example.czgame.wordbank.ui.media.music_base;
import com.example.czgame.wordbank.ui.news.Editorialonline;
import com.example.czgame.wordbank.ui.news.add_news;
import com.example.czgame.wordbank.ui.news.news_activity;
import com.example.czgame.wordbank.ui.news.news_backup;
import com.example.czgame.wordbank.ui.promotodo.Promotodo_activity;
import com.example.czgame.wordbank.ui.promotodo.TimelineView;
import com.example.czgame.wordbank.ui.promotodo.daily_details;
import com.example.czgame.wordbank.ui.promotodo.pro_backup;
import com.example.czgame.wordbank.ui.promotodo.tree;
import com.google.android.material.navigation.NavigationView;
import com.leondzn.simpleanalogclock.SimpleAnalogClock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int PICKFILE_RESULT_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static Activity mActivity;
    public static int score = 1111111;
    public static boolean isChecked = false;
    public  boolean isDark;
    public  SharedPreferences prefs;

    ActionBarDrawerToggle toggle;
    com.suke.widget.SwitchButton switchButton;



    DrawerLayout drawer;
    Menu menu ;
    private Context mContext;
    NavigationView navigationView1;
    NavigationView navigationView;
    private PopupWindow mPopupWindow;
    private DrawerLayout mRelativeLayout;

    @Override
    protected void onStart() {
        super.onStart();

        getWindow().setBackgroundDrawableResource(android.R.color.white);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);


        isDark = prefs.getBoolean("isDark", false);


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mActivity = this;

        toolbar.setTitleTextColor(getResources().getColor(R.color.material_white));
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {

                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission();
            }
        } else {

        }

      /*  FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), add_page.class);
                //String s = view.findViewById(R.id.subtitle).toString();
                //String s = (String) parent.getI;
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });*/


        ///Intent intent=new Intent(MainActivity.this,quiz_page.class);
        //Intent intent = new Intent(Intent.ACTION_VIEW, null, MainActivity.this, quiz_page.class);


        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = hView.findViewById(R.id.appname);
        TextView toalword = hView.findViewById(R.id.wordcount);
        TextView scores = hView.findViewById(R.id.scores);
        TextView scores1 = hView.findViewById(R.id.scores1);


        SpannableString s = new SpannableString(navigationView.getMenu().findItem(R.id.datas).getTitle());
        SpannableString s1 = new SpannableString(navigationView.getMenu().findItem(R.id.datas1).getTitle());
        SpannableString s2 = new SpannableString(navigationView.getMenu().findItem(R.id.datas2).getTitle());
        SpannableString s3 = new SpannableString(navigationView.getMenu().findItem(R.id.datas3).getTitle());
        SpannableString s4 = new SpannableString(navigationView.getMenu().findItem(R.id.datas4).getTitle());

        if (isDark) {
            hView.setBackground(ContextCompat.getDrawable(this, R.drawable.fadede_dark_blue));
            nav_user.setTextColor(Color.WHITE);
            toalword.setTextColor(Color.WHITE);
            scores.setTextColor(Color.WHITE);
            scores1.setTextColor(Color.WHITE);
            s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance45), 0, s.length(), 0);
            navigationView.getMenu().findItem(R.id.datas).setTitle(s);

            s1.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance45), 0, s1.length(), 0);
            navigationView.getMenu().findItem(R.id.datas1).setTitle(s1);

            s2.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance45), 0, s2.length(), 0);
            navigationView.getMenu().findItem(R.id.datas2).setTitle(s2);

            s3.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance45), 0, s3.length(), 0);
            navigationView.getMenu().findItem(R.id.datas3).setTitle(s3);

            s4.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance45), 0, s4.length(), 0);
            navigationView.getMenu().findItem(R.id.datas4).setTitle(s4);

        } else {
            hView.setBackground(ContextCompat.getDrawable(this, R.drawable.fade_blue));
            nav_user.setTextColor(Color.BLACK);
            toalword.setTextColor(Color.BLACK);
            scores.setTextColor(Color.BLACK);
            scores1.setTextColor(Color.BLACK);
            s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
            navigationView.getMenu().findItem(R.id.datas).setTitle(s);

            s1.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s1.length(), 0);
            navigationView.getMenu().findItem(R.id.datas1).setTitle(s1);

            s2.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s2.length(), 0);
            navigationView.getMenu().findItem(R.id.datas2).setTitle(s2);

            s3.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s3.length(), 0);
            navigationView.getMenu().findItem(R.id.datas3).setTitle(s3);

            s4.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s4.length(), 0);
            navigationView.getMenu().findItem(R.id.datas4).setTitle(s4);
        }


        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.scores);
        TextView navUsername1 = headerView.findViewById(R.id.scores1);
        TextView wordcount = headerView.findViewById(R.id.wordcount);


        //prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);


        navUsername.setText("Highest Score in multiple choice  : " + prefs.getInt("highscore", 0));
        navUsername1.setText("Highest Score in matching game  : " + prefs.getInt("highscore1", 0));


        navigationView.bringToFront();
        Menu menu = navigationView.getMenu();

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);

            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    // TODO: switch on menuItem.getItemId()
                    return false;
                }
            });
        }


       /* */




        List<word> wordList = new ArrayList<>();
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        int i =0;
        final Cursor cursor = mDBHelper.getAllData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                i++;

                word word = new word();

                if(!cursor.getString(3).isEmpty() && !cursor.getString(5).isEmpty()
                        && !cursor.getString(6).isEmpty()){
                    word.setID(i);
                    word.setWORD(cursor.getString(1));
                    word.setMEANINGB(cursor.getString(2));
                    word.setMEANINGE(cursor.getString(3));
                    word.setSYNONYMS(cursor.getString(5));
                    word.setANTONYMS(cursor.getString(6));
                }
                else if(cursor.getString(3).isEmpty()){

                    word.setID(i);
                    word.setWORD(cursor.getString(1));
                    word.setMEANINGB(cursor.getString(2));
                    word.setMEANINGE("None");
                    word.setSYNONYMS(cursor.getString(5));
                    word.setANTONYMS(cursor.getString(6));

                }else if(cursor.getString(5).isEmpty()){

                    word.setID(i);
                    word.setWORD(cursor.getString(1));
                    word.setMEANINGB(cursor.getString(2));
                    word.setMEANINGE(cursor.getString(3));
                    word.setSYNONYMS("None");
                    word.setANTONYMS(cursor.getString(6));

                }else if(cursor.getString(6).isEmpty()){

                    word.setID(i);
                    word.setWORD(cursor.getString(1));
                    word.setMEANINGB(cursor.getString(2));
                    word.setMEANINGE(cursor.getString(3));
                    word.setSYNONYMS(cursor.getString(5));
                    word.setANTONYMS("None");
                }




                wordList.add(word);
                
            }
        }

        TextView wordc = findViewById(R.id.word);
        Random r = new Random();
        wordc.setText(wordList.get(r.nextInt(wordList.size() + 1) + 0).getWORD());
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String date = day + "/" + (month + 1) + "/" + year;
        
        TextView sokal = findViewById(R.id.sokal);
        TextView bikal = findViewById(R.id.bikal);
        TextView ratir = findViewById(R.id.rait);
        if(day%2==0){
            sokal.setText("সকালে :  "+ "আন্তর্জাতিক + মানসিক দক্ষতা + গণিত");
            bikal.setText("বিকাল :  "+ "পেপার + কারেন্ট অ্যাফেয়ার্স");
            ratir.setText("রাত   :  "+"ইংরেজী + বাংলা + এক্সাম");
        }else{
            sokal.setText("সকালে :  "+ "ইংরেজী + বাংলাদেশ + গণিত");
            bikal.setText("বিকাল :  "+ "পেপার + জব সলিউসনস");
            ratir.setText("রাত   :  "+"বাংলা + বিজ্ঞান + ভূগোল ও সুশাষন");
        }

        wordcount.setText("Total Number Of Words : " + i);

        SimpleAnalogClock customAnalogClock = findViewById(R.id.analog_clock);


        Handler handler = new Handler();

        final Runnable ra = new Runnable() {
            public void run() {
                Calendar cq = Calendar.getInstance();
                int seconds = cq.get(Calendar.SECOND);
                int minutes = cq.get(Calendar.MINUTE);
                int hour = cq.get(Calendar.HOUR);


                customAnalogClock.setTime(hour,minutes,seconds);
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(ra, 1000);


     //   customAnalogClock.s(true);
       // customAnalogClock.setFace();

/*

*/



        ConstraintLayout constraintLayout1 = findViewById(R.id.content_main);
       // LinearLayout linearLayout1 = findViewById(R.id.listview);

        navigationView1 = findViewById(R.id.nav_view);

        int flag = 0;
        int flag1 = 1;
        int[][] states;
        int[] colors;
        ColorStateList colorStateList = null;
        if (isDark) {

            states = new int[][]{
                    new int[]{android.R.attr.state_enabled}, // checked
                    new int[]{-android.R.attr.state_enabled} // unchecked
            };
            colors = new int[]{
                    Color.WHITE,
                    Color.BLACK,
            };
            colorStateList = new ColorStateList(states, colors);

        }
        if (!isDark) {
            states = new int[][]{
                    new int[]{android.R.attr.state_enabled}, // checked
                    new int[]{-android.R.attr.state_enabled} // unchecked
            };
            colors = new int[]{
                    Color.BLACK,
                    Color.WHITE,
            };
            System.out.println("Darljkkj");
            colorStateList = new ColorStateList(states, colors);

        }
        customAnalogClock.setFaceDrawable(ContextCompat.getDrawable(this,R.drawable.ic_clock));

        navigationView1.setItemIconTintList(colorStateList);

        RelativeLayout relativeLayout = findViewById(R.id.bass);
        if (isDark) {

            constraintLayout1.setBackgroundColor(Color.BLACK);
            // linearLayout.setBackgroundColor(Color.BLACK);
            navigationView1.setBackgroundColor(Color.BLACK);

            Drawable icon = getResources().getDrawable(R.drawable.ic_content_paste_black_24dp);
            icon.setTint(Color.WHITE);
            navigationView1.getMenu().getItem(0).setIcon(icon);


            // ColorStateList colorStateList = new ColorStateList(states, colors);
            //navigationView1.setItemIconTintList(colorStateList);
            customAnalogClock.setHourTint(ContextCompat.getColor(this,R.color.uou));

           // customAnalogClock.setSecondDrawable(ContextCompat.getDrawable(this,R.drawable.ic_library_music_black_24dp));
            customAnalogClock.setSecondTint(ContextCompat.getColor(this,R.color.material_lightblueA400));
            customAnalogClock.setMinuteTint(ContextCompat.getColor(this,R.color.A400red));

            navigationView1.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
           relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.card_background_dark));
            customAnalogClock.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.card_background_dark));
            customAnalogClock.setFaceTint(ContextCompat.getColor(this, R.color.white));
            //if (contactList.size() != 0) list.setAdapter(adapter);

        } else {
            constraintLayout1.setBackgroundColor(Color.WHITE);
            navigationView1.setBackgroundColor(Color.WHITE);
            navigationView1.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
            // linearLayout.setBackgroundColor(Color.WHITE);
            customAnalogClock.setHourTint(ContextCompat.getColor(this,R.color.uou));
            customAnalogClock.setSecondTint(ContextCompat.getColor(this,R.color.material_lightblueA400));
            customAnalogClock.setMinuteTint(ContextCompat.getColor(this,R.color.A400red));
           relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.card_background));
            customAnalogClock.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.card_background));
            customAnalogClock.setFaceTint(ContextCompat.getColor(this, R.color.black));
            //if (contactList.size() != 0) list.setAdapter(adapter);
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isDark", isDark);
        editor.commit();

        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
        Intent intent = new Intent(this, add_news.class);

        intent.setAction(Intent.ACTION_VIEW);
        ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(MainActivity.this, "Shortcut_1")
                .setLongLabel("Add News!")
                .setShortLabel("Add News!")
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_news))
                .setIntent(intent)
                .build();
        shortcutManager.addDynamicShortcuts(Arrays.asList(shortcutInfo));

        //  ShortcutManager shortcutManager= (ShortcutManager) getSystemService(ShortcutManager.class);
        Intent intent1 = new Intent(this, add_page.class);

        intent1.setAction(Intent.ACTION_VIEW);
        ShortcutInfo shortcutInfo1 = new ShortcutInfo.Builder(MainActivity.this, "Shortcut_2")
                .setLongLabel("Add Word")
                .setShortLabel("Add Word")
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_add))
                .setIntent(intent1)
                .build();
        shortcutManager.addDynamicShortcuts(Arrays.asList(shortcutInfo1));


        Intent intent4 = new Intent(this, music_base.class);


        intent4.setAction(Intent.ACTION_VIEW);
        ShortcutInfo shortcutInfo4 = new ShortcutInfo.Builder(MainActivity.this, "Shortcut_4")
                .setLongLabel("Play Song")
                .setShortLabel("Play Song")
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_music))
                .setIntent(intent4)
                .build();
        shortcutManager.addDynamicShortcuts(Arrays.asList(shortcutInfo4));


        Intent intent5 = new Intent(this, Promotodo_activity.class);

        intent5.setAction(Intent.ACTION_VIEW);
        ShortcutInfo shortcutInfo5 = new ShortcutInfo.Builder(MainActivity.this, "Shortcut_5")
                .setLongLabel("Promotodo")
                .setShortLabel("Promotodo")
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_time))
                .setIntent(intent5)
                .build();
        shortcutManager.addDynamicShortcuts(Arrays.asList(shortcutInfo5));


        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent intenta = new Intent(this, daily_service.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intenta, 0);

        Calendar cal= Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 58);
       // alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pIntent);



        if (Build.VERSION.SDK_INT >= 23) {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    cal.getTimeInMillis(), pIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pIntent);
        } else {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pIntent);
        }
    }



    public void showMessage(String title, String Message) {

        AlertDialog.Builder builder;
        if (isDark) {
            builder = new AlertDialog.Builder(this, R.style.DialogurDark);
        } else {
            builder = new AlertDialog.Builder(this, R.style.DialogueLight);
        }

        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_send) {
            // Handle the camera action
            //Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(MainActivity.this, backup_restore.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(myIntent, 0);

        }
        else if(id == R.id.nav_daily){
            Intent myIntent = new Intent(this, daily_details.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(myIntent, 0);
            return true;
        }
        else if (id == R.id.nav_view) {
            Intent myIntent = new Intent(MainActivity.this, news_backup.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(myIntent, 0);
        }
        else if (id == R.id.pro_back) {
            Intent myIntent = new Intent(MainActivity.this, pro_backup.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(myIntent, 0);
        }
        else if (id == R.id.subscription) {


            Calendar mcurrentTime = Calendar.getInstance();
            final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            final int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0,
                            new Intent(MainActivity.this, Mainservice.class),
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    Calendar calendar = Calendar.getInstance();
                    // set the triggered time to currentHour:08:00 for testing
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    calendar.set(Calendar.MINUTE, selectedMinute);
                    calendar.set(Calendar.SECOND, 0);

                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent);


                    //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), , pendingIntent);


                    Toasty.success(MainActivity.this, "Success! " + selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT, true).show();
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();


        }
        else if (id == R.id.nav_home) {


                SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                MainActivity.score = prefs.getInt("highscore", 0);
                Intent myIntent = new Intent(MainActivity.this, Quiz_confirm.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);


        }
        else if (id == R.id.promotodo) {

            try {
                Intent myIntent = new Intent(MainActivity.this, Promotodo_activity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            } catch (Exception e) {
                e.getMessage();
            }

        }
        else if (id == R.id.nav_word) {

            try {
                Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            } catch (Exception e) {
                e.getMessage();
            }

        }
        else if (id == R.id.news) {

            try {
                Intent myIntent = new Intent(MainActivity.this, news_activity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            } catch (Exception e) {
                e.getMessage();
            }

        }
        else if(id == R.id.nav_back){

            Calendar mcurrentTime = Calendar.getInstance();
            final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            final int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    Calendar calendar = Calendar.getInstance();
                    // set the triggered time to currentHour:08:00 for testing
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    calendar.set(Calendar.MINUTE, selectedMinute);
                    calendar.set(Calendar.SECOND, 0);

                    if (android.os.Build.VERSION.SDK_INT >= 23) {
                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                selectedHour, selectedMinute, 0);
                    } else {
                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                selectedHour, selectedMinute, 0);
                    }

                    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    //creating a new intent specifying the broadcast receiver
                    Intent i = new Intent(MainActivity.this, receive_back.class);

                    //creating a pending intent using the intent
                    PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, i, 0);

                    //setting the repeating alarm that will be fired every day
                    am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
                    Toasty.success(MainActivity.this, "schedule is set", Toast.LENGTH_SHORT).show();
                    //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), , pendingIntent);


                    //Toasty.success(MainActivity.this, "Success! " + selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT, true).show();
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time To Auto Backup.");
            mTimePicker.show();
        }
        else if (id == R.id.news_online) {
            try {
                Intent myIntent = new Intent(MainActivity.this, Editorialonline.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        else if (id == R.id.tree) {
            try {
                Intent myIntent = new Intent(MainActivity.this, tree.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        else if (id == R.id.alarmme) {
            try {
                Intent myIntent = new Intent(MainActivity.this, AlarmMe.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        else if (id == R.id.dairy) {
            try {
                Intent myIntent = new Intent(MainActivity.this, DiaryMain.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        else if (id == R.id.tasktime) {
            try {
                Intent myIntent = new Intent(MainActivity.this, TimelineView.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        else if (id == R.id.music_base) {
            try {
                Intent myIntent = new Intent(MainActivity.this, music_base.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        else if (id == R.id.nav_tools) {
            mContext = getApplicationContext();
            mActivity = MainActivity.this;


            if (!isDark && prefs.getInt("size",0) != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogueLight);
                builder.setTitle(R.string.darkmode);
                builder.setMessage(R.string.yes);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    ConstraintLayout constraintLayout = findViewById(R.id.content_main);
                  //  LinearLayout linearLayout = findViewById(R.id.listview);

                    NavigationView navigationView = findViewById(R.id.nav_view);

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        isDark = true;
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("isDark", isDark);
                        editor.commit();
                        constraintLayout.setBackgroundColor(Color.BLACK);
                        // linearLayout.setBackgroundColor(Color.BLACK);
                        navigationView.setBackgroundColor(Color.BLACK);
                        navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
                      //  linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.card_background_dark));
                        //list.setAdapter(adapter);

                        Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);

                        Toasty.success(mContext, "Enabled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("NO", null);
                builder.show();
            } else if (isDark && prefs.getInt("size",0) != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogurDark);
                builder.setTitle(R.string.darkmode);
                builder.setMessage(R.string.no);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    ConstraintLayout constraintLayout = findViewById(R.id.content_main);
                   // LinearLayout linearLayout = findViewById(R.id.listview);

                    NavigationView navigationView = findViewById(R.id.nav_view);

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isDark = false;
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("isDark", isDark);
                        editor.commit();
                        constraintLayout.setBackgroundColor(Color.WHITE);
                        navigationView.setBackgroundColor(Color.WHITE);
                        navigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
                        // linearLayout.setBackgroundColor(Color.WHITE);
                       // linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.card_background));
                       // list.setAdapter(adapter);

                        Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);

                        Toasty.success(mContext, "Disabled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("NO", null);
                builder.show();


            } else if (!isDark && prefs.getInt("size",0) == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogueLight);
                builder.setTitle(R.string.darkmode);
                builder.setMessage(R.string.yes);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    ConstraintLayout constraintLayout = findViewById(R.id.content_main);


                    NavigationView navigationView = findViewById(R.id.nav_view);

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isDark = true;
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("isDark", isDark);
                        editor.commit();
                        constraintLayout.setBackgroundColor(Color.BLACK);
                        // linearLayout.setBackgroundColor(Color.BLACK);
                        navigationView.setBackgroundColor(Color.BLACK);
                        navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));



                        Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);

                        Toasty.success(mContext, "Enabled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("NO", null);
                builder.show();
            } else if (isDark && prefs.getInt("size",0) == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogurDark);
                builder.setTitle(R.string.darkmode);
                builder.setMessage(R.string.no);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    ConstraintLayout constraintLayout = findViewById(R.id.content_main);


                    NavigationView navigationView = findViewById(R.id.nav_view);

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isDark = false;
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("isDark", isDark);
                        editor.commit();
                        constraintLayout.setBackgroundColor(Color.WHITE);
                        navigationView.setBackgroundColor(Color.WHITE);
                        //navigationView.setItemTextColor();
                        navigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
                        // linearLayout.setBackgroundColor(Color.WHITE);


                        Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);

                        Toasty.success(mContext, "Disabled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("NO", null);
                builder.show();
            }


        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        finishAffinity();
        finish();

    }
    @Override
    protected void onResume() {
        super.onResume();


    }




    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            Toasty.info(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }




}
