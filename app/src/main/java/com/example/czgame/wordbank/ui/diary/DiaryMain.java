package com.example.czgame.wordbank.ui.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.etsy.android.grid.StaggeredGridView;
import com.example.czgame.wordbank.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class DiaryMain extends AppCompatActivity {
    public static StaggeredGridView listView;
    public  static  SqliteDatabase db;
    public static  ArrayList<Info> arrayList;
    ArrayList<String> selectList = new ArrayList<String>();
    ArrayList<Integer> unDeleteSelect = new ArrayList<Integer>();

    public  static ArrayAdapter arrayAdapter;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_diary);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        db = new SqliteDatabase(this);
        SQLiteDatabase sqliteDatabase = db.getWritableDatabase();

        listView = findViewById(R.id.ListviewId);

        arrayList=new ArrayList<Info>();

        FloatingActionButton fab = findViewById(R.id.fab);

        FloatingActionButton fab2 = findViewById(R.id.fab2);

        // ClickListener for floating action bar
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryMain.this,AddData.class);
                startActivity(intent);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryMain.this,AddDataVoice.class);
                startActivity(intent);
            }
        });

        Cursor cursor = db.display();
        while (cursor.moveToNext()) {
            Info information = new Info(cursor.getString(0),cursor.getString(1),
                    cursor.getString(2),cursor.getString(3),cursor.getString(4));
            arrayList.add(information);
            // System.out.println(information.toString());
        }

        Collections.reverse(arrayList);//reversing arrayList for showing data in a proper way

        arrayAdapter = new InfoAdapter(this, arrayList);//passing context and arrayList to arrayAdapter
        listView.setAdapter(arrayAdapter);

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        RelativeLayout laybase = findViewById(R.id.laybase);
        RelativeLayout lidtb = findViewById(R.id.opp);


            toolbar.setTitleTextColor(getResources().getColor(R.color.material_white));

        if (isDark && arrayAdapter.getCount() != 0) {

            laybase.setBackgroundColor(Color.BLACK);
            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
            listView.setBackgroundColor(Color.BLACK);
            listView.setAdapter(arrayAdapter);
        } else if (!isDark && arrayAdapter.getCount()!= 0) {

            laybase.setBackgroundColor(Color.WHITE);
            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));
            listView.setBackgroundColor(Color.WHITE);
            listView.setAdapter(arrayAdapter);

        } else if (isDark && arrayAdapter.getCount() == 0) {


            laybase.setBackgroundColor(Color.BLACK);
            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background_dark));
            listView.setBackgroundColor(Color.BLACK);

        } else if (!isDark && arrayAdapter.getCount() == 0) {

            laybase.setBackgroundColor(Color.WHITE);
            lidtb.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_background));
            listView.setBackgroundColor(Color.WHITE);


        }



    }







}
