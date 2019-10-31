package com.example.czgame.wordbank.ui.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.czgame.wordbank.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class add_news extends AppCompatActivity {


    EditText title;
    EditText body;
    EditText url;
    Button save;
    ScrollView scrollview;
    private DBNewsHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        title = findViewById(R.id.title1);
        body = findViewById(R.id.body1);
        url = findViewById(R.id.url1);

        body.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (body.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

        mDBHelper = new DBNewsHelper(this);
        save = findViewById(R.id.save);
        final LinearLayout additem = findViewById(R.id.add_news_item);

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        if (isDark) {
            additem.setBackgroundColor(Color.BLACK);
            title.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            body.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            url.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.edittextstyledark));
            title.setHintTextColor(Color.rgb(185, 185, 185));
            body.setHintTextColor(Color.rgb(185, 185, 185));
            url.setHintTextColor(Color.rgb(185, 185, 185));
            title.setTextColor(Color.WHITE);
            body.setTextColor(Color.WHITE);
            url.setTextColor(Color.WHITE);
        } else {
            additem.setBackgroundColor(Color.WHITE);
            title.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            body.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            url.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.editextstyle));
            title.setHintTextColor(Color.BLACK);
            body.setHintTextColor(Color.BLACK);
            url.setHintTextColor(Color.BLACK);
            title.setTextColor(Color.BLACK);
            body.setTextColor(Color.BLACK);
            url.setTextColor(Color.BLACK);
        }


        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub

                if (!arg1) {
                    DBNewsHelper mDBHelper = new DBNewsHelper(add_news.this);
                    // SQLiteDatabase mDb;
                    String id = "-1";
                    //SQLiteDatabase db = mDBHelper.getWritableDatabase();
                    SQLiteDatabase db1 = mDBHelper.getReadableDatabase();
                    try {
                        Cursor re = db1.rawQuery("SELECT * FROM news_table WHERE WORD = ?; ", new String[]{title.getText().toString()});
                        if (re.moveToFirst()) {
                            do {
                                System.out.println(re.getString(0));
                                id = re.getString(0);
                            } while (re.moveToNext());
                        }

                        re.close();
                        // System.out.println(re.getString(0));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    if (!id.equals("-1")) {
                        Toasty.warning(add_news.this, "Data already exist", Toasty.LENGTH_LONG).show();
                        save.setEnabled(false);
                    } else {
                        save.setEnabled(true);
                    }
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            int i = 0;

            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty() || title.getText().toString().trim().length() <= 0) {
                    Toasty.error(getApplicationContext(), "No input.", Toast.LENGTH_SHORT).show();
                } else if (body.getText().toString().isEmpty() || body.getText().toString().trim().length() <= 0) {
                    Toasty.error(getApplicationContext(), "No input.", Toast.LENGTH_SHORT).show();
                } else {
                    boolean b;
                    if (url.getText().toString().isEmpty()) {
                        b = mDBHelper.insertData(title.getText().toString(), body.getText().toString(), "empty");
                    } else
                        b = mDBHelper.insertData(title.getText().toString(), body.getText().toString(), url.getText().toString());
                    if (b == true) {
                        Toasty.success(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(v.getContext(), news_activity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(myIntent, 0);
                    } else {
                        Toasty.error(getApplicationContext(), "opps.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }
}

