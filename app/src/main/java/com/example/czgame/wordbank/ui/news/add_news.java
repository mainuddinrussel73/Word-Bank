package com.example.czgame.wordbank.ui.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.czgame.wordbank.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;

public class add_news extends AppCompatActivity {


    TextInputEditText title;
    TextInputLayout t;
    TextInputEditText body;
    TextInputLayout b;
    TextInputEditText url;
    TextInputLayout u;
    Button save;
    ScrollView scrollview;
    private DBNewsHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        t = findViewById(R.id.titlelay);
        body = findViewById(R.id.body1);
        b = findViewById(R.id.bodylay);
        url = findViewById(R.id.url1);
        u = findViewById(R.id.urllay);

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
        final RelativeLayout additem = findViewById(R.id.add_news_item);

        SharedPreferences prefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);
        if(isDark) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.black));
        }else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
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
            t.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(add_news.this, R.color.divider)));
            b.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(add_news.this, R.color.divider)));
            u.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(add_news.this, R.color.divider)));
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
            t.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(add_news.this, R.color.darkgray)));
            b.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(add_news.this, R.color.darkgray)));
            u.setDefaultHintTextColor( ColorStateList.valueOf(ContextCompat.getColor(add_news.this, R.color.darkgray)));
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

                    Display display = getWindowManager().getDefaultDisplay();
                    int width = display.getWidth();

                    String dataq = "<html><head><meta name=\"viewport\"\"content=\"width="+width+" height="+width+ ", initial-scale=1 \" /></head>";
                    dataq = dataq + "<body>"+ body.getText().toString() +"</body></html>";

                    String stringToAdd = "width=\"100%\" ";

                    // Create a StringBuilder to insert string in the middle of content.
                    StringBuilder sb = new StringBuilder(dataq);

                    int i = 0;
                    int cont = 0;

                    // Check for the "src" substring, if it exists, take the index where
                    // it appears and insert the stringToAdd there, then increment a counter
                    // because the string gets altered and you should sum the length of the inserted substring
                    while(i != -1){
                        i = dataq.indexOf("src", i + 1);
                        if(i != -1) sb.insert(i + (cont * stringToAdd.length()), stringToAdd );
                        ++cont;
                    }




                    boolean b;
                    if (url.getText().toString().isEmpty()) {
                        b = mDBHelper.insertData(title.getText().toString(), dataq, "empty");
                    } else
                        b = mDBHelper.insertData(title.getText().toString(), dataq, url.getText().toString());
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

