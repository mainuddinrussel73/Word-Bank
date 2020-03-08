package com.example.czgame.wordbank.ui.alertme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.czgame.wordbank.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class AlarmMe extends AppCompatActivity
{
    private final String TAG = "AlarmMe";
    private final int NEW_ALARM_ACTIVITY = 0;
    private final int EDIT_ALARM_ACTIVITY = 1;
    private final int PREFERENCES_ACTIVITY = 2;
    private final int ABOUT_ACTIVITY = 3;
    private final int CONTEXT_MENU_EDIT = 0;
    private final int CONTEXT_MENU_DELETE = 1;
    private final int CONTEXT_MENU_DUPLICATE = 2;
    private ListView mAlarmList;
    private AlarmListAdapter mAlarmListAdapter;
    private Alarm mCurrentAlarm;
    private AdapterView.OnItemClickListener mListOnItemClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Intent intent = new Intent(getBaseContext(), EditAlarm.class);

            mCurrentAlarm = mAlarmListAdapter.getItem(position);
            mCurrentAlarm.toIntent(intent);
            AlarmMe.this.startActivityForResult(intent, EDIT_ALARM_ACTIVITY);
        }


    };

    @Override
    public void onCreate(Bundle bundle)
    {




        super.onCreate(bundle);


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        setContentView(R.layout.activity_alermmain);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Log.i(TAG, "AlarmMe.onCreate()");

        mAlarmList = findViewById(R.id.alarm_list);

        mAlarmListAdapter = new AlarmListAdapter(this);
        mAlarmList.setAdapter(mAlarmListAdapter);
        mAlarmList.setOnItemClickListener(mListOnItemClickListener);
        registerForContextMenu(mAlarmList);

        mCurrentAlarm = null;


        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        RelativeLayout laybase = findViewById(R.id.laybase);
        RelativeLayout lidtb = findViewById(R.id.alarm_list_r);

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if (isDark && mAlarmListAdapter.getCount() != 0) {

            laybase.setBackgroundColor(Color.BLACK);
            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
            mAlarmList.setBackgroundColor(Color.BLACK);
            mAlarmList.setAdapter(mAlarmListAdapter);
        } else if (!isDark && mAlarmListAdapter.getCount()!= 0) {

            laybase.setBackgroundColor(Color.WHITE);
            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));
            mAlarmList.setBackgroundColor(Color.WHITE);
            mAlarmList.setAdapter(mAlarmListAdapter);

        } else if (isDark && mAlarmListAdapter.getCount() == 0) {


            laybase.setBackgroundColor(Color.BLACK);
            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
            mAlarmList.setBackgroundColor(Color.BLACK);

        } else if (!isDark && mAlarmListAdapter.getCount() == 0) {

            laybase.setBackgroundColor(Color.WHITE);
            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));
            mAlarmList.setBackgroundColor(Color.WHITE);


        }


        mAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int pos, long id) {
                // TODO Auto-generated method stub


                Context wrapper = new ContextThemeWrapper(AlarmMe.this, R.style.YOURSTYLE1);
                SharedPreferences prefs = AlarmMe.this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                boolean isDark = prefs.getBoolean("isDark", false);
                if (isDark) {
                    wrapper = new ContextThemeWrapper(AlarmMe.this, R.style.YOURSTYLE);

                } else {
                    wrapper = new ContextThemeWrapper(AlarmMe.this, R.style.YOURSTYLE1);
                }

                PopupMenu popup = new PopupMenu(wrapper, view);
                popup.getMenuInflater().inflate(R.menu.alarm_menu, popup.getMenu());
                //
                popup.show();


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Delete")){
                            mAlarmListAdapter.delete(pos);
                        }else  if(item.getTitle().equals("Duplicate")){
                            Alarm alarm = mAlarmListAdapter.getItem(pos);
                            Alarm newAlarm = new Alarm(AlarmMe.this);
                            Intent intent = new Intent();

                            alarm.toIntent(intent);
                            newAlarm.fromIntent(intent);
                            newAlarm.setTitle(alarm.getTitle() + " (copy)");
                            mAlarmListAdapter.add(newAlarm);
                        }
                        return true;
                    }
                });

                return true;
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i(TAG, "AlarmMe.onDestroy()");
//    mAlarmListAdapter.save();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.i(TAG, "AlarmMe.onResume()");
        mAlarmListAdapter.updateAlarms();
    }

    public void onAddAlarmClick(View view)
    {
        Intent intent = new Intent(getBaseContext(), EditAlarm.class);

        mCurrentAlarm = new Alarm(this);
        mCurrentAlarm.toIntent(intent);

        AlarmMe.this.startActivityForResult(intent, NEW_ALARM_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_ALARM_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                mCurrentAlarm.fromIntent(data);
                mAlarmListAdapter.add(mCurrentAlarm);
            }
            mCurrentAlarm = null;
        } else if (requestCode == EDIT_ALARM_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                mCurrentAlarm.fromIntent(data);
                mAlarmListAdapter.update(mCurrentAlarm);
            }
            mCurrentAlarm = null;
        } else if (requestCode == PREFERENCES_ACTIVITY) {
            mAlarmListAdapter.onSettingsUpdated();
        }
    }

}
