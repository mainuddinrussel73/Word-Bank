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
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.example.czgame.wordbank.ui.Quiz.Quiz_confirm;
import com.example.czgame.wordbank.ui.backup_scheudle.receive_back;
import com.example.czgame.wordbank.ui.media.Media_list_activity;
import com.example.czgame.wordbank.ui.news.add_news;
import com.example.czgame.wordbank.ui.news.news_activity;
import com.example.czgame.wordbank.ui.news.news_backup;
import com.example.czgame.wordbank.ui.promotodo.Promotodo_activity;
import com.example.czgame.wordbank.ui.promotodo.daily_details;
import com.example.czgame.wordbank.ui.promotodo.pro_backup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import es.dmoral.toasty.Toasty;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public  static boolean isfirst = false;
    public static final int PICKFILE_RESULT_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static boolean isDark;
    public static Activity mActivity;
    public static int score = 1111111;
    public static boolean isChecked = false;
    public static int size = 0;
    public static List<word> contactList = new ArrayList<word>();
    public static SharedPreferences prefs;
    public static SharedPreferences sizee;
    ActionBarDrawerToggle toggle;
    com.suke.widget.SwitchButton switchButton;
    SearchView searchView;
    SearchView searchView1;
    Button sort;
    ListView list;
    DrawerLayout drawer;
    MyListAdapter adapter;
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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mActivity = this;





        prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);


        isDark = prefs.getBoolean("isDark", false);


        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {

                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission();
            }
        } else {

        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), add_page.class);
                //String s = view.findViewById(R.id.subtitle).toString();
                //String s = (String) parent.getI;
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            }
        });


        ///Intent intent=new Intent(MainActivity.this,quiz_page.class);
        //Intent intent = new Intent(Intent.ACTION_VIEW, null, MainActivity.this, quiz_page.class);


        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.nav_view);


        SpannableString s = new SpannableString(navigationView.getMenu().findItem(R.id.datas).getTitle());
        SpannableString s1 = new SpannableString(navigationView.getMenu().findItem(R.id.datas1).getTitle());
        SpannableString s2 = new SpannableString(navigationView.getMenu().findItem(R.id.datas2).getTitle());
        SpannableString s3 = new SpannableString(navigationView.getMenu().findItem(R.id.datas3).getTitle());
        SpannableString s4 = new SpannableString(navigationView.getMenu().findItem(R.id.datas4).getTitle());

        if (isDark) {
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
        TextView wordcount = headerView.findViewById(R.id.wordcount);


        //prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);


        navUsername.setText("Highest Score is : " + prefs.getInt("highscore", 0));


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


        DatabaseHelper mDBHelper = new DatabaseHelper(this);


        contactList.clear();

        int i =1;

        final Cursor cursor = mDBHelper.getAllData();

        // looping through all rows and adding to list
        if (cursor.getCount() != 0) {
            // show message
            while (cursor.moveToNext()) {

                word word = new word();
                word.setID(i);
                word.setWORD(cursor.getString(1));
                word.setMEANING(cursor.getString(2));

                contactList.add(word);

                // maintitle.add(word.WORD);
                // subtitle.add(word.MEANING);

                i++;
            }


            // size = contactList.size();
            adapter = new MyListAdapter(this);
            list = findViewById(R.id.list);

            String type = prefs.getString("sort", "asc");
            if (type.equals("asc")) {
                Collections.sort(contactList);
            } else if (type.equals("des")) {
                Collections.sort(contactList, Collections.reverseOrder());
            } else if (type.equals("alp")) {
                Collections.sort(contactList,
                        new Comparator<word>() {
                            public int compare(word f1, word f2) {
                                return f1.getWORD().compareTo(f2.getWORD());
                            }
                        });
            }

            //list.setFastScrollEnabled(true);
            list.setAdapter(adapter);

            //textView = findViewById(R.id.board);
            //final Intent intent = getIntent();
            //bestscore = (intent.getIntExtra("sb",0)> bestscore)?intent.getIntExtra("sb",0) :bestscore;
//            textView.setText("Total Words : \n"+String.valueOf(contactList.size())+"\nCurrent score : \n"+String.valueOf(bestscore));


            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    // System.out.println(position);
                    Intent myIntent = new Intent(view.getContext(), WordDetail.class);
                    //String s = view.findViewById(R.id.subtitle).toString();
                    //String s = (String) parent.getI;
                    myIntent.putExtra("message", contactList.get(position).getWORD());
                    myIntent.putExtra("meaning", contactList.get(position).getMEANING());
                    myIntent.putExtra("id", position);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(myIntent, 0);

                }
            });
        } else {

            showMessage("Error", "Nothing found");
        }

        size = contactList.size();

        sizee = this.getSharedPreferences("ok", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sizee.edit();
        editor.putInt("size", size);
        editor.commit();



        System.out.println(MainActivity.sizee.getInt("size", 0));


        wordcount.setText("Total Number Of Words : " + MainActivity.sizee.getInt("size", 0));


        sort = findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                //PopupMenu popup = new PopupMenu(MainActivity.this, sort);
                //Inflating the Popup using xml file
                Context wrapper = new ContextThemeWrapper(mContext, R.style.YOURSTYLE1);
                if (isDark) {
                    wrapper = new ContextThemeWrapper(MainActivity.this, R.style.YOURSTYLE);

                } else {
                    wrapper = new ContextThemeWrapper(MainActivity.this, R.style.YOURSTYLE1);
                }

                PopupMenu popup = new PopupMenu(wrapper, sort);
                popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());


                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Ascending")) {
                            Collections.sort(contactList);
                            //  adapter = new MyListAdapter(getParent());
                            list = findViewById(R.id.list);
                            list.setAdapter(adapter);

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("sort", "asc");
                            editor.commit();

                        } else if (item.getTitle().equals("Descending")) {
                            Collections.sort(contactList, Collections.reverseOrder());
                            //adapter = new MyListAdapter(getParent());
                            list = findViewById(R.id.list);
                            list.setAdapter(adapter);

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("sort", "des");
                            editor.commit();

                        } else if (item.getTitle().equals("Alphabetically")) {
                            Collections.sort(contactList,
                                    new Comparator<word>() {
                                        public int compare(word f1, word f2) {
                                            return f1.getWORD().compareTo(f2.getWORD());
                                        }
                                    });
                            list = findViewById(R.id.list);
                            list.setAdapter(adapter);

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("sort", "alp");
                            editor.commit();

                        } else {

                        }

                        Toasty.info(MainActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method


        ConstraintLayout constraintLayout1 = findViewById(R.id.content_main);
        LinearLayout linearLayout1 = findViewById(R.id.listview);

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

        navigationView1.setItemIconTintList(colorStateList);

        if (isDark) {

            constraintLayout1.setBackgroundColor(Color.BLACK);
            // linearLayout.setBackgroundColor(Color.BLACK);
            navigationView1.setBackgroundColor(Color.BLACK);

            Drawable icon = getResources().getDrawable(R.drawable.ic_content_paste_black_24dp);
            icon.setTint(Color.WHITE);
            navigationView1.getMenu().getItem(0).setIcon(icon);


            // ColorStateList colorStateList = new ColorStateList(states, colors);
            //navigationView1.setItemIconTintList(colorStateList);


            navigationView1.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
            linearLayout1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.list_viewdark));
            if (contactList.size() != 0) list.setAdapter(adapter);

        } else {
            constraintLayout1.setBackgroundColor(Color.WHITE);
            navigationView1.setBackgroundColor(Color.WHITE);
            navigationView1.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
            // linearLayout.setBackgroundColor(Color.WHITE);
            linearLayout1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.listview_border));

            if (contactList.size() != 0) list.setAdapter(adapter);
        }


        editor = prefs.edit();
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


        Intent intent4 = new Intent(this, Media_list_activity.class);

        intent4.putExtra("size", size);
        intent4.setAction(Intent.ACTION_VIEW);
        ShortcutInfo shortcutInfo4 = new ShortcutInfo.Builder(MainActivity.this, "Shortcut_4")
                .setLongLabel("Play Song")
                .setShortLabel("Play Song")
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_music))
                .setIntent(intent4)
                .build();
        shortcutManager.addDynamicShortcuts(Arrays.asList(shortcutInfo4));


        Intent intent5 = new Intent(this, Promotodo_activity.class);

        intent5.putExtra("size", size);
        intent5.setAction(Intent.ACTION_VIEW);
        ShortcutInfo shortcutInfo5 = new ShortcutInfo.Builder(MainActivity.this, "Shortcut_5")
                .setLongLabel("Promotodo")
                .setShortLabel("Promotodo")
                .setIcon(Icon.createWithResource(this, R.mipmap.ic_time))
                .setIntent(intent5)
                .build();
        shortcutManager.addDynamicShortcuts(Arrays.asList(shortcutInfo5));


    }

    public void showSequence(View view) {




        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(searchView)
                .setPrimaryText("Primary text")
                .setIcon(R.drawable.ic_searchw)
                .show();



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

        }else if(id == R.id.nav_daily){
            Intent myIntent = new Intent(this, daily_details.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(myIntent, 0);
            return true;
        }
        else if (id == R.id.nav_view) {
            Intent myIntent = new Intent(MainActivity.this, news_backup.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(myIntent, 0);
        } else if (id == R.id.pro_back) {
            Intent myIntent = new Intent(MainActivity.this, pro_backup.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(myIntent, 0);
        } else if (id == R.id.subscription) {


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


        } else if (id == R.id.nav_home) {

            if (contactList.size() >= 4) {
                SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                MainActivity.score = prefs.getInt("highscore", 0);
                Intent myIntent = new Intent(MainActivity.this, Quiz_confirm.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            } else {
                showMessage("Sorry", "Collect more then 4 words.");
            }

        } else if (id == R.id.promotodo) {

            try {
                Intent myIntent = new Intent(MainActivity.this, Promotodo_activity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            } catch (Exception e) {
                e.getMessage();
            }

        } else if (id == R.id.news) {

            try {
                Intent myIntent = new Intent(MainActivity.this, news_activity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            } catch (Exception e) {
                e.getMessage();
            }

        }else if(id == R.id.nav_back){

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
        } else if (id == R.id.music) {
            try {
                Intent myIntent = new Intent(MainActivity.this, Media_list_activity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(myIntent, 0);
            } catch (Exception e) {
                e.getMessage();
            }
        } else if (id == R.id.nav_tools) {
            mContext = getApplicationContext();
            mActivity = MainActivity.this;


            if (!isDark && contactList.size() != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogueLight);
                builder.setTitle(R.string.darkmode);
                builder.setMessage(R.string.yes);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    ConstraintLayout constraintLayout = findViewById(R.id.content_main);
                    LinearLayout linearLayout = findViewById(R.id.listview);

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
                        linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.list_viewdark));
                        list.setAdapter(adapter);

                        Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);

                        Toasty.success(mContext, "Enabled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("NO", null);
                builder.show();
            } else if (isDark && contactList.size() != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogurDark);
                builder.setTitle(R.string.darkmode);
                builder.setMessage(R.string.no);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    ConstraintLayout constraintLayout = findViewById(R.id.content_main);
                    LinearLayout linearLayout = findViewById(R.id.listview);

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
                        linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.listview_border));
                        list.setAdapter(adapter);

                        Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);

                        Toasty.success(mContext, "Disabled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("NO", null);
                builder.show();


            } else if (!isDark && contactList.size() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogueLight);
                builder.setTitle(R.string.darkmode);
                builder.setMessage(R.string.yes);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    ConstraintLayout constraintLayout = findViewById(R.id.content_main);
                    LinearLayout linearLayout = findViewById(R.id.listview);

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
                        linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.list_viewdark));


                        Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);

                        Toasty.success(mContext, "Enabled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("NO", null);
                builder.show();
            } else if (isDark && contactList.size() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogurDark);
                builder.setTitle(R.string.darkmode);
                builder.setMessage(R.string.no);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    ConstraintLayout constraintLayout = findViewById(R.id.content_main);
                    LinearLayout linearLayout = findViewById(R.id.listview);

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
                        linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.listview_border));

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
    }
    @Override
    protected void onResume() {
        super.onResume();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);


        searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_searchw);

        searchView1 = (SearchView) menu.findItem(R.id.app_bar_search1).getActionView();
        searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        v = searchView1.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_searchm);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
             /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }

        });
        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView1.clearFocus();
             /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter1(newText);
                return false;
            }

        });


        if (isfirst) {
            isfirst = false;
            new MaterialTapTargetSequence()
                    .addPrompt(new MaterialTapTargetPrompt.Builder(MainActivity.this)
                            .setTarget(findViewById(R.id.fab))
                            .setPrimaryText("Step 1")
                            .setSecondaryText("Add Word")
                            .setSecondaryTextSize(R.dimen.helper_text_size)
                            .setFocalPadding(R.dimen.dp40)
                            .create(), 4000)
                    .addPrompt(new MaterialTapTargetPrompt.Builder(this)
                            .setPrimaryText("Step 2")
                            .setSecondaryText("Sort Words")
                            .setSecondaryTextSize(R.dimen.helper_text_size)
                            .setAnimationInterpolator(new FastOutSlowInInterpolator())
                            .setMaxTextWidth(R.dimen.dp40)
                            .setIcon(R.drawable.ic_sort_black_24dp)
                            .setTarget(R.id.sort)
                            .create(), 4000)
                    .addPrompt(new MaterialTapTargetPrompt.Builder(this)
                            .setPrimaryText("Step 3")
                            .setSecondaryText("Search by word")
                            .setSecondaryTextSize(R.dimen.helper_text_size)
                            .setAnimationInterpolator(new FastOutSlowInInterpolator())
                            .setMaxTextWidth(R.dimen.dp40)
                            .setIcon(R.drawable.ic_searchw)
                            .setTarget(searchView)
                            .create(), 4000)
                    .addPrompt(new MaterialTapTargetPrompt.Builder(MainActivity.this)
                            .setTarget(findViewById(R.id.app_bar_search1))
                            .setPrimaryText("Step 4")
                            .setSecondaryText("Search by meaning")
                            .setSecondaryTextSize(R.dimen.helper_text_size)
                            .setAnimationInterpolator(new LinearOutSlowInInterpolator())
                            .setFocalPadding(R.dimen.dp40)
                            .setTarget(searchView1)
                            .setIcon(R.drawable.ic_searchm)
                            .create(), 4000)
                    .addPrompt(new MaterialTapTargetPrompt.Builder(this)
                            .setPrimaryText("Step 5")
                            .setSecondaryText("Other Options : \n 1) Quiz \n 2) News \n 3) Music \n 4) Promotodo")
                            .setSecondaryTextSize(R.dimen.helper_text_size)
                            .setAnimationInterpolator(new FastOutSlowInInterpolator())
                            .setMaxTextWidth(R.dimen.dp40)
                            .setIcon(R.drawable.ic_menu_black_24dp)
                            .setTarget(navigationView)
                            )
                    .show();
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
        }
        return super.onCreateOptionsMenu(menu);
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
